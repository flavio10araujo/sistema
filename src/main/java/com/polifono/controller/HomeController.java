package com.polifono.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;
import com.polifono.util.EmailSendUtil;
import com.polifono.util.EmailUtil;

@Controller
public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public final String index(final Model model, @RequestParam Optional<String> error) {
    	LOGGER.debug("Getting login page, error={}", error);
    	model.addAttribute("player", new Player());
    	model.addAttribute("error", error);
    	return "index";
	}
    
    @RequestMapping(value = {"/contact"}, method = RequestMethod.GET)
    public final String contact() {
    	
    	boolean logged = false;
    	
    	CurrentUser currentUser = currentAuthenticatedUser();
    	
    	// If the user is logged in.
    	if (currentUser != null) {
    		logged = true;
    	}
    	
    	if (logged) {
			return "contact";
		}
		else {
			return "contactOpen";
		}
    }
    
    @RequestMapping(value = {"/contact"}, method = RequestMethod.POST)
    public final String contactsubmit(final Model model, @RequestParam(value = "email", defaultValue = "") String email, @RequestParam("message") String message) {
    	
    	boolean logged = false;
    	
    	CurrentUser currentUser = currentAuthenticatedUser();
    	
    	// If the user is logged in, get his email.
    	if (currentUser != null) {
    		logged = true;
    		email = currentUser.getUser().getEmail();
    	}
    	
    	String msg = validateContact(email, message);
    	
    	// If there are errors.
    	if (!msg.equals("")) {
    		model.addAttribute("message", "error");
			model.addAttribute("messageContent", msg);
			
			if (logged) {
				return "contact";
			}
			else {
				return "contactOpen";
			}
    	}
    	
    	sendEmailContact(email, message);
    	
    	model.addAttribute("message", "success");
    	
    	if (logged) {
			return "contact";
		}
		else {
			return "contactOpen";
		}
    }
    
    public String validateContact(String email, String message) {
		String msg = "";
		
		if (email == null || email.equals("")) {
			msg = msg + "<br />O e-mail precisa ser informado.";
		}
		else if (!EmailUtil.validateEmail(email)) {
			msg = msg + "<br />O e-mail informado não é válido.";
		}
		
		if (message == null || message.equals("")) {
			msg = msg + "<br />A mensagem precisa ser informada.";
		}
		
		return msg;
	}
    
    public void sendEmailContact(String email, String message) {
		String[] args = new String[2];
		args[0] = email;
		args[1] = message;
		
		try {
			EmailSendUtil.sendHtmlMail(5, "", args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}