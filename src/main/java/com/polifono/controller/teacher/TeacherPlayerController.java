package com.polifono.controller.teacher;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.controller.BaseController;
import com.polifono.domain.Player;
import com.polifono.service.IPlayerService;

@Controller
@RequestMapping("/teacher")
public class TeacherPlayerController extends BaseController {

	public static final String URL_ADMIN_BASIC_INDEX = "teacher/player/index";
	public static final String URL_ADMIN_BASIC_EDIT = "teacher/player/editPage";
	public static final String URL_ADMIN_BASIC_SAVEPAGE = "teacher/player/savepage";
	
	public static final String REDIRECT_HOME = "redirect:/";
	
	@Autowired
	private IPlayerService playerService;
	
	@RequestMapping(value = {"/player", "/player/create"}, method = RequestMethod.GET)
	public String indexPage(HttpSession session, Model model) {
		model.addAttribute("player", new Player());
		
		return URL_ADMIN_BASIC_INDEX;
	}
	
	@RequestMapping(value = {"/player/create"}, method = RequestMethod.POST)
	public final String createPlayer(final Model model, @ModelAttribute("player") Player player) {
		
		if (player == null) {
			model.addAttribute("player", new Player());
			return URL_ADMIN_BASIC_INDEX;
		}
		
		// Verify if the login is already in use.
		Player playerOld = playerService.findByLogin(player.getLogin());
		
		if (playerOld != null) {
			model.addAttribute("player", player);
			model.addAttribute("codRegister", 2);
			// TODO - buscar msg do messages.
			model.addAttribute("msgRegister", "<br />O login " + player.getLogin() + " já está cadastrado para outra pessoa.");
		}
		else {
			String msg = playerService.validateCreatePlayerByTeacher(player);
			
			// If there are not errors.
			if (msg.equals("")) {
				Player teacher = this.currentAuthenticatedUser().getUser();
				player.setCreator(teacher);
				model.addAttribute("player", playerService.create(player));
				model.addAttribute("codRegister", 1);
			}
			// If there are errors.
			else {
				model.addAttribute("player", player);
				model.addAttribute("codRegister", 2);
				model.addAttribute("msgRegister", msg);
			}
		}
		
		return URL_ADMIN_BASIC_INDEX;
	}
}