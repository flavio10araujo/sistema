package com.polifono.controller.teacher;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_TEACHER_STUDENT;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_TEACHER_STUDENT_INDEX;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Class;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.service.ClassPlayerService;
import com.polifono.service.ClassService;
import com.polifono.service.SecurityService;
import com.polifono.service.SendEmailService;
import com.polifono.service.player.PlayerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/teacher")
public class StudentController {

    private final SecurityService securityService;
    private final ClassService classService;
    private final PlayerService playerService;
    private final ClassPlayerService classPlayerService;
    private final SendEmailService sendEmailService;

    @GetMapping({ "/student", "/student/savepage" })
    public String savePage(HttpSession session, Model model) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        model.addAttribute("classes", classService.findByTeacherAndStatus(currentUser.get().getUser().getId(), true));
        model.addAttribute("classPlayer", new ClassPlayer());

        if (session.getAttribute("clazzId") != null) {
            Class filterClass = new Class();
            filterClass.setId((int) session.getAttribute("clazzId"));
            model.addAttribute("classFilter", filterClass);

            model.addAttribute("classPlayers",
                    classPlayerService.findAllByClassIdAndTeacherId((int) session.getAttribute("clazzId"), currentUser.get().getUser().getId()));
        } else {
            model.addAttribute("classFilter", new Class());
        }

        return URL_TEACHER_STUDENT_INDEX;
    }

    @PostMapping("/student")
    public String setFilter(HttpSession session, @ModelAttribute("clazz") Class clazz) {
        if (clazz.getId() > 0) {
            session.setAttribute("clazzId", clazz.getId());
        } else {
            session.setAttribute("clazzId", null);
        }

        return REDIRECT_TEACHER_STUDENT;
    }

    @PostMapping("/student/save")
    public String save(@ModelAttribute("classPlayer") ClassPlayer classPlayer, final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            // If the student's email was not informed.
            if (classPlayer.getPlayer() == null || classPlayer.getPlayer().getEmail() == null || classPlayer.getPlayer().getEmail().isEmpty()) {
                throw new Exception();
            }

            // The teacher only can add players in his own classes.
            Optional<Class> currentClass = classService.findById(classPlayer.getClazz().getId());

            // If the class doesn't exist.
            if (currentClass.isEmpty()) {
                return REDIRECT_HOME;
            }

            if (currentClass.get().getPlayer().getId() != currentUser.get().getUser().getId()) {
                return REDIRECT_HOME;
            }

            String emailLogin = classPlayer.getPlayer().getEmail();

            // Get the player by his email.
            // Get the player only if he is active.
            classPlayer.setPlayer(playerService.findByEmailAndStatus(emailLogin, true).orElse(null));

            // If the email is not registered at the system.
            if (classPlayer.getPlayer() == null) {

                // Try to get the player by his login.
                classPlayer.setPlayer(playerService.findByLogin(emailLogin).orElse(null));

                // If the login is not registered at the system as well.
                if (classPlayer.getPlayer() == null) {
                    redirectAttributes.addFlashAttribute("message", "studentNotFound");
                    return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
                }
            }

            // Verify if the player is already registered in this class.
            List<ClassPlayer> classPlayerAux = classPlayerService.findAllByClassIdAndStudentId(classPlayer.getClazz().getId(), classPlayer.getPlayer().getId());

            if (classPlayerAux != null && !classPlayerAux.isEmpty()) {

                ClassPlayer classPlayerItem = classPlayerAux.get(0);

                // If the player is already registered, and he is not disabled.
                if (classPlayerItem.getStatus() != 3) {
                    redirectAttributes.addFlashAttribute("message", "studentAlreadyRegistered");
                }
                // If the player is already registered but is disabled.
                else {
                    classPlayerItem.setStatus(2);
                    classPlayerService.save(classPlayerItem);
                    redirectAttributes.addFlashAttribute("message", "studentWasDisabled");
                }

                return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
            }

            //Originalmente, o aluno era cadastrado como pendente e ele somente passaria a integrar a sala de aula após confirmar sua participação através de um e-mail recebido.
            //Fiz assim para impedir que o professor cadastrasse qualquer pessoa em suas salas.
            //Alterei posteriormente para facilitar o cadastro dos alunos nas salas. Futuramente devo criar algum mecanismo para o aluno saber que foi adicionado em uma sala
            //e poder sair dessa sala se ele quiser.
            classPlayerService.create(classPlayer);
            //EmailSendUtil.sendEmailInvitationToClass(currentAuthenticatedUser().getUser(), classPlayer);

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
    }

    @GetMapping("/student/resendemail/{id}")
    public String resendEmail(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            // The teacher only can send email to students from his own classes.
            Optional<ClassPlayer> current = classPlayerService.findById(id.intValue());

            // If the classPlayer doesn't exist.
            if (current.isEmpty())
                return REDIRECT_HOME;

            // Verifying if the teacher logged in is the owner of this class.
            if (current.get().getClazz().getPlayer().getId() != currentUser.get().getUser().getId())
                return REDIRECT_HOME;

            // Verifying if the student is not in the Pending status anymore.
            if (current.get().getStatus() != 1) {
                redirectAttributes.addFlashAttribute("message", "studentNotPending");
                return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
            }

            sendEmailService.sendEmailInvitationToClass(currentUser.get().getUser(), current.get());

            redirectAttributes.addFlashAttribute("message", "emailSent");
        } catch (Exception e) {
            log.error("Error sending email to student: {}", e.getMessage());
        }

        return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
    }

    @GetMapping("/student/{operation}/{id}")
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            // The teacher only can edit/delete his own classes.
            Optional<ClassPlayer> current = classPlayerService.findById(id.intValue());

            // If the classPlayer doesn't exist.
            if (current.isEmpty()) {
                return REDIRECT_HOME;
            }

            // Verifying if the teacher logged in is the owner of this class.
            if (current.get().getClazz().getPlayer().getId() != currentUser.get().getUser().getId()) {
                return REDIRECT_HOME;
            }

            if (operation.equals("delete")) {
                if (classPlayerService.delete(id.intValue())) {
                    redirectAttributes.addFlashAttribute("deletion", "success");
                } else {
                    redirectAttributes.addFlashAttribute("deletion", "unsuccess");
                }
            }
        } catch (Exception e) {
            log.error("Error deleting student: {}", e.getMessage());
        }

        return REDIRECT_TEACHER_STUDENT_SAVE_PAGE;
    }
}
