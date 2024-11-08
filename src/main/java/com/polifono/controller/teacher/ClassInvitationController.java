package com.polifono.controller.teacher;

import static com.polifono.common.TemplateConstants.REDIRECT_CLASS_INVITATION;
import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_CLASS_INVITATION;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ClassInvitationController {

    private final SecurityService securityService;
    private final IClassPlayerService classPlayerService;

    @GetMapping("/classinvitation")
    public String classInvitation(final Model model) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Get all the invitation to classes that the student hasn't confirmed his participation yet.
        List<ClassPlayer> classPlayers = classPlayerService.findAllByStudentIdAndStatus(currentUser.get().getUser().getId(), 1);
        model.addAttribute("classPlayers", classPlayers);
        return URL_CLASS_INVITATION;
    }

    @GetMapping("/classinvitation/{id}")
    public String classInvitationSubmit(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes) {
        try {
            Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

            if (currentUser.isEmpty()) {
                return REDIRECT_HOME;
            }

            Optional<ClassPlayer> current = classPlayerService.findById(id.intValue());

            if (current.isEmpty()) {
                return REDIRECT_HOME;
            }

            // Verifying if the student logged in is not the player of this classPlayer.
            if (current.get().getPlayer().getId() != currentUser.get().getUser().getId()) {
                return REDIRECT_HOME;
            }

            // If the player has already confirmed his participation in this class.
            if (current.get().getStatus() != 1) {
                return URL_CLASS_INVITATION;
            }

            if (classPlayerService.changeStatus(id.intValue(), 2)) {
                redirectAttributes.addFlashAttribute("message", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "unsuccess");
            }
        } catch (Exception e) {
            log.error("Error in the classInvitationSubmit method. {}", e.getMessage());
        }

        return REDIRECT_CLASS_INVITATION;
    }
}
