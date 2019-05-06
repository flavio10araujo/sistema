package com.polifono.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.polifono.domain.Phase;
import com.polifono.service.IPhaseService;

@Controller
public class SearchController extends BaseController {

	@Autowired
	private IPhaseService phaseService;
	
	public static final String URL_GAMES_INDEX = "games/index";
	public static final String URL_GAMES_RESULT_SEARCH = "games/resultSearch";
	
	public static final String REDIRECT_HOME = "redirect:/";
	public static final String REDIRECT_GAMES = "redirect:/games/";

	/**
	 * Search q in all the classes that the user already studied.
	 * 
	 * @param model
	 * @param q
	 * @return
	 */
	@RequestMapping(value = {"/search"}, method = RequestMethod.GET, params = {"q"})
	public final String searchContent(final Model model, @RequestParam(value = "q") String q) {
		
		if (q == null || "".equals(q.trim())) {
			return URL_GAMES_INDEX;
		} else if (q.trim().length() < 3 || q.trim().length() > 25) {
			return URL_GAMES_INDEX;
		}
		
		// Looking for the phases that have the q in its content and the user has already studied.
		List<Phase> phases = phaseService.findPhasesBySearchAndUser(HtmlUtils.htmlEscape(q), this.currentAuthenticatedUser().getUser().getId());
		
		model.addAttribute("phases", phases);
		
		return URL_GAMES_RESULT_SEARCH;
	}
}