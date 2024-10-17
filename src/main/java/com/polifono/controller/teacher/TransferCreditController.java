package com.polifono.controller.teacher;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.REDIRECT_TEACHER_CREDIT;
import static com.polifono.common.TemplateConstants.URL_TEACHER_CREDIT_INDEX;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.form.teacher.TransferCreditGroupForm;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IClassService;
import com.polifono.service.IGameService;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TransferCreditController {

    private final SecurityService securityService;
    private final IGameService gameService;
    private final IPlayerService playerService;
    private final IClassService classService;
    private final IPlayerGameService playerGameService;
    private final IClassPlayerService classPlayerService;

    @GetMapping("/credit")
    public String transferCredit(Model model) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        model.addAttribute("playerGame", new PlayerGame());
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("transferCreditGroupForm", new TransferCreditGroupForm());
        model.addAttribute("classes", classService.findByTeacherAndStatus(currentUser.get().getUser().getId(), true));

        return URL_TEACHER_CREDIT_INDEX;
    }

    @PostMapping("/credit/individual")
    public String transferCreditIndividual(@ModelAttribute("playerGame") PlayerGame playerGame, final RedirectAttributes redirectAttributes) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            // If the student's email was not informed.
            if (playerGame.getPlayer() == null || playerGame.getPlayer().getEmail() == null || playerGame.getPlayer().getEmail().isEmpty()) {
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
                    return REDIRECT_TEACHER_CREDIT;
                }
            }

            // The quantity of credits must be between 1 and 30.
            if (playerGame.getCredit() < 1 || playerGame.getCredit() > 30) {
                redirectAttributes.addFlashAttribute("message", "creditsBetweenXandY");
                return REDIRECT_TEACHER_CREDIT;
            }

            // Check if the logged player has enough credits to do this transaction.
            Optional<Player> playerLogged = playerService.findById(currentUser.get().getUser().getId());

            // If the player is not found.
            if (playerLogged.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                return REDIRECT_TEACHER_CREDIT;
            }

            if (playerLogged.get().getCredit() < playerGame.getCredit()) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                // Update session user.
                securityService.updateCurrentAuthenticatedUser(playerLogged.get());
                return REDIRECT_TEACHER_CREDIT;
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
            playerLogged = Optional.ofNullable(playerService.removeCreditsFromPlayer(playerLogged.get().getId(), playerGame.getCredit()));

            // If the player is not found.
            if (playerLogged.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                return REDIRECT_TEACHER_CREDIT;
            }

            // Add specific credits to the student.
            playerGameService.save(playerGameExistent);

            // Update session user.
            securityService.updateCurrentAuthenticatedUser(playerLogged.get());

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_TEACHER_CREDIT;
    }

    @PostMapping("/credit/group")
    public String transferCreditGroup(@ModelAttribute("transferCreditGroupForm") TransferCreditGroupForm transferCreditGroupForm,
            final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

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
                return REDIRECT_TEACHER_CREDIT;
            }

            // Get all the students of the class selected.
            List<ClassPlayer> classPlayerList = classPlayerService.findAllByClassIdAndStatus(transferCreditGroupForm.getClazz().getId(), 2);

            // Get the number of students confirmed (2) in the class.
            int numberOfStudents = classPlayerList.size();

            if (numberOfStudents == 0) {
                redirectAttributes.addFlashAttribute("message", "classEmpty");
                return REDIRECT_TEACHER_CREDIT;
            }

            int totalCredits = transferCreditGroupForm.getCredit() * numberOfStudents;

            // Check if the logged player has enough credits to do this transaction.
            Optional<Player> playerLogged = playerService.findById(currentUser.get().getUser().getId());

            // If the player is not found.
            if (playerLogged.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                return REDIRECT_TEACHER_CREDIT;
            }

            if (playerLogged.get().getCredit() < totalCredits) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                // Update session user.
                securityService.updateCurrentAuthenticatedUser(playerLogged.get());
                return REDIRECT_TEACHER_CREDIT;
            }

            // Remove generic credits from the teacher.
            playerLogged = Optional.ofNullable(playerService.removeCreditsFromPlayer(playerLogged.get().getId(), totalCredits));

            // If the player is not found.
            if (playerLogged.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "creditsInsufficient");
                return REDIRECT_TEACHER_CREDIT;
            }

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
            securityService.updateCurrentAuthenticatedUser(playerLogged.get());

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_TEACHER_CREDIT;
    }
}
