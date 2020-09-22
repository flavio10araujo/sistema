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
import com.polifono.domain.Level;
import com.polifono.domain.Login;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Playervideo;
import com.polifono.domain.Transaction;
import com.polifono.domain.enums.Role;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.ILoginService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IPlayervideoService;
import com.polifono.service.ITransactionService;
import com.polifono.util.StringUtil;

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
	private IGameService gameService;
	
	@Autowired
	private ILevelService levelService;
	
	@Autowired
	private ITransactionService transactionService;
	
	@Autowired
	private IPlayervideoService playervideoService;
	
	@Autowired
	private IContentService contentService;
	
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
	public static final String URL_PROFILE_PROFILEPLAYERADDVIDEO = "profile/profileVideosEdit";
	
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
		List<Transaction> transactions4 = transactionService.findByPlayerAndStatus(player, 4); // PagSeguro
		
		if (transactions == null) {
			transactions = new ArrayList<Transaction>();
		}
		
		if (transactions4 != null) {
			for (Transaction t : transactions4) {
				transactions.add(t);
			}
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
		
		model.addAttribute("player", player);
		
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
			player.setName(StringUtil.formatNamePlayer(edit.getName().trim() + " " + edit.getLastName().trim()));
			
			String name = player.getName();
			name = name.substring(0, name.indexOf(" "));
			
			String lastName = player.getName();
			lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();
			
			player.setLastName(lastName);
			player.setName(name);
			
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
	
	@RequestMapping(value = {"/player/addVideo/{playerId}"}, method = RequestMethod.GET)
	public final String profilePlayerAddVideo(final Model model, @PathVariable("playerId") Integer playerId) {
		
		// Verify if the playerId belongs to the player logged OR the user logged is an admin.
		if (this.currentAuthenticatedUser().getUser().getId() == playerId || this.currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			Player player = playerService.findOne(playerId);
			
			if (player == null) return URL_PROFILE_PROFILENOTFOUND;
			
			model.addAttribute("player", player);
			
			// Filter.
			model.addAttribute("games", (ArrayList<Game>) gameService.findByActive(true));
			model.addAttribute("levels", (ArrayList<Level>) levelService.findByActive(true));
			// Form
			Playervideo playervideo = new Playervideo();
			playervideo.setPlayer(player);
			model.addAttribute("playervideo", playervideo);
		}
		else {
			LOGGER.debug("Someone tried to add a video to another player with a different id.");
			return REDIRECT_HOME;
		}

		return URL_PROFILE_PROFILEPLAYERADDVIDEO;
	}
	
	@RequestMapping(value = "/player/addVideo", method = RequestMethod.POST)
	public String addVideo(final Model model, @ModelAttribute("playervideo") Playervideo playervideo, final RedirectAttributes redirectAttributes) {

		// The player only can add videos in his name.
		// The admin can add videos for everybody.
		if (this.currentAuthenticatedUser().getUser().getId() != playervideo.getPlayer().getId() 
				&& !this.currentAuthenticatedUser().getUser().getRole().toString().equals("ADMIN")) {
			return REDIRECT_HOME;
		}

		Player player = playerService.findOne(playervideo.getPlayer().getId());
		
		if (player == null) {
			return REDIRECT_HOME;
		} else {
			playervideo.setPlayer(player);
		}
		
		String msg = playerService.validateAddVideo(playervideo);
		
		// If no problems has been detected until now. 
		if ("".equals(msg)) {
			// The player cannot add a video in a phase that he hasn't finished yet.
			if (playerPhaseService.findByPlayerPhaseAndStatus(player.getId(), playervideo.getContent().getPhase().getId(), 3) == null) {
				msg = "<br />Só é permitido adicionar vídeos em aulas que o aluno já concluiu.";
			}
			// The player cannot add more than one video in the same phase.
			else if (playervideoService.findByPlayerAndPhase(player, playervideo.getContent().getPhase()) != null) {
				msg = "<br />Não é permitido adicionar mais de um vídeo para a mesma fase.";
			}
		}
		
		if (!"".equals(msg)) {
			// Msgs.
			model.addAttribute("message", "error");
			model.addAttribute("messageContent", msg);
			// Filter.
			model.addAttribute("games", (ArrayList<Game>) gameService.findByActive(true));
			model.addAttribute("levels", (ArrayList<Level>) levelService.findByActive(true));
			// Form.
			model.addAttribute("playervideo", playervideo);
			
			return URL_PROFILE_PROFILEPLAYERADDVIDEO;
		}
		
		try  {
			player.setScore(player.getScore() + 25);
			player.setCoin(player.getCoin() + 25);
			
			if (this.currentAuthenticatedUser().getUser().getId() == playervideo.getPlayer().getId()) {
				// Update session user.
				this.updateCurrentAuthenticateUser(player);
			}
			
			playervideo.setContent(contentService.findByPhaseAndOrder(playervideo.getContent().getPhase().getId(), 1));
			playervideoService.save(playervideo);
			
			redirectAttributes.addFlashAttribute("save", "success");
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("save", "unsuccess");
		}
		
		return "redirect:/profile/player/" + playervideo.getPlayer().getId() + "/videos";
	}
}