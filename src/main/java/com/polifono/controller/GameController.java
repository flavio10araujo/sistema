package com.polifono.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Answer;
import com.polifono.domain.Content;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Question;
import com.polifono.service.ContentService;
import com.polifono.service.GameService;
import com.polifono.service.LevelService;
import com.polifono.service.MapService;
import com.polifono.service.PhaseService;
import com.polifono.service.PlayerPhaseService;
import com.polifono.service.PlayerService;
import com.polifono.service.QuestionService;

@Controller
public class GameController extends BaseController {

	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private LevelService levelService;
	
	@Autowired
	private MapService mapService;
	
	@Autowired
	private PhaseService phaseService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private PlayerPhaseService playerPhaseService;

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public final String index(final Model model) {
		
		// If the player is not logged.
		if (currentAuthenticatedUser() == null) {
			model.addAttribute("player", new Player());
			return "index";
		}
		else {
			return "redirect:/games";
		}
	}

	/**
	 * Show the index page of the games.
	 * List all the games.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/games"}, method = RequestMethod.GET)
	public final String listGames(final Model model) {
		List<Game> games = gameService.findAll();
		model.addAttribute("games", games);
		return "games/index";
	}

	/**
	 * List all the levels of a game and flag which ones are opened or closed for the player logged in.
	 * 
	 * @param model
	 * @param gameName
	 * @return
	 */
	@RequestMapping(value = {"/games/{gameName}"}, method = RequestMethod.GET)
	public final String listLevelsOfTheGame(final Model model, @PathVariable("gameName") String gameName) {
		Game game = gameService.findByNamelink(gameName);

		// If the game doesn't exist.
		if (game == null) {
			return "redirect:/";
		}
		
		// Checking if this game has at least one phase.
		Phase phaseAux = phaseService.findLastPhaseOfTheLevel(game.getId(), 1);
		
		if (phaseAux == null) {
			return "redirect:/";
		}
		
		int levelPermitted = 0;
		
		// Checking what is the last phase completed by this player in this game.
		PlayerPhase lastPlayerPhaseCompleted = playerPhaseService.findLastPhaseCompleted(currentAuthenticatedUser().getUser(), game);
		
		// If the player has never played any phase of this game.
		if (lastPlayerPhaseCompleted == null) {
			levelPermitted = 1;
		}
		else {
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
		
		List<Level> levelsAux = levelService.findByGame(game.getId());
		List<Level> levels = new ArrayList<Level>();
		
		for (Level level : levelsAux) {
			if (level.getOrder() <= levelPermitted) {
				level.setOpened(true);
			}
			levels.add(level);
		}
		
		model.addAttribute("game", game);
		model.addAttribute("levels", levels);

		return "games/level";
	}
	
	@RequestMapping(value = {"/games/{gameName}/{levelOrder}/{mapOrder}"}, method = RequestMethod.GET)
	public final String listPhasesOfTheMap(
				final Model model, 
				@PathVariable("gameName") String gameName, 
				@PathVariable("levelOrder") String levelOrderStr,
				@PathVariable("mapOrder") String mapOrderStr) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(levelOrderStr);
		intParameters.add(mapOrderStr);
		
		if (!isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		int levelOrder = Integer.parseInt(levelOrderStr);
		int mapOrder = Integer.parseInt(mapOrderStr);
		
		Game game = gameService.findByNamelink(gameName);
		
		// If the game doesn't exist.
		if (game == null) {
			return "redirect:/";
		}
		
		// If the level doesn't exist.
		if (Integer.parseInt(levelOrderStr) <= 0 || Integer.parseInt(levelOrderStr) > 5) {
			return "redirect:/";
		}
		
		Map map = mapService.findMapsByGameLevelAndOrder(game, levelOrder, mapOrder);
		
		// If the map doesn't exist.
		if (map == null) {
			return "redirect:/";
		}
		
		// Checking what is the last phase completed by this player in this game.
		PlayerPhase lastPhaseCompleted = playerPhaseService.findLastPhaseCompleted(currentAuthenticatedUser().getUser(), game);
		
		// Looking for the phases of the map.
		List<Phase> phases = findPhasesByMap(map, lastPhaseCompleted);
		
		// If there are not phases in the map.
		if (phases == null) {
			return "redirect:/";
		}
		
		if (!playerCanAccessThisMap(map)) {
			return "redirect:/";
		}

		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phases", phases);

		return "games/map";
	}
	
	/**
	 * Verify if the player has permission to access a specific map.
	 * Return true if the player has the permission.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerCanAccessThisMap(Map map) {

		// The first map of the first level is always permitted.
		if (map.getLevel().getOrder() == 1 && map.getOrder() == 1) {
			return true;
		}
		
		// Get the last phase that the player has done in a specific game.
		Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(currentAuthenticatedUser().getUser(), map.getGame());
		
		// If the player is trying to access a map different of the first map of the first level and he never had finished a phase of this game.
		if (lastPhaseDone == null && (map.getLevel().getOrder() != 1 || map.getOrder() != 1)) {
			return false;
		}
		
		// If the player is trying to access a map in a previous level than the lastPhaseDone's level. 
		if (map.getLevel().getOrder() < lastPhaseDone.getMap().getLevel().getOrder()) {
			return true;
		}
		
		// If the player is trying to access a previous map (or the same map) at the same level of the lastPhaseDone.
		if (
			map.getLevel().getOrder() == lastPhaseDone.getMap().getLevel().getOrder()
			&& map.getOrder() <= lastPhaseDone.getMap().getOrder()
			) {
			return true;
		}
		
		// If you are here, it's because the player is trying to access a next map at the same level OR a map in one of the next levels.
		
		// Get the last phase of the map of the lastPhaseDone. 
		Phase lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId());
		
		// If the lastPhaseDone is not the lastPhaseOfTheLevel. Then the player can't access the next map or the next level.
		if (lastPhaseDone.getId() != lastPhaseOfTheLevel.getId()) {
			return false;
		}
		
		// If the player is trying to access a map in the same level.
		if (lastPhaseDone.getMap().getLevel().getOrder() == map.getLevel().getOrder()) {
			// If it is the next map.
			if (lastPhaseDone.getMap().getOrder() == (map.getOrder() + 1)) {
				return true;
			}
			else {
				return false;
			}
		}
		
		// If the player is trying to access a map in the next level.
		if ((lastPhaseDone.getMap().getLevel().getOrder() + 1) == map.getLevel().getOrder()) {
			// If it is the first map.
			if (lastPhaseDone.getMap().getOrder() == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * List all the phases of the current map of the player logged in. 
	 * If the player didn't play any phase of this game, the first phase of the first map of the game will be showed.
	 * If the player has already played a phase of this game, it will be showed the right map with the next phase unlocked.
	 * 
	 * POSSO USAR ESSE MÉTODO PARA CRIAR UM ATALHO NA TELA PRINCIPAL E DIRECIONAR O ALUNO DIRETO PRO MAPA QUE ELE DEVE IR.
	 * TAMBÉM POSSO FAZER ALGO PARECIDO NA TELA INICIAL PARA DIRECIONAR O ALUNO DIRETO PRA AULA QUE ELE DEVE IR.
	 * 
	 * @param model
	 * @param gameName
	 * @return
	 */
	/*@RequestMapping(value = {"/gamesOLD/{gameName}"}, method = RequestMethod.GET)
	public final String gamePhases(final Model model, @PathVariable("gameName") String gameName) {
		Game game = gameService.findByNamelink(gameName);

		// If the game doesn't exist.
		if (game == null) {
			return "redirect:/";
		}
		
		// Checking what is the last phase completed by this player in this game.
		PlayerPhase lastPhaseCompleted = playerPhaseService.findLastPhaseCompleted(currentAuthenticatedUser().getUser(), game);
		// Checking what is the current map.
		Map map = findCurrentMap(game, lastPhaseCompleted);
		
		// If there is not any map for this game.
		if (map == null) {
			return "redirect:/";
		}
		
		// Looking for the phases of the map.
		List<Phase> phases = findPhasesByMap(map, lastPhaseCompleted);
		
		// If there are not phases in the map.
		if (phases == null) {
			return "redirect:/";
		}

		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phases", phases);

		return "games/map";
	}*/

	/**
	 * Get the current map based on the last phase completed by the player in a specific game.
	 * 
	 * @param lastPhaseCompleted
	 * @return
	 */
	public final Map findCurrentMap(Game game, PlayerPhase lastPhaseCompleted) {
		Map map = null;

		// If the player has never completed any phase of this game. 
		if (lastPhaseCompleted == null) {
			// Find the first map of the first level of this game.
			map = mapService.findMapByGameAndLevel(game.getId(), 1);
		}
		// If the player has already completed at least one phase of this game.
		else {
			// Looking for the next phase.
			Phase nextPhase = phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap(), lastPhaseCompleted.getPhase().getOrder() + 1);

			// If the next phase is in the same map than the last phase completed by the player for this game.
			if (nextPhase != null) {
				map = lastPhaseCompleted.getPhase().getMap();
			}
			// If the next phase is in the next map or in the next level.
			else {
				// Checking if the next map is in the same level.
				Map nextMapSameLevel = mapService.findNextMapSameLevel(lastPhaseCompleted.getPhase().getMap());

				// If the next map is in the same level.
				if (nextMapSameLevel != null) {
					map = nextMapSameLevel;
				}
				// If the next map is not in the same level. 
				else {
					// Find the first map of the next level.
					Map firstMapNextLevel = mapService.findMapByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1);

					// If it has found the first map of the next level.
					if (firstMapNextLevel != null) {
						map = firstMapNextLevel;
						map.setLevelCompleted(true);
					}
					// It doesn't exist a next map, because the player has already finished the last phase of the last level. 
					else {
						// Draw the last map of the last level.
						map = lastPhaseCompleted.getPhase().getMap();
						map.setGameCompleted(true);
					}
				}
			}
		}

