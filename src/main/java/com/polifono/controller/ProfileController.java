package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.polifono.domain.Diploma;
import com.polifono.domain.Game;
import com.polifono.domain.Login;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Transaction;
import com.polifono.domain.enums.Role;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.ILevelService;
import com.polifono.service.ILoginService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.ITransactionService;

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
	
	@Autowired
	private IDiplomaService diplomaService;
	
	@Autowired
	private ILevelService levelService;
	
	@Autowired
	private ITransactionService transactionService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
	
	public static final String URL_PROFILE_PROFILENOTFOUND = "profile/profileNotFound";
	public static final String URL_PROFILE_PROFILEPLAYER = "profile/profilePlayer";
	public static final String URL_PROFILE_PROFILEPLAYEREDIT = "profile/profilePlayerEdit";
	public static final String URL_PROFILE_PROFILESCORE = "profile/profileScore";
	public static final String URL_PROFILE_PROFILESCORE_OWNER = "profile/profileScoreOwner";
	public static final String URL_PROFILE_PROFILEATTENDANCE = "profile/profileAttendance";
	public static final String URL_PROFILE_PROFILEATTENDANCE_OWNER = "profile/profileAttendanceOwner";
	public static final String URL_PROFILE_PROFILECREDITS = "profile/profileCredits";
	public static final String URL_PROFILE_PROFILEVIDEOS = "profile/profileVideos";
	
	public static final String REDIRECT_HOME = "redirect:/";

	@RequestMapping(value = {"/player/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayer(final Model model, @PathVariable("playerId") Integer playerId) {
		
		Player player = playerService.findOne(playerId);
		
		if (player == null) return URL_PROFILE_PROFILENOTFOUND;
		
		// If the user is logged AND (he is accessing his own profile OR he is an admin).
		if (
				this.currentAuthenticatedUser() != null 
				&& 
				(
						currentAuthenticatedUser().getUser().getId() == playerId
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
		
		List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(player.getId());
		
		if (playerPhases == null) {
			playerPhases = new ArrayList<PlayerPhase>();
		}
		
		List<Diploma> diplomas = diplomaService.findByPlayer(player.getId());
		
		if (diplomas == null) {
			diplomas = new ArrayList<Diploma>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("phases", phases);
		model.addAttribute("playerPhases", playerPhases);
		model.addAttribute("diplomas", diplomas);
		
		return URL_PROFILE_PROFILEPLAYER;
	}
	
	@RequestMapping(value = {"/player/{playerId}/score"}, method = RequestMethod.GET)
	public final String score(final Model model, @PathVariable("playerId") Integer playerId) {
		
		Player player = playerService.findOne(playerId);
		
		if (player == null) return URL_PROFILE_PROFILENOTFOUND;
		
		// If the player logged in is not the player Id && is not ADMIN and is not TEACHER.
		if (
				this.currentAuthenticatedUser().getUser().getId() != playerId &&
				!this.currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN) &&
				!this.currentAuthenticatedUser().getUser().getRole().equals(Role.TEACHER)
			) {
			return REDIRECT_HOME;
		}

		// The teacher only can see his own page and of his students.
		if (this.currentAuthenticatedUser().getUser().getId() != playerId && 
				this.currentAuthenticatedUser().getUser().getRole().equals(Role.TEACHER)) {
			
			if (!classPlayerService.isMyStudent(currentAuthenticatedUser().getUser(), player)) {
				return REDIRECT_HOME;
			}
		}
		
		List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(player.getId());
		List<Game> playerPhasesGames = new ArrayList<Game>();
		
		if (playerPhases == null) {
			playerPhases = new ArrayList<PlayerPhase>();
		}
		else {
			playerPhasesGames = playerPhaseService.filterPlayerPhasesListByGame(playerPhases);
		}
		
		model.addAttribute("player", player);
		model.addAttribute("playerPhases", playerPhases);
		model.addAttribute("playerPhasesGames", playerPhasesGames);
		model.addAttribute("levels", levelService.findAll());
		
		// Students can see his own grades, but in a different page.
		if (this.currentAuthenticatedUser().getUser().getRole().equals(Role.USER)
				|| this.currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN)) {
			
			model.addAttribute("editAvailable", true);
			
			return URL_PROFILE_PROFILESCORE_OWNER;
		}
		else {
			return URL_PROFILE_PROFILESCORE;
		}
	}
	
	@RequestMapping(value = {"/player/{playerId}/attendance"}, method = RequestMethod.GET)
	public final String attendance(final Model model, @PathVariable("playerId") Integer playerId) {
		
		Player player = playerService.findOne(playerId);
		
		if (player == null) return URL_PROFILE_PROFILENOTFOUND;
		
		// If the player logged in is not the player Id && is not ADMIN and is not TEACHER.
		if (
				currentAuthenticatedUser().getUser().getId() != playerId &&
				!currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN) &&
				!currentAuthenticatedUser().getUser().getRole().equals(Role.TEACHER)
			) {
			return REDIRECT_HOME;
		}
		
		// The teacher only can see his own page and of his students.
		if (currentAuthenticatedUser().getUser().getId() != playerId && 
				currentAuthenticatedUser().getUser().getRole().equals(Role.TEACHER)) {
			
			if (!classPlayerService.isMyStudent(currentAuthenticatedUser().getUser(), player)) {
				return REDIRECT_HOME;
			}
		}
		
		List<Login> logins = loginService.findByPlayer(player.getId());
		
		if (logins == null) {
			logins = new ArrayList<Login>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("logins", logins);
		
		// Students can see his own attendances, but in a different page.
		if (this.currentAuthenticatedUser().getUser().getRole().equals(Role.USER)
				|| this.currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN)) {
			
			model.addAttribute("editAvailable", true);
			
			return URL_PROFILE_PROFILEATTENDANCE_OWNER;
		}
		else {
			return URL_PROFILE_PROFILEATTENDANCE;
		}
	}
	
	@RequestMapping(value = {"/player/{playerId}/credits"}, method = RequestMethod.GET)
	public final String credits(final Model model, @PathVariable("playerId") Integer playerId) {
		
		Player player = playerService.findOne(playerId);
		
		if (player == null) return URL_PROFILE_PROFILENOTFOUND;
		
		// If the player logged in is not the player Id && is not ADMIN.
		if (
				currentAuthenticatedUser().getUser().getId() != playerId &&
				!currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN)
			) {
			return REDIRECT_HOME;
		}
		
		List<Transaction> transactions = transactionService.findByPlayerAndStatus(player, 3);
		
		if (transactions == null) {
			transactions = new ArrayList<Transaction>();
		}
		
		model.addAttribute("player", player);
		model.addAttribute("transactions", transactions);
		model.addAttribute("editAvailable", true);
		
		return URL_PROFILE_PROFILECREDITS;
	}
	
	@RequestMapping(value = {"/player/{playerId}/videos"}, method = RequestMethod.GET)
	public final String videos(final Model model, @PathVariable("playerId") Integer playerId) {
		
		Player player = playerService.findOne(playerId);
		
		if (player == null) return URL_PROFILE_PROFILENOTFOUND;
		
		
		// If the player logged in is not the playerId && is not ADMIN.
		if (
				currentAuthenticatedUser().getUser().getId() != playerId &&
				!currentAuthenticatedUser().getUser().getRole().equals(Role.ADMIN)
			) {
			model.addAttribute("editAvailable", false);
		} else {
			model.addAttribute("editAvailable", true);
		}
		
		List<Phase> videos = new ArrayList<Phase>();
		
		model.addAttribute("player", player);
		model.addAttribute("videos", videos);
		
		
		return URL_PROFILE_PROFILEVIDEOS;
	}
	
	@RequestMapping(value = {"/player/edit/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayerEdit(final Model model, @PathVariable("playerId") Integer playerId) {
		
		// Verify if the playerId belongs to the player logged OR the user logged is an admin.
		if (this.currentAuthenticatedUser().getUser().getId() == playerId || this.currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			Player player = playerService.findOne(playerId);
			
			if (player == null) return URL_PROFILE_PROFILENOTFOUND;
			
			model.addAttribute("player", player);
		}
		else {
			LOGGER.debug("Someone tried to edit another player with a different id.");
			return REDIRECT_HOME;
		}

		return URL_PROFILE_PROFILEPLAYEREDIT;
	}

	@RequestMapping(value = "/player/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("edit") Player edit, final RedirectAttributes redirectAttributes) {

		// The player only can edit his own profile.
		// The admin can edit all the profiles.
		if (this.currentAuthenticatedUser().getUser().getId() != edit.getId() 
				&& !this.currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			return REDIRECT_HOME;
		}

		String msg = playerService.validateUpdateProfile(edit);
		
		if (!"".equals(msg)) {
			redirectAttributes.addFlashAttribute("message", "error");
			redirectAttributes.addFlashAttribute("messageContent", msg);
			return "redirect:/profile/player/edit/" + edit.getId();
		}
		
		Player player = playerService.findOne(edit.getId());
		
		try  {
			player.setName(edit.getName().trim());
			player.setLastName(edit.getLastName().trim());
			player.setPhone(edit.getPhone());
			player.setSex(edit.getSex());
			player.setDtBirth(edit.getDtBirth());
			player.setAddress(edit.getAddress());
			
			if (edit.getAbout() != null && edit.getAbout().length() > 500) {
				player.setAbout(edit.getAbout().substring(0, 499));
			}
			else {
				player.setAbout(edit.getAbout());
			}
			
			playerService.save(player);
			redirectAttributes.addFlashAttribute("edit", "success");
			
			// If player logged is editing his own profile.
			if (currentAuthenticatedUser().getUser().getId() == player.getId()) {
				// Update the currentAuthenticateUser
				this.updateCurrentAuthenticateUser(player);
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		
		return "redirect:/profile/player/" + edit.getId();
	}
}