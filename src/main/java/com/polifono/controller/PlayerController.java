package com.polifono.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IPlayerService;
import com.polifono.util.EmailSendUtil;
import com.polifono.util.EmailUtil;
import com.polifono.util.RandomStringUtil;

@Controller
public class PlayerController extends BaseController {
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private IClassPlayerService classPlayerService;
	
	@RequestMapping(value = {"/player/create"}, method = RequestMethod.POST)
	public final String createPlayer(final Model model, @ModelAttribute("player") Player player) {
		if (player == null) {
			model.addAttribute("player", new Player());
			return "index";
		}
		
		// Verify if the email is already in use.
		Player playerOld = playerService.findByEmail(player.getEmail());
		
		if (playerOld != null) {
			model.addAttribute("player", player);
			model.addAttribute("codRegister", 2);
			model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " já está cadastrado para outra pessoa.");
		}
		else {
			String msg = validateCreatePlayer(player);
			
			// If there is not errors.
			if (msg.equals("")) {
				player.setEmailConfirmed(new RandomStringUtil(10).nextString()); // This field is sent to the player's email to confirm if the email is real.
				model.addAttribute("player", playerService.create(player));
				model.addAttribute("codRegister", 1);
				sendEmailConfirmRegister(player);
			}
			else {
				model.addAttribute("player", player);
				model.addAttribute("codRegister", 2);
				model.addAttribute("msgRegister", msg);
			}
		}
		
