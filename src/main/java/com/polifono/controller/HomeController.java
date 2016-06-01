package com.polifono.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Player;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public final String index(final Model model, @RequestParam Optional<String> error) {
    	LOGGER.debug("Getting login page, error={}", error);
    	model.addAttribute("player", new Player());
    	model.addAttribute("error", error);
    	
    	return "index";
	}
    
    /*@RequestMapping("/error.html")
    public String error(HttpServletRequest request, Model model) {
    	model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
    	Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    	String errorMessage = null;
      
    	if (throwable != null) {
    		errorMessage = throwable.getMessage();
    	}
      
    	model.addAttribute("errorMessage", errorMessage);
    	return "error.html";
    }*/
}