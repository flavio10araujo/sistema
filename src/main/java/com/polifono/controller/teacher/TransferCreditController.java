package com.polifono.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.controller.BaseController;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.service.IGameService;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;

@Controller
@RequestMapping("/teacher")
public class TransferCreditController extends BaseController {

	public static final String URL_TEACHER_CREDIT = "teacher/credit";
	
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private IPlayerGameService playerGameService;

	@RequestMapping(value = {"/credit"}, method = RequestMethod.GET)
	public String transferCredit(Model model) {
		model.addAttribute("games", gameService.findAll());
		model.addAttribute("playerGame", new PlayerGame());

		return "teacher/credit/index";
	}

	@RequestMapping(value = {"/credit/individual"}, method = RequestMethod.POST)
	public String transferCreditIndividual(@ModelAttribute("playerGame") PlayerGame playerGame, final RedirectAttributes redirectAttributes) {

		try {
			// If the student's email was not informed.
			if (playerGame.getPlayer() == null || playerGame.getPlayer().getEmail() == null || "".equals(playerGame.getPlayer().getEmail())) {
				throw new Exception();
			}
			
			// If the game was not informed.
			if (playerGame.getGame() == null || playerGame.getGame().getId() == 0) {
				throw new Exception();
			}
	
			String emailLogin = playerGame.getPlayer().getEmail();
	
			// Get the player by his email.
			// Get the player only if he is active.
			playerGame.setPlayer(playerService.findByEmailAndStatus(emailLogin, true));
	
			// If the email is not registered at the system.
			if (playerGame.getPlayer() == null) {
	
				// Try to get the player by his login.
				playerGame.setPlayer(playerService.findByLogin(emailLogin));
	
				// If the login is not registered at the system as well.
				if (playerGame.getPlayer() == null) {
					redirectAttributes.addFlashAttribute("message", "studentNotFound");
					return "redirect:/" + URL_TEACHER_CREDIT;
				}
			}
			
			// The quantity of credits must be between 1 and 30.
			if (playerGame.getCredit() < 1 || playerGame.getCredit() > 30) {
				redirectAttributes.addFlashAttribute("message", "creditsBetweenXandY");
				return "redirect:/" + URL_TEACHER_CREDIT;
			}
			
			// Check if the logged player has enough credits to do this transaction.
			Player playerLogged = playerService.findOne(this.currentAuthenticatedUser().getUser().getId());
			if (playerLogged.getCredit() < playerGame.getCredit()) {
				redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
				// Update session user.
				this.updateCurrentAuthenticateUser(playerLogged);
				return "redirect:/" + URL_TEACHER_CREDIT;
			}
			
			// Check if the student already has specific credits for this game.
			PlayerGame playerGameExistent = playerGameService.findByPlayerAndGame(playerGame.getPlayer().getId(), playerGame.getGame().getId());

			if (playerGameExistent == null) {
				playerGameExistent = new PlayerGame();
				playerGameExistent.setPlayer(playerGame.getPlayer());
				playerGameExistent.setGame(playerGame.getGame());
			}
			
			playerGameExistent.setCredit(playerGameExistent.getCredit() + playerGame.getCredit());
			
			// Remove generic credits from the teacher.
			playerLogged = playerService.removeCreditsFromPlayer(playerLogged.getId(), playerGame.getCredit());
			
			// Add specific credits to the student.
			playerGameService.save(playerGameExistent);
			
			// Update session user.
			this.updateCurrentAuthenticateUser(playerLogged);
			
			redirectAttributes.addFlashAttribute("save", "success");
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("save", "unsuccess");
		}

		return "redirect:/" + URL_TEACHER_CREDIT;
	}
}