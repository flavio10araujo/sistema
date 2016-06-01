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

import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.service.PhaseService;
import com.polifono.service.PlayerPhaseService;
import com.polifono.service.PlayerService;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private PhaseService phaseService;
	
	@Autowired
	private PlayerPhaseService playerPhaseService;

	@RequestMapping(value = {"/player/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayer(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		Player player = playerService.getPlayer(Integer.parseInt(playerId));
		
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
		
		List<Phase> phases = phaseService.findGamesForProfile(player);
		
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
	
	@RequestMapping(value = {"/player/edit/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayerEdit(final Model model, @PathVariable("playerId") String playerId) {
		
		List<String> intParameters = new ArrayList<String>();
		intParameters.add(playerId);
		
		if (playerId == null || !isParameterInteger(intParameters)) {
			return "redirect:/";
		}

		// Verify if the playerId belongs to the player logged OR the user logged is an admin.
		if (currentAuthenticatedUser().getUser().getId() == Integer.parseInt(playerId) || currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			Player player = playerService.getPlayer(Integer.parseInt(playerId));
			
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
		
		Player playerOld = playerService.getPlayer(edit.getId());
		
		try  {
			edit.setDtInc(playerOld.getDtInc());
			edit.setRole(playerOld.getRole());
			edit.setPassword(playerOld.getPassword());
			edit.setCredit(playerOld.getCredit());
			edit.setScore(playerOld.getScore());
			
			playerService.save(edit);
			redirectAttributes.addFlashAttribute("edit", "success");
			
			// If player logged is editing his own profile.
			if (currentAuthenticatedUser().getUser().getId() == edit.getId()) {
				// Update the currentAuthenticateUser
				updateCurrentAuthenticateUser(edit);
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		
		return "redirect:/profile/player/" + edit.getId();
	}
}