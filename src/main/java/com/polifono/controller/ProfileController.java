package com.polifono.controller;

import java.util.ArrayList;
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
import com.polifono.domain.Login;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.ILoginService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private IPhaseService phaseService;
	
	@Autowired
	private IPlayerPhaseService playerPhaseService;
	
	@Autowired
	private IClassPlayerService classPlayerService;
	
	@Autowired
	private ILoginService loginService;

	@RequestMapping(value = {"/player/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayer(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		Player player = playerService.findOne(Integer.parseInt(playerId));
		
		if (player == null) {
			return "profile/profileNotFound";
		}
		
		// If the user is logged AND (he is accessing his own profile OR he is an admin).
		if (
				currentAuthenticatedUser() != null 
				&& 
				(
						currentAuthenticatedUser().getUser().getId() == Integer.parseInt(playerId)
						||
						currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")
				)
			) {
			
			model.addAttribute("editAvailable", true);
			
			if (currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
				model.addAttribute("deleteAvailable", true);
			}
			else {
				model.addAttribute("deleteAvailable", false);
			}
		}
		else {
			model.addAttribute("editAvailable", false);
			model.addAttribute("deleteAvailable", false);
		}
		
		List<Phase> phases = phaseService.findGamesForProfile(player.getId());
		
		if (phases == null) {
			phases = new ArrayList<Phase>();
		}
		
		List<PlayerPhase> playerPhases = playerPhaseService.findPlayerPhaseByPlayer(player);
		
		if (playerPhases == null) {
			playerPhases = new ArrayList<PlayerPhase>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("phases", phases);
		model.addAttribute("playerPhases", playerPhases);
		
		return "profile/profilePlayer";
	}
	
	@RequestMapping(value = {"/player/{playerId}/score"}, method = RequestMethod.GET)
	public final String score(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		Player player = playerService.findOne(Integer.parseInt(playerId));
		
		if (player == null) {
			return "profile/profileNotFound";
		}
		
		// If the player logged in is not the player Id && is not ADMIN and is not TEACHER.
		if (
				currentAuthenticatedUser().getUser().getId() != Integer.parseInt(playerId) &&
				!currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN") &&
				!currentAuthenticatedUser().getUser().getRole().toString().equals("TEACHER")
			) {
			return "redirect:/";
		}
		
		// The teacher only can see his own page and of his students.
		if (currentAuthenticatedUser().getUser().getId() != Integer.parseInt(playerId) && 
				currentAuthenticatedUser().getUser().getRole().toString().equals("TEACHER")) {
			if (!isMyStudent(currentAuthenticatedUser().getUser(), player)) {
				return "redirect:/";
			}
		}
		
		List<PlayerPhase> playerPhases = playerPhaseService.findPlayerPhaseByPlayer(player);
		
		if (playerPhases == null) {
			playerPhases = new ArrayList<PlayerPhase>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("playerPhases", playerPhases);
		
		return "profile/profileScore";
	}
	
	@RequestMapping(value = {"/player/{playerId}/attendance"}, method = RequestMethod.GET)
	public final String attendance(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		Player player = playerService.findOne(Integer.parseInt(playerId));
		
		if (player == null) {
			return "profile/profileNotFound";
		}
		
		// If the player logged in is not the player Id && is not ADMIN and is not TEACHER.
		if (
				currentAuthenticatedUser().getUser().getId() != Integer.parseInt(playerId) &&
				!currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN") &&
				!currentAuthenticatedUser().getUser().getRole().toString().equals("TEACHER")
			) {
			return "redirect:/";
		}
		
		// The teacher only can see his own page and of his students.
		if (currentAuthenticatedUser().getUser().getId() != Integer.parseInt(playerId) && 
				currentAuthenticatedUser().getUser().getRole().toString().equals("TEACHER")) {
			if (!isMyStudent(currentAuthenticatedUser().getUser(), player)) {
				return "redirect:/";
			}
		}
		
		List<Login> logins = loginService.findByPlayer(player.getId());
		
		if (logins == null) {
			logins = new ArrayList<Login>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("logins", logins);
		
		return "profile/profileAttendance";
	}
	
	@RequestMapping(value = {"/player/edit/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayerEdit(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}

		// Verify if the playerId belongs to the player logged OR the user logged is an admin.
		if (currentAuthenticatedUser().getUser().getId() == Integer.parseInt(playerId) || currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			Player player = playerService.findOne(Integer.parseInt(playerId));
			
			if (player == null) {
				return "profile/profileNotFound";
			}
			
			model.addAttribute("player", player);
		}
		else {
			return "redirect:/";
		}
		
		
		return "profile/profilePlayerEdit";
	}
	
	@RequestMapping(value = "/player/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("edit") Player edit, final RedirectAttributes redirectAttributes) {

		// The player only can edit his own profile.
		// The admin can edit all the profiles.
		if (currentAuthenticatedUser().getUser().getId() != edit.getId() 
				&& !currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			return "redirect:/";
		}

		String msg = validateEditProfile(edit);
		
		if (!"".equals(msg)) {
			redirectAttributes.addFlashAttribute("message", "error");
			redirectAttributes.addFlashAttribute("messageContent", msg);
			return "redirect:/profile/player/edit/" + edit.getId();
		}
		
		Player player = playerService.findOne(edit.getId());
		
		try  {
			player.setName(edit.getName());
			player.setPhone(edit.getPhone());
			player.setSex(edit.getSex());
			player.setDtBirth(edit.getDtBirth());
			player.setAddress(edit.getAddress());
			
			playerService.save(player);
			redirectAttributes.addFlashAttribute("edit", "success");
			
			// If player logged is editing his own profile.
			if (currentAuthenticatedUser().getUser().getId() == player.getId()) {
				// Update the currentAuthenticateUser
				updateCurrentAuthenticateUser(player);
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		
		return "redirect:/profile/player/" + edit.getId();
	}
	
	/**
	 * Method used to see if student is student of teacher in any class.
	 * Return true if student is student of teacher.
	 * 
	 * @param teacher
	 * @param student
	 * @return
	 */
	public boolean isMyStudent(Player teacher, Player student) {
		List<ClassPlayer> classPlayers = classPlayerService.findByTeacherAndStudent(teacher.getId(), student.getId());
		
		if (classPlayers != null && classPlayers.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public String validateEditProfile(Player player) {
		String msg = "";
		
		if (player.getName() == null || "".equals(player.getName())) {
			msg = "O nome precisa ser informado.<br />";
		}
		
		return msg;
	}
}