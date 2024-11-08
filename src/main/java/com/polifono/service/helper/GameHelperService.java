package com.polifono.service.helper;

import static com.polifono.common.TemplateConstants.URL_BUY_CREDITS;
import static com.polifono.common.TemplateConstants.URL_GAMES_END_OF_GAME;
import static com.polifono.common.TemplateConstants.URL_GAMES_END_OF_LEVEL;
import static com.polifono.common.TemplateConstants.URL_GAMES_RESULT_TEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.polifono.domain.Content;
import com.polifono.domain.Diploma;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Question;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IQuestionService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.player.PlayerCreditService;
import com.polifono.service.impl.player.PlayerService;
import com.polifono.util.ContentUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameHelperService {

    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final PlayerService playerService;
    private final PlayerCreditService playerCreditService;
    private final IGameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IContentService contentService;
    private final IQuestionService questionService;
    private final IPlayerPhaseService playerPhaseService;
    private final IDiplomaService diplomaService;

    public Player getPlayerOrRedirectHome() {
        return getOrRedirectHome(playerService.findById(getUserId()));
    }

    public Game getGameByNamelinkOrRedirectHome(String gameName) {
        return getOrRedirectHome(gameService.findByNamelink(gameName));
    }

    public Map getMapByGameLevelAndOrderOrRedirectHome(Game game, int levelOrder, int mapOrder) {
        return getOrRedirectHome(mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder));
    }

    public Map getCurrentMapByCurrentPhaseAndPlayerPhaseOrRedirectHome(Phase currentPhase, PlayerPhase playerPhase) {
        return getOrRedirectHome(mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase));
    }

    public Phase getPhaseByMapAndOrderOrRedirectHome(Map map, int phaseOrder) {
        return getOrRedirectHome(phaseService.findByMapAndOrder(map.getId(), phaseOrder));
    }

    public Content getContentByPhaseOrRedirectHome(Phase phase) {
        return getOrRedirectHome(ContentUtil.formatContent(contentService.findByPhaseAndOrder(phase.getId(), 1).orElse(null)));
    }

    public Content getQuestionaryContentByPhaseOrRedirectHome(Phase phase) {
        return getOrRedirectHome(contentService.findByPhaseAndOrder(phase.getId(), 0));
    }

    public List<Question> getQuestionsByContentOrRedirectHome(Content content) {
        return getListOrRedirectHome(questionService.findByContent(content.getId()));
    }

    public List<Integer> getQuestionsIdFromSessionOrRedirectHome(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Integer> questionsId = (List<Integer>) session.getAttribute("questionsId");
        return getListOrRedirectHome(questionsId);
    }

    public void isPlayerAllowedToAccessPhaseOrRedirectHome(Phase phase) {
        isAllowedOrRedirectHome(phaseService.canPlayerAccessPhase(phase, getUserId()));
    }

    public void addRankingToModel(Model model) {
        model.addAttribute("ranking_monthly", playerPhaseService.getRankingMonthly());
    }

    public void addScoreToModel(Model model, int score) {
        model.addAttribute("score", score);
    }

    public void addGradeToModel(Model model, int grade) {
        model.addAttribute("grade", grade);
    }

    public void addDiplomaToModel(Model model, Diploma diploma) {
        model.addAttribute("diploma", diploma);
    }

    public void addCurrentPhaseToModel(Model model, Phase currentPhase) {
        model.addAttribute("phase", currentPhase);
    }

    public void addNextPhaseToModel(Model model, Map map, PlayerPhase playerPhase) {
        model.addAttribute("phase", setNextPhase(getPhasesCheckedByMapAndPlayerPhase(map, playerPhase)));
    }

    public void addQuestionsIdToSession(HttpSession session, List<Question> questions) {
        session.setAttribute("questionsId", getTestQuestionsId(questions));
    }

    public void addMsgCreditsInsufficientToModel(Model model, Locale locale) {
        model.addAttribute("msg", messagesResource.getMessage("msg.credits.insufficient", null, locale));
    }

    public void addGameAndLevelsToModel(Model model, Game game) {
        model.addAttribute("game", game);
        model.addAttribute("levels", flagLevelsToOpenedOrNot(game, getPermittedLevelForPlayer(game)));
    }

    public void addGameMapAndPhasesToModel(Model model, Game game, Map map) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phases", getPhasesCheckedByMap(map));
    }

    public void addGameMapPhaseAndContentToModel(Model model, Game game, Map map, Phase phase, Content content) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("content", content);
    }

    public void addGameMapPhaseAndQuestionsToModel(Model model, Game game, Map map, Phase phase, List<Question> questions) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("questions", questions);
    }

    /**
     * Return the nextPhase that the player has to do.
     * phases is a list of phases of a map with the flag opened equals true if the phase was already finished by the player.
     * Besides from the phases already finished, the next phase is also with the opened flag with the value true.
     */
    public Phase setNextPhase(List<Phase> phases) {
        Phase nextPhase = null;

        label:
        {
            for (Phase p : phases) {
                if (p.isOpened()) {
                    nextPhase = p;
                } else {
                    break label;
                }
            }
        }

        return nextPhase;
    }

    public String handleDisplayBuyCreditsPage(Model model, Locale locale) {
        addMsgCreditsInsufficientToModel(model, locale);
        return URL_BUY_CREDITS;
    }

    public String handleFailedPhase(Model model, Phase currentPhase, int grade) {
        addCurrentPhaseToModel(model, currentPhase);
        addGradeToModel(model, grade);
        return URL_GAMES_RESULT_TEST;
    }

    public String handlePassedPhase(Model model, Phase currentPhase, int grade) {
        Player player = getPlayerOrRedirectHome();
        PlayerPhase playerPhase = playerPhaseService.setupPlayerPhaseInProgress(currentPhase, grade);
        addScoreToModel(model, playerPhase.getScore());
        playerCreditService.updatePlayerAfterPassingPhase(player, playerPhase, currentPhase);
        playerPhaseService.save(playerPhase);

        Map map = getCurrentMapByCurrentPhaseAndPlayerPhaseOrRedirectHome(currentPhase, playerPhase);
        if (map.isLevelCompleted() || map.isGameCompleted()) {
            addDiplomaToModel(model, diplomaService.setupDiploma(player, currentPhase));
            return handleLevelOrGameCompletion(map);
        }

        addNextPhaseToModel(model, map, playerPhase);
        addGradeToModel(model, grade);
        return URL_GAMES_RESULT_TEST;
    }

    private String handleLevelOrGameCompletion(Map map) {
        playerCreditService.addCreditsToPlayerAfterLevelOrGameCompletion(map);

        if (map.isLevelCompleted()) {
            return URL_GAMES_END_OF_LEVEL;
        } else {
            return URL_GAMES_END_OF_GAME;
        }
    }

    private List<Integer> getTestQuestionsId(List<Question> questions) {
        List<Integer> questionsId = new ArrayList<>();

        for (Question q : questions) {
            questionsId.add(q.getId());
        }

        return questionsId;
    }

    private int getUserId() {
        return securityService.getUserId();
    }

    private <T> T getOrRedirectHome(Optional<T> optional) {
        return optional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private <T> List<T> getListOrRedirectHome(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return list;
    }

    private void isAllowedOrRedirectHome(boolean isAllowed) {
        if (!isAllowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private List<Level> flagLevelsToOpenedOrNot(Game game, int levelPermitted) {
        return levelService.flagLevelsToOpenedOrNot(game.getId(), levelPermitted);
    }

    private int getPermittedLevelForPlayer(Game game) {
        return playerPhaseService.getPermittedLevelForPlayer(getUserId(), game.getId());
    }

    /**
     * This method returns all the phases of a Map.
     * The phases are flagged as opened=true (player can open the phase) or opened=false (player can't open the phase).
     */
    private List<Phase> getPhasesCheckedByMap(Map map) {
        Optional<PlayerPhase> lastPhaseCompletedOpt = getLastPhaseCompleted(map.getGame());
        return getPhasesCheckedByMapAndPlayerPhase(map, lastPhaseCompletedOpt.orElse(null));
    }

    private Optional<PlayerPhase> getLastPhaseCompleted(Game game) {
        return playerPhaseService.findLastPhaseCompleted(getUserId(), game.getId());
    }

    private List<Phase> getPhasesCheckedByMapAndPlayerPhase(Map map, PlayerPhase playerPhase) {
        return phaseService.findPhasesCheckedByMap(map, playerPhase);
    }
}
