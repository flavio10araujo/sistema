package com.polifono.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Diploma;
import com.polifono.service.IDiplomaService;

@Controller
public class DiplomaController extends BaseController {
	
	@Autowired
	private IDiplomaService diplomaService;
	
	public static final String URL_DIPLOMA_SEARCH = "diplomaSearch";
    public static final String URL_DIPLOMAOPEN_SEARCH = "diplomaSearchOpen";
    public static final String URL_DIPLOMA_GET = "diplomaGet";
    public static final String URL_DIPLOMAOPEN_GET = "diplomaGetOpen";

	@RequestMapping(value = {"/diploma"}, method = RequestMethod.GET)
    public final String diplomaSearch() {
    	// If the user is logged in.
    	if (this.currentAuthenticatedUser() != null) {
    		return URL_DIPLOMA_SEARCH;
		}
		else {
			return URL_DIPLOMAOPEN_SEARCH;
		}
    }
	
	@RequestMapping(value = {"/diploma"}, method = RequestMethod.POST)
    public final String diplomaSearchSubmit(final Model model, @RequestParam(value = "code", defaultValue = "") String code) {
    	
		if (code == null || "".equals(code)) {
			// If the user is logged in, get his email.
	    	if (currentAuthenticatedUser() != null) return URL_DIPLOMA_SEARCH;
			else return URL_DIPLOMAOPEN_SEARCH;
		}

		Diploma diploma = diplomaService.findByCode(code);
		
		if (diploma != null) {
			model.addAttribute("message", "success");
			model.addAttribute("diploma", diploma);
		}
		else {
			model.addAttribute("message", "error");
		}
    	
    	// If the user is logged in, get his email.
    	if (currentAuthenticatedUser() != null) return URL_DIPLOMA_SEARCH;
		else return URL_DIPLOMAOPEN_SEARCH;
    }
	
	@RequestMapping(value = {"/diploma/{code}"}, method = RequestMethod.GET)
	public final String diplomaGet(final Model model, @PathVariable("code") String code) {
		if (code == null || "".equals(code)) {
			// If the user is logged in, get his email.
	    	if (currentAuthenticatedUser() != null) return URL_DIPLOMA_SEARCH;
			else return URL_DIPLOMAOPEN_SEARCH;
		}
		
		Diploma diploma = diplomaService.findByCode(code);
		
		if (diploma != null) {
			model.addAttribute("message", "success");
			model.addAttribute("diploma", diploma);
		}
		else {
			model.addAttribute("message", "error");
		}
    	
    	// If the user is logged in, get his email.
    	if (currentAuthenticatedUser() != null) return URL_DIPLOMA_GET;
		else return URL_DIPLOMAOPEN_GET;
	}
}