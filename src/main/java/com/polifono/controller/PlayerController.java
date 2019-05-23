package com.polifono.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.polifono.domain.bean.PlayerFacebook;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IPlayerService;
import com.polifono.service.impl.LoginServiceImpl;
import com.polifono.util.EmailSendUtil;
import com.polifono.util.EmailUtil;
import com.polifono.util.RandomStringUtil;
import com.polifono.util.StringUtil;
import com.polifono.util.Util;

@Controller
public class PlayerController extends BaseController {
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private IClassPlayerService classPlayerService;
	
	@Autowired
	private LoginServiceImpl loginService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);
	
	public static final String URL_INDEX = "index";
	public static final String URL_EMAILCONFIRMATION = "emailconfirmation";
	public static final String URL_PASSWORDRESET = "index";
	public static final String URL_CLASSINVITATION = "classinvitation";
	
	public static final String REDIRECT_HOME = "redirect:/";
	public static final String REDIRECT_CLASSINVITATION = "redirect:/classinvitation";
	
	@RequestMapping(value = {"/player/create"}, method = RequestMethod.POST)
	public final String createPlayer(HttpServletRequest request, final Model model, @ModelAttribute("player") Player player) {
		
		model.addAttribute("playerResend", new Player());
		
		if (player == null) {
			LOGGER.debug("/player/create POST player is null");
			model.addAttribute("player", new Player());
			return URL_INDEX;
		}
		
		// Verify if the email is already in use.
		Player playerOld = null; 
				
		if (player.getEmail() != null && !"".equals(player.getEmail().trim())) {
			player.setEmail(EmailUtil.avoidWrongDomain(player.getEmail()));
			playerOld = playerService.findByEmail(player.getEmail());
		}
		
		if (playerOld != null) {
			model.addAttribute("player", player);
			model.addAttribute("codRegister", 2);
			// TODO - buscar msg do messages.
			model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " já está cadastrado para outra pessoa.");
		}
		else {
			String msg = playerService.validateCreatePlayer(player);
			
			// If there is not errors.
			if (msg.equals("")) {
				String password = player.getPassword();
				
				player.setName(StringUtil.formatNamePlayer(player.getName()));
				
				String name = player.getName();
				name = name.substring(0, name.indexOf(" "));
				
				String lastName = player.getName();
				lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();
				
				player.setLastName(lastName);
				player.setName(name);
				
				model.addAttribute("player", playerService.create(player));
				model.addAttribute("codRegister", 1);
				EmailSendUtil.sendEmailConfirmRegister(player);
				
				try {
					request.login(player.getEmail(), password);
				}
				catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return REDIRECT_HOME;
			}
			else {
				model.addAttribute("player", player);
				model.addAttribute("codRegister", 2);
				model.addAttribute("msgRegister", msg);
			}
		}
		
		return URL_INDEX;
	}
	
	@RequestMapping(value = {"/emailconfirmation"}, method = RequestMethod.GET)
	public final String emailconfirmation(final Model model) {
		model.addAttribute("player", new Player());
		model.addAttribute("playerResend", new Player());
		return URL_EMAILCONFIRMATION;
	}
	
	@RequestMapping(value = {"/emailconfirmation"}, method = RequestMethod.POST)
	public final String emailconfirmationsubmit(final Model model, @ModelAttribute("player") Player player) {
		model.addAttribute("playerResend", new Player());
		
		if (player == null) {
			model.addAttribute("player", new Player());
			return URL_EMAILCONFIRMATION;
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(player.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("player", player);
			// TODO - pegar msg do messages.
			model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the player was already confirmed.
			if (playerOld.isIndEmailConfirmed()) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("player", player);
				// TODO - pegar msg do messages.
				model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
			}
			else {
				// If the code informed is not correct.
				if (!player.getEmailConfirmed().equals(playerOld.getEmailConfirmed())) {
					model.addAttribute("codRegister", 2);
					model.addAttribute("player", player);
					// TODO - pegar msg do messages.
					model.addAttribute("msgRegister", "<br />O código de ativação informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
				}
				else {
					playerOld.setIndEmailConfirmed(true);
					playerService.save(playerOld);
										
					model.addAttribute("codRegister", 1);
					model.addAttribute("player", new Player());
					// TODO - pegar msg do messages.
					model.addAttribute("msgRegister", "<br />O e-mail " + playerOld.getEmail() + " foi confirmado com sucesso!");
				}
			}
		}
		
		return URL_EMAILCONFIRMATION;
	}
	
	@RequestMapping(value = {"/emailconfirmationresend"}, method = RequestMethod.POST)
	public final String emailconfirmationresend(final Model model, @ModelAttribute("playerResend") Player playerResend) {
	
		model.addAttribute("player", new Player());
		
		if (playerResend == null) {
			model.addAttribute("playerResend", new Player());
			return URL_EMAILCONFIRMATION;
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(playerResend.getEmail());
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("playerResend", playerResend);
			// TODO - pegar msg do messages.
			model.addAttribute("msgRegister", "<br />O email " + playerResend.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the player was already confirmed.
			if (playerOld.isIndEmailConfirmed()) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("playerResend", playerResend);
				// TODO - pegar msg do messages.
				model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
			}
			else {
				playerOld.setEmailConfirmed(new RandomStringUtil(10).nextString());
				playerService.save(playerOld);
				EmailSendUtil.sendEmailConfirmRegister(playerOld);
				
				model.addAttribute("codRegister", 1);
				model.addAttribute("playerResend", new Player());
				// TODO - pegar msg do messages.
				model.addAttribute("msgRegister", "<br />O e-mail com o código de ativação foi reenviado para " + playerOld.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
			}
		}
		
		return URL_EMAILCONFIRMATION;
	}
	
	@RequestMapping(value = {"/passwordreset"}, method = RequestMethod.GET)
	public final String passwordreset(final Model model) {
		model.addAttribute("player", new Player());
		model.addAttribute("playerResend", new Player());
		return URL_PASSWORDRESET;
	}
	
	@RequestMapping(value = {"/passwordresetresend"}, method = RequestMethod.POST)
	public final String passwordreseteresend(final Model model, @ModelAttribute("playerResend") Player playerResend) {
	
		model.addAttribute("player", new Player());
		
		if (playerResend == null) {
			model.addAttribute("playerResend", new Player());
			return URL_PASSWORDRESET;
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(playerResend.getEmail());
		boolean byLogin = false;
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			playerOld = playerService.findByLogin(playerResend.getEmail());
			byLogin = true;
		}
		
		// If there is not exist a player with this email/login.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("playerResend", playerResend);
			// TODO - pegar msg do messages.
			model.addAttribute("msgRegister", "<br />O login " + playerResend.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			playerOld.setPasswordReset(new RandomStringUtil(10).nextString());
			playerService.save(playerOld);
			
			if (!byLogin) {
				model.addAttribute("msgRegister", "<br />O código para alterar a senha foi enviado para " + playerOld.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
				EmailSendUtil.sendEmailPasswordReset(playerOld);
			}
			else {
				Player teacher = playerOld.getCreator();
				teacher.setName(playerOld.getName());
				teacher.setPasswordReset(playerOld.getPasswordReset());
				EmailSendUtil.sendEmailPasswordReset(teacher);
				model.addAttribute("msgRegister", "<br />O código para alterar a senha foi enviado para " + teacher.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
			}

			model.addAttribute("codRegister", 1);
			model.addAttribute("playerResend", new Player());
		}
		
		return URL_PASSWORDRESET;
	}
	
	@RequestMapping(value = {"/passwordreset"}, method = RequestMethod.POST)
	public final String passwordresetsubmit(final Model model, @ModelAttribute("player") Player player) {
		model.addAttribute("playerResend", new Player());
		
		if (player == null) {
			model.addAttribute("player", new Player());
			return URL_PASSWORDRESET;
		}
		
		// Verify if there is a player with this email.
		Player playerOld = playerService.findByEmail(player.getEmail());
		boolean byLogin = false;
		
		// If there is not exist a player with this email.
		if (playerOld == null) {
			playerOld = playerService.findByLogin(player.getEmail());
			byLogin = true;
		}
		
		// If there is not exist a player with this email/login.
		if (playerOld == null) {
			model.addAttribute("codRegister", 2);
			model.addAttribute("player", player);
			// TODO - pegar msg do messages.
			model.addAttribute("msgRegister", "<br />O login " + player.getEmail() + " não está cadastrado no sistema.");
		}
		else {
			// If the code informed is not correct.
			if (!player.getPasswordReset().equals(playerOld.getPasswordReset())) {
				model.addAttribute("codRegister", 2);
				model.addAttribute("player", player);
				// TODO - pegar msg do messages.
				model.addAttribute("msgRegister", "<br />O código de confirmação da alteração da senha informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
			}
			else {
				playerOld.setPassword(player.getPassword());
				String msg = playerService.validateChangePasswordPlayer(playerOld);
				
				// If there is not errors.
				if (msg.equals("")) {
					playerOld.setPassword(StringUtil.encryptPassword(playerOld.getPassword()));
					playerOld.setPasswordReset(""); // If the user has changed the password successfully, the reset code is cleaned.
					playerService.save(playerOld);
					
					model.addAttribute("codRegister", 1);
					model.addAttribute("player", new Player());
					// TODO - pegar msg do messages.
					if (!byLogin) {
						model.addAttribute("msgRegister", "<br />A senha de acesso para o login " + playerOld.getEmail() + " foi alterada com sucesso!");
					}
					else {
						model.addAttribute("msgRegister", "<br />A senha de acesso para o login " + playerOld.getLogin() + " foi alterada com sucesso!");
					}
				}
				else {
					model.addAttribute("codRegister", 2);
					model.addAttribute("player", player);
					model.addAttribute("msgRegister", msg);
				}
			}
		}
		
		return URL_PASSWORDRESET;
	}
	
	@RequestMapping(value = {"/classinvitation"}, method = RequestMethod.GET)
	public final String classinvitation(final Model model) {
		// Get all the invitation to classes that the student hasn't confirmed his participation yet.
		List<ClassPlayer> classPlayers = classPlayerService.findByPlayerAndStatus(currentAuthenticatedUser().getUser().getId(), 1);
		model.addAttribute("classPlayers", classPlayers);
		return URL_CLASSINVITATION;
	}

	@RequestMapping(value = {"/classinvitation/{id}"}, method = RequestMethod.GET)
	public final String classinvitationsubmit(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes, final Model model) {
		
		try {
			ClassPlayer current = classPlayerService.findOne(id.intValue());
			
			// If the classPlayer doesn't exist.
			if (current == null) return REDIRECT_HOME;
			
			// Verifying if the student logged in is not the player of this classPlayer.
			if (current.getPlayer().getId() != currentAuthenticatedUser().getUser().getId()) return REDIRECT_HOME;
			
			// If the player has already confirmed his participation in this class.
			if (current.getStatus() != 1) return URL_CLASSINVITATION;
			
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
		
		return REDIRECT_CLASSINVITATION;
	}
	
	/**
	 * Method used when the user does the login with his Facebook account.
	 * 
	 * @param 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@RequestMapping("/loginfb")
	public String loginfb(HttpServletRequest request, final Model model, String code) {
		
		try {
			JSONObject resp = new JSONObject(Util.readURL(new URL("https://graph.facebook.com/v2.12/me?fields=email,first_name,last_name&access_token=" + code)));
			PlayerFacebook playerFacebook = new PlayerFacebook(resp);
			
			if (playerFacebook == null || playerFacebook.getId() == null) {
				System.out.println("loginfb - playerFacebook is null");
				return URL_INDEX;
			}
			
			// Get the ID of the playerFacebook and verify is he is already related to any player.
			Player player = playerService.findByIdFacebook(playerFacebook.getId());
			
			// If yes, log the player in.
			if (player != null) {
				request.getSession(true);
				updateCurrentAuthenticateUser(player);
				loginService.registerLogin(player);
			} else {
				// If not, verify if the playerFacebook has an email.
				if (playerFacebook.getEmail() != null && !playerFacebook.getEmail().equals("")) {
					
					// If it is here it is because playerFacebook doesn't exist in the system AND has an email.
			
					player = playerService.findByEmail(playerFacebook.getEmail());
					
					// If the playerFacebook's email is already registered in the system.
					if (player != null) {
						// If it is here it is because the playerFacebook's email is already registered in the system, but it is not linked to any Facebook account.
						// Let's create the link and register in the database.
						player.setIdFacebook(playerFacebook.getId());
						player.setIndEmailConfirmed(true);
						
						playerService.save(player);
						
						request.getSession(true);
						updateCurrentAuthenticateUser(player);
						loginService.registerLogin(player);
					} else {
						// If it is here it is because it is necessary to register the player in the system.
						player = new Player();
						
						player.setIdFacebook(playerFacebook.getId());
						player.setName(playerFacebook.getFirstName());
						player.setLastName(playerFacebook.getLastName());
						player.setEmail(playerFacebook.getEmail());
						player.setPassword(new RandomStringUtil(6).nextString());
						player.setIndEmailConfirmed(true);
						
						playerService.create(player);
						
						request.getSession(true);
						updateCurrentAuthenticateUser(player);
						loginService.registerLogin(player);
					}
				} else {
					// If it is here it is because the playerFacebook doesn't exist in the system AND E doesn't have an email.
					// In this case, the user will not have neither an email nor login. He will always log in with his Facebook account.
					
					player = new Player();
					
					player.setIdFacebook(playerFacebook.getId());
					player.setName(playerFacebook.getFirstName());
					player.setLastName(playerFacebook.getLastName());
					player.setPassword(new RandomStringUtil(6).nextString());
					player.setLogin(playerFacebook.getId()+"");
					player.setIndEmailConfirmed(true);
					
					playerService.create(player);
					
					request.getSession(true);
					updateCurrentAuthenticateUser(player);
					loginService.registerLogin(player);
				}
			}
			
			return REDIRECT_HOME;
		}
		catch(MalformedURLException e) {
			model.addAttribute("player", new Player());
			model.addAttribute("codRegister", 2);
			// TODO - buscar msg do messages.
			model.addAttribute("msgRegister", "<br />Ocorreu algum erro ao utilizar sua conta do Facebook.");
			System.out.println("MalformedURLException - loginfb - Ocorreu algum erro ao utilizar sua conta do Facebook.");
			return URL_INDEX;
		}
		catch(IOException e) {
			model.addAttribute("player", new Player());
			model.addAttribute("codRegister", 2);
			// TODO - buscar msg do messages.
			model.addAttribute("msgRegister", "<br />Algum erro ocorreu ao utilizar sua conta do Facebook.");
			System.out.println("IOException - loginfb - Ocorreu algum erro ao utilizar sua conta do Facebook.");
			return URL_INDEX;
		}
	}
}