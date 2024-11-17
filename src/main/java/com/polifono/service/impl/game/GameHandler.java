package com.polifono.service.impl.game;

import static com.polifono.common.constant.TemplateConstants.URL_BUY_CREDITS;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_END_OF_GAME;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_END_OF_LEVEL;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_RESULT_TEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.polifono.common.util.ContentUtil;
import com.polifono.model.entity.Content;
import com.polifono.model.entity.Diploma;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.model.entity.Question;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IQuestionService;
import com.polifono.service.impl.ContentService;
import com.polifono.service.impl.LevelService;
import com.polifono.service.impl.MapService;
import com.polifono.service.impl.PhaseService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.diploma.DiplomaService;
import com.polifono.service.impl.player.PlayerCreditService;
import com.polifono.service.impl.player.PlayerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameHandler {

    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final PlayerService playerService;
    private final PlayerCreditService playerCreditService;
    private final GameService gameService;
    private final LevelService levelService;
    private final MapService mapService;
    private final PhaseService phaseService;
    private final ContentService contentService;
    private final IQuestionService questionService;
    private final IPlayerPhaseService playerPhaseService;
    private final DiplomaService diplomaService;

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
        addPhaseToModel(model, currentPhase);
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

        addPhaseToModel(model, setNextPhase(getPhasesCheckedByMapAndPlayerPhase(map, playerPhase)));
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

    private void addScoreToModel(Model model, int score) {
        model.addAttribute("score", score);
    }

    private void addGradeToModel(Model model, int grade) {
        model.addAttribute("grade", grade);
    }

    private void addDiplomaToModel(Model model, Diploma diploma) {
        model.addAttribute("diploma", diploma);
    }

    private void addPhaseToModel(Model model, Phase phase) {
        model.addAttribute("phase", phase);
    }
}
