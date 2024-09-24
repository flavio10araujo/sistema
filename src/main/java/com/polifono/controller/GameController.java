package com.polifono.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IQuestionService;
import com.polifono.util.ContentUtil;
import com.polifono.util.RandomStringUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GameController extends BaseController {

    private final IPlayerService playerService;
    private final IGameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IContentService contentService;
    private final IQuestionService questionService;
    private final IPlayerPhaseService playerPhaseService;
    private final IDiplomaService diplomaService;

    public static final String URL_GAMES_INDEX = "games/index";
    public static final String URL_GAMES_LEVEL = "games/level";
    public static final String URL_GAMES_MAP = "games/map";
    public static final String URL_GAMES_PHASECONTENT = "games/phaseContent";
    public static final String URL_GAMES_PHASETEST = "games/phaseTest";
    public static final String URL_GAMES_RESULTTEST = "games/resultTest";
    public static final String URL_GAMES_ENDOFLEVEL = "games/endoflevel";
    public static final String URL_GAMES_ENDOFGAME = "games/endofgame";
    public static final String URL_BUYCREDITS = "buycredits";

    public static final String REDIRECT_HOME = "redirect:/";
    public static final String REDIRECT_GAMES = "redirect:/games";

    /**
     * Show the index page of the games.
     * List all the games.
     */
    @RequestMapping(value = { "/games" }, method = RequestMethod.GET)
    public final String listGames(final Model model) {
        //model.addAttribute("games", gameService.findAll());
        model.addAttribute("ranking_monthly", playerPhaseService.getRankingMonthly());
        return URL_GAMES_INDEX;
    }

    /**
     * List all the levels of a game and flag which ones are opened or closed for the player logged in.
     */
    @RequestMapping(value = { "/games/{gameName}" }, method = RequestMethod.GET)
    public final String listLevelsOfTheGame(final Model model, @PathVariable("gameName") String gameName) {
        Game game = gameService.findByNamelink(gameName);

        // If the game doesn't exist.
        if (game == null)
            return REDIRECT_HOME;

        int levelPermitted = 0;

        // Checking what is the last phase completed by this player in this game.
        PlayerPhase lastPlayerPhaseCompleted = playerPhaseService.findLastPhaseCompleted(this.currentAuthenticatedUser().getUser().getId(), game.getId());

        // If the player has never played any phase of this game.
        if (lastPlayerPhaseCompleted == null) {
            levelPermitted = 1;
        } else {
            // Checking in which level is the lastPhaseCompleted.
            Level lastLevel = lastPlayerPhaseCompleted.getPhase().getMap().getLevel();

            // Checking if the lastPhaseCompleted is the last phase of this level.
            Phase lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(game.getId(), lastLevel.getId());

            // If no, only this level and the level before this one can the accessed.
            if (lastPlayerPhaseCompleted.getPhase().getId() != lastPhaseOfTheLevel.getId()) {
                levelPermitted = lastLevel.getOrder();
            }
            // If yes, the player receive the permission to see the next level.
            else {
                levelPermitted = lastLevel.getOrder() + 1;
            }
        }

        //System.out.println("levelPermitted = " + levelPermitted);

        List<Level> levels = levelService.flagLevelsToOpenedOrNot(game.getId(), levelPermitted);

        model.addAttribute("game", game);
        model.addAttribute("levels", levels);

        return URL_GAMES_LEVEL;
    }

    @RequestMapping(value = { "/games/{gameName}/{levelOrder}/{mapOrder}" }, method = RequestMethod.GET)
    public final String listPhasesOfTheMap(
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") Integer levelOrder,
            @PathVariable("mapOrder") Integer mapOrder
    ) {

        Game game = gameService.findByNamelink(gameName);

        // If the game doesn't exist.
        if (game == null)
            return REDIRECT_HOME;

        // If the level doesn't exist.
        if (levelOrder <= 0 || levelOrder > 5)
            return REDIRECT_HOME;

        Map map = mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);

        // If the map doesn't exist.
        if (map == null)
            return REDIRECT_HOME;

        // Checking what is the last phase completed by this player in this game.
        PlayerPhase lastPhaseCompleted = playerPhaseService.findLastPhaseCompleted(this.currentAuthenticatedUser().getUser().getId(), game.getId());

        // Looking for the phases of the map.
        List<Phase> phases = phaseService.findPhasesCheckedByMap(map, lastPhaseCompleted);

        // If there are not phases in the map.
        if (phases == null)
            return REDIRECT_HOME;

        if (!mapService.playerCanAccessThisMap(map, this.currentAuthenticatedUser().getUser()))
            return REDIRECT_HOME;

        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phases", phases);

        return URL_GAMES_MAP;
    }

    /**
     * List all the phases of the current map of the player logged in.
     * If the player didn't play any phase of this game, the first phase of the first map of the game will be showed.
     * If the player has already played a phase of this game, it will be showed the right map with the next phase unlocked.
     * <p>
     * POSSO USAR ESSE MÉTODO PARA CRIAR UM ATALHO NA TELA PRINCIPAL E DIRECIONAR O ALUNO DIRETO PRO MAPA QUE ELE DEVE IR.
     * TAMBÉM POSSO FAZER ALGO PARECIDO NA TELA INICIAL PARA DIRECIONAR O ALUNO DIRETO PRA AULA QUE ELE DEVE IR.
     */
	/*@RequestMapping(value = {"/gamesOLD/{gameName}"}, method = RequestMethod.GET)
	public final String gamePhases(final Model model, @PathVariable("gameName") String gameName) {
		Game game = gameService.findByNamelink(gameName);

		// If the game doesn't exist.
		if (game == null) {
			return REDIRECT_HOME;
		}

		// Checking what is the last phase completed by this player in this game.
		PlayerPhase lastPhaseCompleted = playerPhaseService.findLastPhaseCompleted(this.currentAuthenticatedUser().getUser(), game);
		// Checking what is the current map.
		Map map = findCurrentMap(game, lastPhaseCompleted);

		// If there is not any map for this game.
		if (map == null) {
			return REDIRECT_HOME;
		}

		// Looking for the phases of the map.
		List<Phase> phases = findPhasesByMap(map, lastPhaseCompleted);

		// If there are not phases in the map.
		if (phases == null) {
			return REDIRECT_HOME;
		}

		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phases", phases);

		return "games/map";
	}*/
    @RequestMapping(value = { "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}" }, method = RequestMethod.GET)
    public final String initPhase(
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") Integer levelOrder,
            @PathVariable("mapOrder") Integer mapOrder,
            @PathVariable("phaseOrder") Integer phaseOrder
    ) {

        Game game = gameService.findByNamelink(gameName);

        // If the game doesn't exist.
        if (game == null)
            return REDIRECT_HOME;

        Map map = mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);

        // If the map doesn't exist.
        if (map == null)
            return REDIRECT_HOME;

        Phase phase = phaseService.findByMapAndOrder(map.getId(), phaseOrder);

        // If the phase doesn't exist.
        if (phase == null)
            return REDIRECT_HOME;

        // If the player doesn't have permission to access this phase.
        if (!phaseService.playerCanAccessThisPhase(phase, this.currentAuthenticatedUser().getUser()))
            return REDIRECT_HOME;

        // Get the first content of this phase.
        Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(phase.getId(), 1));

        // If the content doesn't exist.
        if (content == null)
            return REDIRECT_HOME;

        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (!playerService.playerHasCredits(this.currentAuthenticatedUser().getUser(), phase) && phase.getOrder() > 1) {

            // Get the last phase that the player has done in a specific game.
            Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(this.currentAuthenticatedUser().getUser().getId(),
                    phase.getMap().getGame().getId());

            // If the player is trying to access a phase that he has already finished, it's OK. Otherwise, he can't access this phase.
            if (lastPhaseDone.getOrder() < phase.getOrder()) {
                model.addAttribute("msg", messagesResourceBundle.getString("msg.credits.insufficient"));
                return URL_BUYCREDITS;
            }
        }

        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("content", content);

        return URL_GAMES_PHASECONTENT;
    }

    @RequestMapping(value = { "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test" }, method = RequestMethod.GET)
    public final String initTest(
            HttpSession session,
            final Model model,
            @PathVariable("gameName") String gameName,
            @PathVariable("levelOrder") Integer levelOrder,
            @PathVariable("mapOrder") Integer mapOrder,
            @PathVariable("phaseOrder") Integer phaseOrder
    ) {

        Game game = gameService.findByNamelink(gameName);

        // If the game doesn't exist.
        if (game == null)
            return REDIRECT_HOME;

        Map map = mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);

        // If the map doesn't exist.
        if (map == null)
            return REDIRECT_HOME;

        Phase phase = phaseService.findByMapAndOrder(map.getId(), phaseOrder);

        // If the phase doesn't exist.
        if (phase == null)
            return REDIRECT_HOME;

        // If the player has already passed this test he can't see the test again.
        if (playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, this.currentAuthenticatedUser().getUser()))
            return REDIRECT_GAMES + "/" + gameName;

        // If the player doesn't have permission to access this phase.
        if (!phaseService.playerCanAccessThisPhase(phase, this.currentAuthenticatedUser().getUser()))
            return REDIRECT_HOME;

        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (!playerService.playerHasCredits(this.currentAuthenticatedUser().getUser(), phase) && phase.getOrder() > 1) {

            // Get the last phase that the player has done in a specific game.
            Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(this.currentAuthenticatedUser().getUser().getId(),
                    phase.getMap().getGame().getId());

            // If the player is trying to access a phase that he has already finished, it's OK. Otherwise, he can't access this phase.
            if (lastPhaseDone.getOrder() < phase.getOrder()) {
                model.addAttribute("msg", messagesResourceBundle.getString("msg.credits.insufficient"));
                return URL_BUYCREDITS;
            }
        }

        // Adding a playerPhase at T007.
        playerPhaseService.setTestAttempt(this.currentAuthenticatedUser().getUser(), phase);

        // Get the questionary of this phase.
        Content content = contentService.findByPhaseAndOrder(phase.getId(), 0);
        List<Question> questions = questionService.findByContent(content.getId());

        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("questions", questions);

        List<Integer> questionsId = new ArrayList<>();
        for (Question q : questions) {
            questionsId.add(q.getId());
        }

        session.setAttribute("questionsId", questionsId);

        return URL_GAMES_PHASETEST;
    }

    @RequestMapping(value = { "/games/result" }, method = RequestMethod.POST)
    public final String showResultTest(
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

        if (grade >= 70) {
            PlayerPhase playerPhase = playerPhaseService.findByPlayerPhaseAndStatus(this.currentAuthenticatedUser().getUser().getId(), currentPhase.getId(), 2);

            playerPhase.setGrade(grade);
            Phasestatus phasestatus = new Phasestatus();
            phasestatus.setId(3);
            playerPhase.setPhasestatus(phasestatus);
            playerPhase.setDtTest(new Date());
            playerPhase.setScore(gameService.calculateScore(playerPhase.getNumAttempts(), grade));

            model.addAttribute("score", playerPhase.getScore());

            Player player = playerService.findById(this.currentAuthenticatedUser().getUser().getId()).get();
            player.setScore(this.calculatePlayerScoreAfterPassTheTest(player.getScore(), playerPhase.getScore()));
            player.setCoin(this.calculatePlayerCoinAfterPassTheTest(player.getCoin(), playerPhase.getScore()));

            player = playerService.removeOneCreditFromPlayer(player, currentPhase.getMap().getGame());

            // Changing the status of this phase.
            // Now the phase is completed and the player can play the next phase.
            playerPhaseService.save(playerPhase);

            // Update session user.
            this.updateCurrentAuthenticateUser(player);

            // Checking what is the map of the next phase.
            Map map = mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase);

            // The attribute levelCompleted will be true if the player has just finished the last phase of the last map of the level.
            if (map.isLevelCompleted()) {

                // When the player finishes the last phase of the level, he gets a diploma.
                Diploma diploma = setDiploma(player, currentPhase.getMap().getGame(), currentPhase.getMap().getLevel());
                diplomaService.save(diploma);
                model.addAttribute("diploma", diploma);

                // When the player finishes the last phase of the level, he gains n credits.
                this.updateCurrentAuthenticateUser(playerService.addCreditsToPlayer(this.currentAuthenticatedUser().getUser().getId(), ConfigsCreditsProperties.getLevelCompleted()));

                return URL_GAMES_ENDOFLEVEL;
            }

            // The attribute gameCompleted will be true if the player has just finished the last phase of the last map of the last level of the game.
            if (map.isGameCompleted()) {
                // When the player finishes the last phase of the last level of the game, he gets a diploma.
                Diploma diploma = setDiploma(player, currentPhase.getMap().getGame(), currentPhase.getMap().getLevel());
                diplomaService.save(diploma);
                model.addAttribute("diploma", diploma);

                // When the player finishes the last phase of the last level of the game, he gains n credits.
                this.updateCurrentAuthenticateUser(playerService.addCreditsToPlayer(this.currentAuthenticatedUser().getUser().getId(), ConfigsCreditsProperties.getGameCompleted()));

                return URL_GAMES_ENDOFGAME;
            }

            // Looking for the phases of the map.
            List<Phase> phases = phaseService.findPhasesCheckedByMap(map, playerPhase);

            Phase nextPhase = setNextPhase(phases);

            model.addAttribute("phase", nextPhase);
        } else {
            model.addAttribute("phase", currentPhase);
        }

        return URL_GAMES_RESULTTEST;
    }

    public final int calculatePlayerScoreAfterPassTheTest(int playerScore, int testScore) {
        return playerScore + testScore;
    }

    public final int calculatePlayerCoinAfterPassTheTest(int playerCoin, int testScore) {
        return playerCoin + testScore;
    }

    /**
     * Return the nextPhase that the player has to do.
     * phases is a list of phases of a map with the flag opened equals true if the phase was already done by the player.
     * Besides from the phases already done, the next phase is also with the opened flag with the value true.
     */
    public final Phase setNextPhase(List<Phase> phases) {
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
    public final Diploma setDiploma(Player player, Game game, Level level) {
        Diploma diploma = new Diploma();

        diploma.setPlayer(player);
        diploma.setGame(game);
        diploma.setLevel(level);

        diploma.setDt(new Date());
        diploma.setCode(player.getId() + "-" + game.getId() + "-" + level.getId() + "-" + new RandomStringUtil(10).nextString());

        return diploma;
    }
}
