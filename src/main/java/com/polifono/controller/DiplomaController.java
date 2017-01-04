package com.polifono.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiplomaController extends BaseController {
	
	public static final String URL_DIPLOMA = "diploma";
    public static final String URL_DIPLOMAOPEN = "diplomaOpen";

	@RequestMapping(value = {"/diploma"}, method = RequestMethod.GET)
    public final String diplomaSearch() {
    	// If the user is logged in.
    	if (this.currentAuthenticatedUser() != null) {
    		return URL_DIPLOMA;
		}
		else {
			return URL_DIPLOMAOPEN;
		}
    }
	
	@RequestMapping(value = {"/diploma"}, method = RequestMethod.POST)
    public final String diplomaSearchSubmit(final Model model, @RequestParam(value = "code", defaultValue = "") String code) {
    	
    	model.addAttribute("message", "success");
    	
    	// If the user is logged in, get his email.
    	if (currentAuthenticatedUser() != null) return URL_DIPLOMA;
		else return URL_DIPLOMAOPEN;
    }
}