		return "index";
	}
	
	@RequestMapping(value = {"/emailconfirmation"}, method = RequestMethod.GET)
	public final String emailconfirmation(final Model model) {
		model.addAttribute("player", new Player());
		model.addAttribute("playerResend", new Player());
		return "emailconfirmation";
	}
	
	@RequestMapping(value = {"/emailconfirmation"}, method = RequestMethod.POST)
	public final String emailconfirmationsubmit(final Model model, @ModelAttribute("player") Player player) {
		model.addAttribute("playerResend", new Player());
		
		if (player == null) {
			model.addAttribute("player", new Player());
			return "emailconfirmation";
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(player.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("player", player);
			model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the player was already confirmed.
			if (playerOld.isIndEmailConfirmed()) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("player", player);
				model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
			}
			else {
				// If the code informed is not correct.
				if (!player.getEmailConfirmed().equals(playerOld.getEmailConfirmed())) {
					model.addAttribute("codRegister", 2);
					model.addAttribute("player", player);
					model.addAttribute("msgRegister", "<br />O código de ativação informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
				}
				else {
					playerOld.setIndEmailConfirmed(true);
					playerService.save(playerOld);
										
					model.addAttribute("codRegister", 1);
					model.addAttribute("player", new Player());
					model.addAttribute("msgRegister", "<br />O e-mail " + playerOld.getEmail() + " foi confirmado com sucesso!");
				}
			}
		}
		
		return "emailconfirmation";
	}
	
	@RequestMapping(value = {"/emailconfirmationresend"}, method = RequestMethod.POST)
	public final String emailconfirmationresend(final Model model, @ModelAttribute("playerResend") Player playerResend) {
	
		model.addAttribute("player", new Player());
		
		if (playerResend == null) {
			model.addAttribute("playerResend", new Player());
			return "emailconfirmation";
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(playerResend.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("playerResend", playerResend);
			model.addAttribute("msgRegister", "<br />O email " + playerResend.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the player was already confirmed.
			if (playerOld.isIndEmailConfirmed()) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("playerResend", playerResend);
				model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
			}
			else {
				playerOld.setEmailConfirmed(new RandomStringUtil(10).nextString());
				playerService.save(playerOld);
				sendEmailConfirmRegister(playerOld);
				
				model.addAttribute("codRegister", 1);
				model.addAttribute("playerResend", new Player());
				model.addAttribute("msgRegister", "<br />O e-mail com o código de ativação foi reenviado para " + playerOld.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
			}
		}
		
		return "emailconfirmation";
	}
	
	@RequestMapping(value = {"/passwordreset"}, method = RequestMethod.GET)
	public final String passwordreset(final Model model) {
		model.addAttribute("player", new Player());
		model.addAttribute("playerResend", new Player());
		return "passwordreset";
	}
	
	@RequestMapping(value = {"/passwordresetresend"}, method = RequestMethod.POST)
	public final String passwordreseteresend(final Model model, @ModelAttribute("playerResend") Player playerResend) {
	
		model.addAttribute("player", new Player());
		
		if (playerResend == null) {
			model.addAttribute("playerResend", new Player());
			return "passwordreset";
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(playerResend.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("playerResend", playerResend);
			model.addAttribute("msgRegister", "<br />O email " + playerResend.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			playerOld.setPasswordReset(new RandomStringUtil(10).nextString());
			playerService.save(playerOld);
			sendEmailPasswordReset(playerOld);
				
			model.addAttribute("codRegister", 1);
			model.addAttribute("playerResend", new Player());
			model.addAttribute("msgRegister", "<br />O e-mail com o código para alterar a senha foi enviado para " + playerOld.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
		}
		
		return "passwordreset";
	}
	
	@RequestMapping(value = {"/passwordreset"}, method = RequestMethod.POST)
	public final String passwordresetsubmit(final Model model, @ModelAttribute("player") Player player) {
		model.addAttribute("playerResend", new Player());
		
		if (player == null) {
			model.addAttribute("player", new Player());
			return "passwordreset";
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(player.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("player", player);
			model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the code informed is not correct.
			if (!player.getPasswordReset().equals(playerOld.getPasswordReset())) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("player", player);
				model.addAttribute("msgRegister", "<br />O código de confirmação da alteração da senha informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
			}
			else {
				playerOld.setPassword(player.getPassword());
				String msg = validateCreatePlayer(playerOld);
				
				// If there is not errors.
				if (msg.equals("")) {
					playerOld.setPassword(playerService.encryptPassword(playerOld.getPassword()));
					playerOld.setPasswordReset(""); // If the user has changed the password successfully, the reset code is cleaned.
					playerService.save(playerOld);
					
					model.addAttribute("codRegister", 1);
					model.addAttribute("player", new Player());
					model.addAttribute("msgRegister", "<br />A senha de acesso para o e-mail " + playerOld.getEmail() + " foi alterada com sucesso!");
				}
				else {
					model.addAttribute("codRegister", 2);
					model.addAttribute("player", player);
					model.addAttribute("msgRegister", msg);
				}
			}
		}
		
		return "passwordreset";
	}
	
	@RequestMapping(value = {"/classinvitation"}, method = RequestMethod.GET)
	public final String classinvitation(final Model model) {
		
		// Get all the invitation to classes that the student hasn't confirmed his participation yet.
		List<ClassPlayer> classPlayers = classPlayerService.findByPlayerAndStatus(currentAuthenticatedUser().getUser().getId(), 1);
		
		model.addAttribute("classPlayers", classPlayers);
		
		return "classinvitation";
	}
	
	@RequestMapping(value = {"/classinvitation/{id}"}, method = RequestMethod.GET)
	public final String classinvitationsubmit(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes, final Model model) {
		
		try {
			ClassPlayer current = classPlayerService.find(id.intValue());
			
			// If the classPlayer doesn't exist.
			if (current == null) {
				return "redirect:/";
			}
			
			// Verifying if the student logged in is not the player of this classPlayer.
			if (current.getPlayer().getId() != currentAuthenticatedUser().getUser().getId()) {
				return "redirect:/";
			}
			
			if (current.getStatus() != 1) {
				return "classinvitation";
			}
			
			if (classPlayerService.changeStatus(id.intValue(), 2)) {
				redirectAttributes.addFlashAttribute("message", "success");
			}
			else {
				redirectAttributes.addFlashAttribute("message", "unsuccess");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/classinvitation";
	}
	
	public String validateCreatePlayer(Player player) {
		String msg = "";
		
		if (player.getName() == null || player.getName().equals("")) {
			msg = msg + "<br />O nome precisa ser informado.";
		}
		
		if (player.getEmail() == null || player.getEmail().equals("")) {
			msg = msg + "<br />O e-mail precisa ser informado.";
		}
		else if (!EmailUtil.validateEmail(player.getEmail())) {
			msg = msg + "<br />O e-mail informado não é válido.";
		}
		
		if (player.getPassword() == null || player.getPassword().equals("")) {
			msg = msg + "<br />A senha precisa ser informada.";
		}
		else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
			msg = msg + "<br />A senha precisa possuir entre 6 e 20 caracteres.";
		}
		else if (!EmailUtil.validatePassword(player.getPassword())) {
			msg = msg + "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.";
		}
		
		return msg;
	}
	
	/**
	 * This method is used to send the email to the user confirm his email.
	 * 
	 * @param player
	 */
	public void sendEmailConfirmRegister(Player player) {
		String[] args = new String[3];
		args[0] = player.getName();
		args[1] = player.getEmail();
		args[2] = player.getEmailConfirmed();
		
		try {
			EmailSendUtil.sendHtmlMail(1, player.getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to send the email to the user when he doesn't remember his password.
	 * 
	 * @param player
	 */
	public void sendEmailPasswordReset(Player player) {
		String[] args = new String[3];
		args[0] = player.getName();
		args[1] = player.getEmail();
		args[2] = player.getPasswordReset();
		
		try {
			EmailSendUtil.sendHtmlMail(2, player.getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}