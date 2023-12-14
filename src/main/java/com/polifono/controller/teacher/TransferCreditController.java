package com.polifono.controller.teacher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.controller.BaseController;
import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.form.teacher.TransferCreditGroupForm;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IClassService;
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
    private IClassService classService;

    @Autowired
    private IPlayerGameService playerGameService;

    @Autowired
    private IClassPlayerService classPlayerService;

    @RequestMapping(value = { "/credit" }, method = RequestMethod.GET)
    public String transferCredit(Model model) {
        model.addAttribute("playerGame", new PlayerGame());
        model.addAttribute("games", gameService.findAll());

        model.addAttribute("transferCreditGroupForm", new TransferCreditGroupForm());
        model.addAttribute("classes",
                (ArrayList<com.polifono.domain.Class>) classService.findByTeacherAndStatus(currentAuthenticatedUser().getUser().getId(), true));

        return "teacher/credit/index";
    }

    @RequestMapping(value = { "/credit/individual" }, method = RequestMethod.POST)
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
            Player playerLogged = playerService.findById(this.currentAuthenticatedUser().getUser().getId()).get();
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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_TEACHER_CREDIT;
    }

    @RequestMapping(value = { "/credit/group" }, method = RequestMethod.POST)
    public String transferCreditGroup(@ModelAttribute("transferCreditGroupForm") TransferCreditGroupForm transferCreditGroupForm,
            final RedirectAttributes redirectAttributes) {
        try {
            // If the class was not informed.
            if (transferCreditGroupForm.getClazz() == null || transferCreditGroupForm.getClazz().getId() == 0) {
                throw new Exception();
            }

            // If the game was not informed.
            if (transferCreditGroupForm.getGame() == null || transferCreditGroupForm.getGame().getId() == 0) {
                throw new Exception();
            }

            // The quantity of credits must be between 1 and 30.
            if (transferCreditGroupForm.getCredit() < 1 || transferCreditGroupForm.getCredit() > 30) {
                redirectAttributes.addFlashAttribute("message", "creditsBetweenXandY");
                return "redirect:/" + URL_TEACHER_CREDIT;
            }

            // Get all the students of the class selected.
            List<ClassPlayer> classPlayerList = classPlayerService.findByClassAndStatus(transferCreditGroupForm.getClazz().getId(), 2);

            // Get the number of students confirmed (2) in the class.
            int numberOfStudents = classPlayerList.size();

            if (numberOfStudents <= 0) {
                redirectAttributes.addFlashAttribute("message", "classEmpty");
                return "redirect:/" + URL_TEACHER_CREDIT;
            }

            int totalCredits = transferCreditGroupForm.getCredit() * numberOfStudents;

            // Check if the logged player has enough credits to do this transaction.
            Player playerLogged = playerService.findById(this.currentAuthenticatedUser().getUser().getId()).get();
            if (playerLogged.getCredit() < totalCredits) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                // Update session user.
                this.updateCurrentAuthenticateUser(playerLogged);
                return "redirect:/" + URL_TEACHER_CREDIT;
            }

            // Remove generic credits from the teacher.
            playerLogged = playerService.removeCreditsFromPlayer(playerLogged.getId(), totalCredits);

            PlayerGame playerGameExistent;

            // For each student in this class.
            for (ClassPlayer cp : classPlayerList) {
                // Check if the student already has specific credits for this game.
                playerGameExistent = playerGameService.findByPlayerAndGame(cp.getPlayer().getId(), transferCreditGroupForm.getGame().getId());

                if (playerGameExistent == null) {
                    playerGameExistent = new PlayerGame();
                    playerGameExistent.setPlayer(cp.getPlayer());
                    playerGameExistent.setGame(transferCreditGroupForm.getGame());
                }

                playerGameExistent.setCredit(playerGameExistent.getCredit() + transferCreditGroupForm.getCredit());

                // Add specific credits to the student.
                playerGameService.save(playerGameExistent);
            }

            // Update session user.
            this.updateCurrentAuthenticateUser(playerLogged);

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_TEACHER_CREDIT;
    }
}
