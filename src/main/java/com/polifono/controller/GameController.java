package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_GAMES;
import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_BUY_CREDITS;
import static com.polifono.common.TemplateConstants.URL_GAMES_END_OF_GAME;
import static com.polifono.common.TemplateConstants.URL_GAMES_END_OF_LEVEL;
import static com.polifono.common.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.TemplateConstants.URL_GAMES_LEVEL;
import static com.polifono.common.TemplateConstants.URL_GAMES_MAP;
import static com.polifono.common.TemplateConstants.URL_GAMES_PHASE_CONTENT;
import static com.polifono.common.TemplateConstants.URL_GAMES_PHASE_TEST;
import static com.polifono.common.TemplateConstants.URL_GAMES_RESULT_TEST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Content;
import com.polifono.domain.Diploma;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Question;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.dto.RankingDTO;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IQuestionService;
import com.polifono.service.helper.GameHelperService;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SecurityService;
import com.polifono.util.ContentUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final IPlayerService playerService;
    private final IGameService gameService;
    private final GameHelperService gameHelperService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IContentService contentService;
    private final IQuestionService questionService;
    private final IPlayerPhaseService playerPhaseService;
    private final IDiplomaService diplomaService;
    private final GenerateRandomStringService generateRandomStringService;

    @GetMapping("/games")
    public String listGames(final Model model) {
        model.addAttribute("ranking_monthly", getRankingMonthly());
        return URL_GAMES_INDEX;
    }

    @GetMapping("/games/{gameName}")
    public String listLevelsOfTheGame(final Model model, @PathVariable("gameName") @NotBlank String gameName) {
        Optional<Game> gameOpt = getGameByNamelink(gameName);

        if (gameOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        int levelPermitted = getPermittedLevelForPlayer(getUserId(), gameOpt.get());

        model.addAttribute("game", gameOpt.get());
        model.addAttribute("levels", flagLevelsToOpenedOrNot(gameOpt.get(), levelPermitted));

        return URL_GAMES_LEVEL;
    }

    @GetMapping("/games/{gameName}/{levelOrder}/{mapOrder}")
    public String listPhasesOfTheMap(
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") int levelOrder,
            @PathVariable("mapOrder") int mapOrder
    ) {

        Optional<Game> gameOpt = getGameByNamelink(gameName);

        if (gameOpt.isEmpty() || !gameHelperService.isValidLevelOrder(levelOrder)) {
            return REDIRECT_HOME;
        }

        Optional<Map> mapOpt = getMapByGameLevelAndOrder(gameOpt.get(), levelOrder, mapOrder);

        if (mapOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Looking for the phases of the map that were already completed by the player.
        List<Phase> phases = getPhasesCompletedByMap(mapOpt.get(), getUserId());

        // If there are no phases in the map.
        if (phases == null || !isPlayerAllowedToAccessTheMap(mapOpt.get(), getUserId())) {
            return REDIRECT_HOME;
        }

        model.addAttribute("game", gameOpt.get());
        model.addAttribute("map", mapOpt.get());
        model.addAttribute("phases", phases);

        return URL_GAMES_MAP;
    }

    /**
     * List all the phases of the current map of the player logged in.
     * If the player didn't play any phase of this game, the first phase of the first map of the game will be shown.
     * If the player has already played a phase of this game, it will be shown the right map with the next phase unlocked.
     */
    @GetMapping("/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}")
    public String initPhase(
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") int levelOrder,
            @PathVariable("mapOrder") int mapOrder,
            @PathVariable("phaseOrder") int phaseOrder,
            Locale locale
    ) {

        Optional<Game> gameOpt = getGameByNamelink(gameName);

        if (gameOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Map> mapOpt = getMapByGameLevelAndOrder(gameOpt.get(), levelOrder, mapOrder);

        if (mapOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Phase> phaseOpt = getPhaseByMapAndOrder(mapOpt.get(), phaseOrder);

        if (phaseOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        // If the player doesn't have permission to access this phase.
        if (!isPlayerAllowedToAccessThePhase(phaseOpt.get(), getUserId())) {
            return REDIRECT_HOME;
        }

        Optional<Content> contentOpt = getContent(phaseOpt.get());

        if (contentOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (!playerService.playerHasCredits(getUserId(), phaseOpt.get()) && phaseOpt.get().getOrder() > 1) {

            // Get the last phase that the player has done in a specific game.
            Optional<Phase> lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(getUserId(), phaseOpt.get().getMap().getGame().getId());

            if (lastPhaseDone.isEmpty()) {
                return REDIRECT_HOME;
            }

            // If the player is trying to access a phase that he has already finished, it's OK. Otherwise, he can't access this phase.
            if (lastPhaseDone.get().getOrder() < phaseOpt.get().getOrder()) {
                model.addAttribute("msg", messagesResource.getMessage("msg.credits.insufficient", null, locale));
                return URL_BUY_CREDITS;
            }
        }

        model.addAttribute("game", gameOpt.get());
        model.addAttribute("map", mapOpt.get());
        model.addAttribute("phase", phaseOpt.get());
        model.addAttribute("content", contentOpt.get());

        return URL_GAMES_PHASE_CONTENT;
    }

    private Optional<Content> getContent(Phase phase) {
        return ContentUtil.formatContent(contentService.findByPhaseAndOrder(phase.getId(), 1));
    }

    private boolean isPlayerAllowedToAccessThePhase(Phase phase, int playerId) {
        return phaseService.canPlayerAccessPhase(phase, playerId);
    }

    private Optional<Phase> getPhaseByMapAndOrder(Map map, int phaseOrder) {
        return phaseService.findByMapAndOrder(map.getId(), phaseOrder);
    }

    @GetMapping("/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test")
    public String initTest(
            HttpSession session,
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") Integer levelOrder,
            @PathVariable("mapOrder") Integer mapOrder,
            @PathVariable("phaseOrder") Integer phaseOrder,
            Locale locale
    ) {

        Optional<Game> game = getGameByNamelink(gameName);

        if (game.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Map> map = getMapByGameLevelAndOrder(game.get(), levelOrder, mapOrder);

        if (map.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Phase> phase = getPhaseByMapAndOrder(map.get(), phaseOrder);

        if (phase.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // If the player has already passed this test he can't see the test again.
        if (playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase.get(), getUserId())) {
            return REDIRECT_GAMES + "/" + gameName;
        }

        // If the player doesn't have permission to access this phase.
        if (!isPlayerAllowedToAccessThePhase(phase.get(), getUserId())) {
            return REDIRECT_HOME;
        }

        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (!playerService.playerHasCredits(getUserId(), phase.get()) && phase.get().getOrder() > 1) {

            // Get the last phase that the player has done in a specific game.
            Optional<Phase> lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(getUserId(), phase.get().getMap().getGame().getId());

            if (lastPhaseDone.isEmpty()) {
                return REDIRECT_HOME;
            }

            // If the player is trying to access a phase that he has already finished, it's OK. Otherwise, he can't access this phase.
            if (lastPhaseDone.get().getOrder() < phase.get().getOrder()) {
                model.addAttribute("msg", messagesResource.getMessage("msg.credits.insufficient", null, locale));
                return URL_BUY_CREDITS;
            }
        }

        // Adding a playerPhase at T007.
        playerPhaseService.setTestAttempt(currentUser.get().getUser(), phase.get());

        // Get the questionary of this phase.
        Content content = contentService.findByPhaseAndOrder(phase.get().getId(), 0);
        List<Question> questions = questionService.findByContent(content.getId());

        model.addAttribute("game", game.get());
        model.addAttribute("map", map.get());
        model.addAttribute("phase", phase.get());
        model.addAttribute("questions", questions);

        List<Integer> questionsId = new ArrayList<>();
        for (Question q : questions) {
            questionsId.add(q.getId());
        }

        session.setAttribute("questionsId", questionsId);

        return URL_GAMES_PHASE_TEST;
    }

    @PostMapping("/games/result")
    public String showResultTest(
            HttpSession session,
            final Model model,
            @RequestParam java.util.Map<String, String> playerAnswers // playerAnswers is all the parameters passed, not only the player's answers.
    ) {

        @SuppressWarnings("unchecked")
        List<Integer> questionsId = (List<Integer>) session.getAttribute("questionsId");

        if (questionsId == null || questionsId.isEmpty())
            return REDIRECT_HOME;

        int grade = gameService.calculateGrade(questionsId, playerAnswers);

        Phase currentPhase = gameService.getPhaseOfTheTest(questionsId);

        model.addAttribute("grade", grade);

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        if (grade >= 70) {
            PlayerPhase playerPhase = playerPhaseService.findByPlayerPhaseAndStatus(currentUser.get().getUser().getId(), currentPhase.getId(), 2);

            playerPhase.setGrade(grade);
            Phasestatus phasestatus = new Phasestatus();
            phasestatus.setId(3);
            playerPhase.setPhasestatus(phasestatus);
            playerPhase.setDtTest(new Date());
            playerPhase.setScore(gameService.calculateScore(playerPhase.getNumAttempts(), grade));

            model.addAttribute("score", playerPhase.getScore());

            Optional<Player> player = playerService.findById(currentUser.get().getUser().getId());

            // If the player doesn't exist.
            if (player.isEmpty())
                return REDIRECT_HOME;

            player.get().setScore(this.calculatePlayerScoreAfterPassTheTest(player.get().getScore(), playerPhase.getScore()));
            player.get().setCoin(this.calculatePlayerCoinAfterPassTheTest(player.get().getCoin(), playerPhase.getScore()));

            player = Optional.ofNullable(playerService.removeOneCreditFromPlayer(player.get(), currentPhase.getMap().getGame()));

            // If the player doesn't exist.
            if (player.isEmpty())
                return REDIRECT_HOME;

            // Changing the status of this phase.
            // Now the phase is completed and the player can play the next phase.
            playerPhaseService.save(playerPhase);

            // Update session user.
            securityService.updateCurrentAuthenticatedUser(player.get());

            // Checking what is the map of the next phase.
            Optional<Map> map = mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase);

            if (map.isEmpty()) {
                return REDIRECT_HOME;
            }

            // The attribute levelCompleted will be true if the player has just finished the last phase of the last map of the level.
            if (map.get().isLevelCompleted()) {
                // When the player finishes the last phase of the level, he gets a diploma.
                Diploma diploma = setDiploma(player.get(), currentPhase.getMap().getGame(), currentPhase.getMap().getLevel());
                diplomaService.save(diploma);
                model.addAttribute("diploma", diploma);

                // When the player finishes the last phase of the level, he gains n credits.
                securityService.updateCurrentAuthenticatedUser(
                        playerService.addCreditsToPlayer(currentUser.get().getUser().getId(), configsCreditsProperties.getLevelCompleted()));

                return URL_GAMES_END_OF_LEVEL;
            }

            // The attribute gameCompleted will be true if the player has just finished the last phase of the last map of the last level of the game.
            if (map.get().isGameCompleted()) {
                // When the player finishes the last phase of the last level of the game, he gets a diploma.
                Diploma diploma = setDiploma(player.get(), currentPhase.getMap().getGame(), currentPhase.getMap().getLevel());
                diplomaService.save(diploma);
                model.addAttribute("diploma", diploma);

                // When the player finishes the last phase of the last level of the game, he gains n credits.
                securityService.updateCurrentAuthenticatedUser(
                        playerService.addCreditsToPlayer(currentUser.get().getUser().getId(), configsCreditsProperties.getGameCompleted()));

                return URL_GAMES_END_OF_GAME;
            }

            // Looking for the phases of the map.
            List<Phase> phases = phaseService.findPhasesCheckedByMap(map.get(), playerPhase);

            Phase nextPhase = setNextPhase(phases);

            model.addAttribute("phase", nextPhase);
        } else {
            model.addAttribute("phase", currentPhase);
        }

        return URL_GAMES_RESULT_TEST;
    }

    private int calculatePlayerScoreAfterPassTheTest(int playerScore, int testScore) {
        return playerScore + testScore;
    }

    private int calculatePlayerCoinAfterPassTheTest(int playerCoin, int testScore) {
        return playerCoin + testScore;
    }

    /**
     * Return the nextPhase that the player has to do.
     * phases is a list of phases of a map with the flag opened equals true if the phase was already done by the player.
     * Besides from the phases already done, the next phase is also with the opened flag with the value true.
     */
    private Phase setNextPhase(List<Phase> phases) {
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

    /**
     * Return a diploma to be saved.
     */
    private Diploma setDiploma(Player player, Game game, Level level) {
        Diploma diploma = new Diploma();

        diploma.setPlayer(player);
        diploma.setGame(game);
        diploma.setLevel(level);

        diploma.setDt(new Date());
        diploma.setCode(player.getId() + "-" + game.getId() + "-" + level.getId() + "-" + generateRandomStringService.generate(10));

        return diploma;
    }

    private List<Level> flagLevelsToOpenedOrNot(Game game, int levelPermitted) {
        return levelService.flagLevelsToOpenedOrNot(game.getId(), levelPermitted);
    }

    private List<RankingDTO> getRankingMonthly() {
        return playerPhaseService.getRankingMonthly();
    }

    private int getPermittedLevelForPlayer(int playerId, Game game) {
        return playerPhaseService.getPermittedLevelForPlayer(playerId, game.getId());
    }

    private int getUserId() {
        return securityService.getUserId();
    }

    private Optional<Game> getGameByNamelink(String gameName) {
        return gameService.findByNamelink(gameName);
    }

    private boolean isPlayerAllowedToAccessTheMap(Map map, int playerId) {
        return mapService.canPlayerAccessMap(map, playerId);
    }

    private List<Phase> getPhasesCompletedByMap(Map map, int playerId) {
        Optional<PlayerPhase> lastPhaseCompletedOpt = getLastPhaseCompleted(playerId, map.getGame());

        return phaseService.findPhasesCheckedByMap(map, lastPhaseCompletedOpt.orElse(null));
    }

    private Optional<PlayerPhase> getLastPhaseCompleted(int playerId, Game game) {
        return playerPhaseService.findLastPhaseCompleted(playerId, game.getId());
    }

    private Optional<Map> getMapByGameLevelAndOrder(Game game, int levelOrder, int mapOrder) {
        return mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);
    }
}
