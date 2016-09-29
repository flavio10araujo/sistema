package com.polifono.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);
	private static final String PATH = "/error";
	
	@Override
    public final String getErrorPath() {
        return PATH;
    }
	
	@RequestMapping(value=PATH)
	public final String error() {
		//return "error/error";
		return "redirect:/";
	}

	@RequestMapping("/error404")
	String error404() {
		LOGGER.debug("Entrou no error404");
		return "error/404";
	}
	
	@RequestMapping("/error500")
	String error500() {
		LOGGER.debug("entrou no error500");
		return "error/500";
	}
}