		return map;
	}
	
	/**
	 * Get the phases of the map.
	 * Check which phases are opened.
	 * 
	 * @param map
	 * @return
	 */
	public final List<Phase> findPhasesByMap(Map map, PlayerPhase lastPhaseCompleted) {
		List<Phase> phases = phaseService.findPhasesByMap(map.getId());
		
		// If there are not phases in the map.
		if (phases == null || phases.size() == 0) {
			return null;
		}

		// If the player has never completed any phase of this game.
		if (lastPhaseCompleted == null) {
			// Open the first phase of the map.
			phases.get(0).setOpened(true);
		}
		// If the player has already completed at least one phase of this game.
		else {
			// Open all phases until the next phase.
			for (int i = 0; i < phases.size(); i++) {
				if (phases.get(i).getOrder() <= (lastPhaseCompleted.getPhase().getOrder() + 1)) {
					phases.get(i).setOpened(true);
				}
			}
		}
		
		return phases;
	}

	@RequestMapping(value = {"/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}"}, method = RequestMethod.GET)
	public final String initPhase(
			final Model model, 
			@PathVariable("gameName") String gameName, 
			@PathVariable("levelOrder") String levelOrderStr,
			@PathVariable("mapOrder") String mapOrderStr,
			@PathVariable("phaseOrder") String phaseOrderStr) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(levelOrderStr);
		intParameters.add(mapOrderStr);
		intParameters.add(phaseOrderStr);
		
		if (!isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		int levelOrder = Integer.parseInt(levelOrderStr);
		int mapOrder = Integer.parseInt(mapOrderStr);
		int phaseOrder = Integer.parseInt(phaseOrderStr);
		
		Game game = gameService.findByNamelink(gameName);
		
		// If the game doesn't exist.
		if (game == null) {
			return "redirect:/";
		}
		
		Map map = mapService.findMapsByGameLevelAndOrder(game, levelOrder, mapOrder);
		
		// If the map doesn't exist.
		if (map == null) {
			return "redirect:/";
		}
		
		Phase phase = phaseService.findPhaseByMapAndOrder(map, phaseOrder);
		
		// If the phase doesn't exist.
		if (phase == null) {
			return "redirect:/";
		}
		
		// If the player doesn't have permission to access this phase.
		if (!playerCanAccessThisPhase(phase)) {
			return "redirect:/";
		}
		
		// Get the first content of this phase.
		Content content = contentService.findContentByPhaseAndOrder(phase, 1);
		
		// If the content doesn't exist.
		if (content == null) {
			return "redirect:/";
		}
		
		// If the player doesn't have credits anymore.
		if (!playerHasCredits(phase)) {
			model.addAttribute("msg", "Seus créditos são insuficientes para fazer essa fase. Assim que você colocar mais créditos, a fase será liberada.");
			return "buycredits";
		}
		
		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phase", phase);
		model.addAttribute("content", content);
		
		return "games/phaseContent";
	}
	
	/**
	 * Verify if the player has enough credits to play the phase.
	 * Return true, it the player has credits.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerHasCredits(Phase phase) {
		// If the player doesn't have credits anymore.
		int playerId = currentAuthenticatedUser().getUser().getId();
		Player player = playerService.getPlayer(playerId);
		
		if (player.getCredit() <= 0) {
			// The first phase is always free.
			if (phase.getOrder() > 1) {
				
				// Get the last phase that the player has done in a specific game.
				Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(currentAuthenticatedUser().getUser(), phase.getMap().getGame());
				
				// If the player is trying to access a phase that he has already finished, it's OK.
				if (lastPhaseDone.getOrder() < phase.getOrder()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Verify if the player has permission to access a specific phase.
	 * Return true if the player has the permission.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerCanAccessThisPhase(Phase phase) {

		// The first phase is always permitted.
		if (phase.getOrder() == 1) {
			return true;
		}
		
		// Get the last phase that the player has done in a specific game.
		Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(currentAuthenticatedUser().getUser(), phase.getMap().getGame());
		
		// If the player is trying to access a phase but he never had finished a phase of this game.
		if (lastPhaseDone == null) {
			return false;
		}
		
		// If the player is trying to access a phase that he had already done OR the next phase in the right sequence.
		if (lastPhaseDone.getOrder() >= (phase.getOrder() - 1)) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(value = {"/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test"}, method = RequestMethod.GET)
	public final String initTest(
			HttpSession session,
			final Model model, 
			@PathVariable("gameName") String gameName, 
			@PathVariable("levelOrder") String levelOrderStr,
			@PathVariable("mapOrder") String mapOrderStr,
			@PathVariable("phaseOrder") String phaseOrderStr) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(levelOrderStr);
		intParameters.add(mapOrderStr);
		intParameters.add(phaseOrderStr);
		
		if (!isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		int levelOrder = Integer.parseInt(levelOrderStr);
		int mapOrder = Integer.parseInt(mapOrderStr);
		int phaseOrder = Integer.parseInt(phaseOrderStr);
		
		Game game = gameService.findByNamelink(gameName);
		
		// If the game doesn't exist.
		if (game == null) {
			return "redirect:/";
		}
		
		Map map = mapService.findMapsByGameLevelAndOrder(game, levelOrder, mapOrder);
		
		// If the map doesn't exist.
		if (map == null) {
			return "redirect:/";
		}
		
		Phase phase = phaseService.findPhaseByMapAndOrder(map, phaseOrder);
		
		// If the phase doesn't exist.
		if (phase == null) {
			return "redirect:/";
		}
		
		// If the player has already passed this test he can't see the test again.
		if (phaseAlreadyCompleted(phase)) {
			return "redirect:/games/" + gameName;
		}
		
		// If the player doesn't have permission to access this phase.
		if (!playerCanAccessThisPhase(phase)) {
			return "redirect:/";
		}
		
		// If the player doesn't have credits anymore.
		if (!playerHasCredits(phase)) {
			model.addAttribute("msg", "Seus créditos são insuficientes para fazer essa fase. Assim que você colocar mais créditos, a fase será liberada.");
			return "buycredits";
		}
		
		// Adding a playerPhase at T007.
		setTestAttempt(phase);
		
		// Get the questionary of this phase.
		Content content = contentService.findContentByPhaseAndOrder(phase, 0);
		List<Question> questions = questionService.findQuestionsByContent(content.getId());
		
		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phase", phase);
		model.addAttribute("questions", questions);
		
		List<Integer> questionsId = new ArrayList<Integer>();
		for (Question q : questions) {
			questionsId.add(q.getId());
		}
		
		session.setAttribute("questionsId" , questionsId);
		
		return "games/phaseTest";
	}
	
	/**
	 * This method is used to register an attempt of doing the test.
	 * 
	 * @param phase
	 */
	public void setTestAttempt(Phase phase) {
		PlayerPhase playerPhase = playerPhaseService.findPlayerPhaseByPlayerPhaseAndStatus(currentAuthenticatedUser().getUser(), phase, 2);
		
		// If this is not the first attempt.
		if (playerPhase != null) {
			playerPhase.setNumAttempts(playerPhase.getNumAttempts() + 1);
		}
		else {
			playerPhase = new PlayerPhase();
			
			playerPhase.setNumAttempts(1);
			playerPhase.setPhase(phase);
			Phasestatus phasestatus = new Phasestatus();
			phasestatus.setId(2);
			playerPhase.setPhasestatus(phasestatus);
			
			int playerId = currentAuthenticatedUser().getUser().getId();
			Player player = playerService.getPlayer(playerId);
			
			playerPhase.setPlayer(player);
		}
		
		playerPhaseService.save(playerPhase);
	}
	
	/**
	 * Verify if the phase was already done by the player.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean phaseAlreadyCompleted(Phase phase) {

		PlayerPhase playerPhase = playerPhaseService.findPlayerPhaseByPlayerPhaseAndStatus(currentAuthenticatedUser().getUser(), phase, 3);
		
		// This phase is already completed by this phase.
		if (playerPhase != null) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(value = {"/games/result"}, method = RequestMethod.POST)
	public final String showResultTest(
			HttpSession session, 
			final Model model, 
			@RequestParam java.util.Map<String, String> playerAnswers) {
		
		@SuppressWarnings("unchecked")
		List<Integer> questionsId = (List<Integer>) session.getAttribute("questionsId");
		
		if (questionsId == null || questionsId.size() == 0) {
			return "redirect:/";
		}
		
		Phase currentPhase = null;
		Question questionAux = null;
		int countQuestionsRight = 0, phaseOrder = 0;
		String playerAnswer = null;
		
		for (Integer questionId : questionsId) {
			questionAux = questionService.find(questionId);
			
			if (questionAux.getContent().getPhase().getOrder() > phaseOrder) {
				phaseOrder = questionAux.getContent().getPhase().getOrder();
				currentPhase = questionAux.getContent().getPhase();
			}
			
			playerAnswer = playerAnswers.get(questionId.toString());
			
			if (playerAnswer != null) {
				for (Answer answer : questionAux.getAnswers()) {
					if ((answer.getId() == Integer.parseInt(playerAnswer))
							&& answer.isRight()) {
						countQuestionsRight++;
						continue;
					}
				}
			}
		}
		
		int grade = (countQuestionsRight * 100) / questionsId.size();
		
		model.addAttribute("grade", grade);
		
		if (grade >= 70) {
			PlayerPhase playerPhase = playerPhaseService.findPlayerPhaseByPlayerPhaseAndStatus(currentAuthenticatedUser().getUser(), currentPhase, 2);
			
			playerPhase.setGrade(grade);
			Phasestatus phasestatus = new Phasestatus();
			phasestatus.setId(3);
			playerPhase.setPhasestatus(phasestatus);
			playerPhase.setDtTest(new Date());
			playerPhase.setScore(calculateScore(playerPhase.getNumAttempts(), grade));
			
			int playerId = currentAuthenticatedUser().getUser().getId();
			Player player = playerService.getPlayer(playerId);
			player.setScore(player.getScore() + playerPhase.getScore());
			player.setCredit(player.getCredit() - 1);
			
			// Update session user. 
			updateCurrentAuthenticateUser(player);
			
			// Changing the status of this phase.
			// Now the phase is completed and the player can play the next phase.
			playerPhaseService.save(playerPhase);
			
			// Checking what is the map of the next phase.
			Map map = findCurrentMap(currentPhase.getMap().getGame(), playerPhase);
			
			// The attribute levelCompleted will be true if the player has just finished the last phase of the last map of the level.
			if (map.isLevelCompleted()) {
				updateCurrentAuthenticateUser(addCreditToPlayer(currentAuthenticatedUser().getUser().getId(), 3));
				return "games/endoflevel";
			}
			
			// The attribute gameCompleted will be true if the player has just finished the last phase of the last map of the last level of the game.
			if (map.isGameCompleted()) {
				updateCurrentAuthenticateUser(addCreditToPlayer(currentAuthenticatedUser().getUser().getId(), 3));
				return "games/endofgame";
			}
			
			// Looking for the phases of the map.
			List<Phase> phases = findPhasesByMap(map, playerPhase);
			
			Phase nextPhase = null;
			
			label: {
				for (Phase p : phases) {
					if (p.isOpened()) {
						nextPhase = p;
					}
					else {
						break label;
					}
				}
			}
			
			model.addAttribute("phase", nextPhase);
		}
		else {
			model.addAttribute("phase", currentPhase);
		}
		
		return "games/resultTest";
	}
	
	/**
	 * This method is used to calculate the correct score to the player.
	 * 
	 * @param numAttempts
	 * @param grade
	 * @return
	 */
	public int calculateScore(int numAttempts, int grade) {
		/*
		 1
		 100 = 100
		 90 = 90
		 80 = 80
		 70 = 70
		 2
		 100 = 70 
		 90 = 65
		 80 = 55
		 70 = 50
		 3
		 100 = 50
		 90 = 45
		 80 = 35
		 70 = 30
		 4...
		 10 
		 */
		
		if (numAttempts == 1) {
			return grade;
		}
		
		if (numAttempts == 2) {
			if (grade > 90) {
				return 70;
			}
			if (grade > 80) {
				return 65;
			}
			if (grade > 70) {
				return 55;
			}
			if (grade == 70) {
				return 50;
			}
		}
		
		if (numAttempts == 3) {
			if (grade > 90) {
				return 50;
			}
			if (grade > 80) {
				return 45;
			}
			if (grade > 70) {
				return 35;
			}
			if (grade == 70) {
				return 30;
			}
		}
		
		return 10;
	}
	
	/**
	 * 
	 * @param player
	 * @param qtdCredits
	 */
	public Player addCreditToPlayer(int playerId, int qtdCredits) {
		return playerService.addCreditsToPlayer(playerId, qtdCredits);
	}
}