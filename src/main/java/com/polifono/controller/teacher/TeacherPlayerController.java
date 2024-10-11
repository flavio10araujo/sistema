package com.polifono.controller.teacher;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.controller.BaseController;
import com.polifono.domain.Player;
import com.polifono.service.IPlayerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TeacherPlayerController extends BaseController {

    public static final String URL_ADMIN_BASIC_INDEX = "teacher/player/index";

    private final IPlayerService playerService;

    @RequestMapping(value = { "/player", "/player/create" }, method = RequestMethod.GET)
    public String indexPage(HttpSession session, Model model) {
        model.addAttribute("player", new Player());
        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/player/create" }, method = RequestMethod.POST)
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
        } else {
            String msg = playerService.validateCreatePlayerByTeacher(player);

            // If there are not errors.
            if (msg.isEmpty()) {
                String name = player.getName().trim();
                name = name.substring(0, name.indexOf(" "));

                String lastName = player.getName().trim();
                lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();

                player.setLastName(lastName);
                player.setName(name);

                Player teacher = Objects.requireNonNull(this.currentAuthenticatedUser()).getUser();
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
