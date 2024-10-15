package com.polifono.controller.teacher;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_TEACHER_PLAYER_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.service.IPlayerService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TeacherPlayerController {

    private final SecurityService securityService;
    private final IPlayerService playerService;

    @GetMapping({ "/player", "/player/create" })
    public String indexPage(Model model) {
        model.addAttribute("player", new Player());
        return URL_TEACHER_PLAYER_INDEX;
    }

    @PostMapping("/player/create")
    public String createPlayer(final Model model, @ModelAttribute("player") Player player) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        if (player == null) {
            model.addAttribute("player", new Player());
            return URL_TEACHER_PLAYER_INDEX;
        }

        // Verify if the login is already in use.
        Player playerOld = playerService.findByLogin(player.getLogin());

        if (playerOld != null) {
            model.addAttribute("player", player);
            model.addAttribute("codRegister", 2);
            // TODO - buscar msg do messages.
            model.addAttribute("msgRegister", "<br />O login " + player.getLogin() + " já está cadastrado para outra pessoa.");
        } else {
            String msg = playerService.validateCreatePlayerByTeacher(player);

            // If there are no errors.
            if (msg.isEmpty()) {
                String name = player.getName().trim();
                name = name.substring(0, name.indexOf(" "));

                String lastName = player.getName().trim();
                lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();

                player.setLastName(lastName);
                player.setName(name);

                Player teacher = currentUser.get().getUser();
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

        return URL_TEACHER_PLAYER_INDEX;
    }
}
