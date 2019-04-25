package com.polifono.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.domain.Content;
import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.service.IContentService;
import com.polifono.service.IGameService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerService;

@Controller
public class PromoController extends BaseController {

	@Autowired
	@Qualifier("playerServiceImpl")
	private IPlayerService playerService;
	
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IMapService mapService;
	
	@Autowired
	private IPhaseService phaseService;
	
	@Autowired
	private IContentService contentService;
	
	public static final String URL_PROMOS_PHASECONTENT_MUSIC_THEORY = "promos/phaseContentMusicalTheory";
	public static final String URL_PROMOS_PHASECONTENT_RECORDER = "promos/phaseContentRecorder";
	public static final String URL_PROMOS_PHASECONTENT_ACOUSTIC_GUITAR = "promos/phaseContentAcousticGuitar";
	public static final String URL_PROMOS_PHASECONTENT_SAXOPHONE = "promos/phaseContentSaxophone";
	
	public static final String REDIRECT_HOME = "redirect:/";

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"/promos"}, method = RequestMethod.GET)
	public final String listGames(final Model model) {
		return REDIRECT_HOME;
	}

	@RequestMapping(value = {"/promos/{gameName}"}, method = RequestMethod.GET)
	public final String listPromosLevel05(
			final Model model, 
			@PathVariable("gameName") String gameName
			) {
		
		// http://www.polifono.com/promos/musical_theory
		// http://www.polifono.com/promos/recorder
		// http://www.polifono.com/promos/acoustic_guitar
		// http://www.polifono.com/promos/saxophone
		
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1;
		String gameNameParam = gameName;
		
		model.addAttribute("player", new Player());
		model.addAttribute("playerResend", new Player());
		
		// workaround - if it's trying to see the musical_theory course, it's showed the recorder course.
		if ("musical_theory".equals(gameName)) {
			gameNameParam = "recorder";
		}
		
		Game game = gameService.findByNamelink(gameNameParam);
		
		// If the game doesn't exist.
		if (game == null) return REDIRECT_HOME;
		
		Map map = mapService.findByGameLevelAndOrder(game.getId(), levelOrder, mapOrder);
		
		// If the map doesn't exist.
		if (map == null) return REDIRECT_HOME;
		
		Phase phase = phaseService.findByMapAndOrder(map.getId(), phaseOrder);
		
		// If the phase doesn't exist.
		if (phase == null) return REDIRECT_HOME;
		
		// Get the first content of this phase.
		Content content = contentService.findByPhaseAndOrder(phase.getId(), 1);
		
		// If the content doesn't exist.
		if (content == null) return REDIRECT_HOME;
		
		model.addAttribute("game", game);
		model.addAttribute("map", map);
		model.addAttribute("phase", phase);
		model.addAttribute("content", content);
		
		// saxophone
		if (game.getId() == 3) {
			return URL_PROMOS_PHASECONTENT_SAXOPHONE;
		}
		// acoustic_guitar
		else if (game.getId() == 2) {
			return URL_PROMOS_PHASECONTENT_ACOUSTIC_GUITAR;
		}
		// musical_theory
		else if ("musical_theory".equals(gameName)) {
			return URL_PROMOS_PHASECONTENT_MUSIC_THEORY;
		}
		// recorder
		else {
			return URL_PROMOS_PHASECONTENT_RECORDER;
		}
	}
}