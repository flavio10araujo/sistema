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
        addRankingToModel(model);
        return URL_GAMES_INDEX;
    }

    @GetMapping("/games/{gameName}")
    public String listLevelsOfTheGame(final Model model, @PathVariable("gameName") @NotBlank String gameName) {
        Optional<Game> gameOpt = getGameByNamelink(gameName);
        if (gameOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        addGameAndLevelsToModel(model, gameOpt.get(), getPermittedLevelForPlayer(getUserId(), gameOpt.get()));
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
        if (mapOpt.isEmpty() || !isPlayerAllowedToAccessTheMap(mapOpt.get(), getUserId())) {
            return REDIRECT_HOME;
        }

        addGameMapAndPhasesToModel(model, gameOpt.get(), mapOpt.get());
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
        if (phaseOpt.isEmpty() || isPlayerNotAllowedToAccessThePhase(getUserId(), phaseOpt.get())) {
            return REDIRECT_HOME;
        }

        Optional<Content> contentOpt = getContent(phaseOpt.get());
        if (contentOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        if (shouldDisplayBuyCreditsPage(getUserId(), gameOpt.get(), phaseOpt.get())) {
            addMsgCreditsInsufficientToModel(model, locale);
            return URL_BUY_CREDITS;
        }

        addGameMapPhaseAndContentToModel(model, gameOpt.get(), mapOpt.get(), phaseOpt.get(), contentOpt.get());
        return URL_GAMES_PHASE_CONTENT;
    }

    @GetMapping("/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test")
    public String initTest(
            HttpSession session,
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

        Optional<CurrentUser> currentUser = getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        if (isPhaseAlreadyCompletedByPlayer(getUserId(), phaseOpt.get())) {
            return REDIRECT_GAMES + "/" + gameName;
        }

        if (isPlayerNotAllowedToAccessThePhase(getUserId(), phaseOpt.get())) {
            return REDIRECT_HOME;
        }

        if (shouldDisplayBuyCreditsPage(getUserId(), gameOpt.get(), phaseOpt.get())) {
            addMsgCreditsInsufficientToModel(model, locale);
            return URL_BUY_CREDITS;
        }

        registerTestAttempt(currentUser.get(), phaseOpt.get());

        // Get the questionary of this phase.
        Optional<Content> contentOpt = getFirstContentByPhase(phaseOpt.get());
        if (contentOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        List<Question> questions = getQuestionsByContent(contentOpt.get());
        if (questions.isEmpty()) {
            return REDIRECT_HOME;
        }

        addQuestionsIdToSession(session, questions);
        addGameMapPhaseAndQuestionsToModel(model, gameOpt.get(), mapOpt.get(), phaseOpt.get(), questions);
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

        if (questionsId == null || questionsId.isEmpty()) {
            return REDIRECT_HOME;
        }

        int grade = gameService.calculateGrade(questionsId, playerAnswers);

        Phase currentPhase = gameService.getPhaseOfTheTest(questionsId);

        model.addAttribute("grade", grade);

        Optional<CurrentUser> currentUser = getCurrentAuthenticatedUser();

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

    private Optional<CurrentUser> getCurrentAuthenticatedUser() {
        return securityService.getCurrentAuthenticatedUser();
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

    /**
     * This method returns all the phases of a Map.
     * The phases are flagged as opened=true (player can open the phase) or opened=false (player can't open the phase).
     */
    private List<Phase> getPhasesCheckedByMap(Map map, int playerId) {
        Optional<PlayerPhase> lastPhaseCompletedOpt = getLastPhaseCompleted(playerId, map.getGame());
        return phaseService.findPhasesCheckedByMap(map, lastPhaseCompletedOpt.orElse(null));
    }

    private Optional<PlayerPhase> getLastPhaseCompleted(int playerId, Game game) {
        return playerPhaseService.findLastPhaseCompleted(playerId, game.getId());
    }

    private Optional<Map> getMapByGameLevelAndOrder(Game game, int levelOrder, int mapOrder) {
        return mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);
    }

    private static boolean isTryingToAccessPhaseNeverPlayed(Phase lastPhaseDone, Phase phase) {
        return lastPhaseDone.getOrder() < phase.getOrder();
    }

    private Optional<Phase> getLastPhaseDoneByPlayerAndGame(int playerId, Game game) {
        return phaseService.findLastPhaseDoneByPlayerAndGame(playerId, game.getId());
    }

    private boolean isTryingToAccessNotFreePhaseWithoutCredits(int playerId, Phase phase) {
        return !playerService.playerHasCredits(playerId, phase) && phase.getOrder() > 1;
    }

    private Optional<Content> getContent(Phase phase) {
        return ContentUtil.formatContent(contentService.findByPhaseAndOrder(phase.getId(), 1).orElse(null));
    }

    private boolean isPlayerNotAllowedToAccessThePhase(int playerId, Phase phase) {
        return !phaseService.canPlayerAccessPhase(phase, playerId);
    }

    private Optional<Phase> getPhaseByMapAndOrder(Map map, int phaseOrder) {
        return phaseService.findByMapAndOrder(map.getId(), phaseOrder);
    }

    private List<Question> getQuestionsByContent(Content content) {
        return questionService.findByContent(content.getId());
    }

    private Optional<Content> getFirstContentByPhase(Phase phase) {
        return contentService.findByPhaseAndOrder(phase.getId(), 0);
    }

    private void registerTestAttempt(CurrentUser currentUser, Phase phase) {
        playerPhaseService.setTestAttempt(currentUser.getUser(), phase);
    }

    private void addQuestionsIdToSession(HttpSession session, List<Question> questions) {
        session.setAttribute("questionsId", getTestQuestionsId(questions));
    }

    private void addRankingToModel(Model model) {
        model.addAttribute("ranking_monthly", getRankingMonthly());
    }

    private void addMsgCreditsInsufficientToModel(Model model, Locale locale) {
        model.addAttribute("msg", messagesResource.getMessage("msg.credits.insufficient", null, locale));
    }

    private void addGameAndLevelsToModel(Model model, Game game, int levelPermitted) {
        model.addAttribute("game", game);
        model.addAttribute("levels", flagLevelsToOpenedOrNot(game, levelPermitted));
    }

    private void addGameMapAndPhasesToModel(Model model, Game game, Map map) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phases", getPhasesCheckedByMap(map, getUserId()));
    }

    private void addGameMapPhaseAndContentToModel(Model model, Game game, Map map, Phase phase, Content content) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("content", content);
    }

    private void addGameMapPhaseAndQuestionsToModel(Model model, Game game, Map map, Phase phase, List<Question> questions) {
        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("questions", questions);
    }

    private boolean shouldDisplayBuyCreditsPage(int playerId, Game game, Phase phase) {
        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (isTryingToAccessNotFreePhaseWithoutCredits(playerId, phase)) {
            // Get the last phase that the player has finished in a specific game.
            Optional<Phase> lastPhaseDoneOpt = getLastPhaseDoneByPlayerAndGame(playerId, game);

            return lastPhaseDoneOpt.isEmpty() || isTryingToAccessPhaseNeverPlayed(lastPhaseDoneOpt.get(), phase);
        }

        return false;
    }

    private boolean isPhaseAlreadyCompletedByPlayer(int playerId, Phase phase) {
        return playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, playerId);
    }

    private List<Integer> getTestQuestionsId(List<Question> questions) {
        List<Integer> questionsId = new ArrayList<>();

        for (Question q : questions) {
            questionsId.add(q.getId());
        }

        return questionsId;
    }
}
