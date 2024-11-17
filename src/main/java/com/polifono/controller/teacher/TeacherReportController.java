package com.polifono.controller.teacher;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_TEACHER_REPORT_INDEX;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polifono.model.CurrentUser;
import com.polifono.model.dto.teacher.ReportGeneralDTO;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.form.teacher.ReportGeneralForm;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.impl.ClassPlayerService;
import com.polifono.service.impl.ClassService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.game.GameService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TeacherReportController {

    private final SecurityService securityService;
    private final GameService gameService;
    private final ClassService classService;
    private final ClassPlayerService classPlayerService;
    private final IPlayerPhaseService playerPhaseService;

    @GetMapping("/report")
    public String reportGeneral(Model model) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        model.addAttribute("games", gameService.findAll());
        model.addAttribute("classes", classService.findByTeacherAndStatus(currentUser.get().getUser().getId(), true));
        // Form
        model.addAttribute("reportGeneralForm", new ReportGeneralForm());

        return URL_TEACHER_REPORT_INDEX;
    }

    @PostMapping("/report")
    public String reportGeneralSubmit(@ModelAttribute("reportGeneralForm") ReportGeneralForm reportGeneralForm, Model model) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        String msg = validateReportGeneral(reportGeneralForm);

        model.addAttribute("games", gameService.findAll());
        model.addAttribute("classes", classService.findByTeacherAndStatus(currentUser.get().getUser().getId(), true));
        // Form
        model.addAttribute("reportGeneralForm", reportGeneralForm);

        // If there is an error.
        if (!msg.isEmpty()) {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", msg);
            return URL_TEACHER_REPORT_INDEX;
        }

        List<ClassPlayer> classPlayers = classPlayerService.findAllByClassIdAndStatus(reportGeneralForm.getClazz().getId(), 2);
        List<ReportGeneralDTO> list = new ArrayList<>();

        for (ClassPlayer classPlayer : classPlayers) {
            list.add(new ReportGeneralDTO(reportGeneralForm.getPhaseBegin(), reportGeneralForm.getPhaseEnd(), classPlayer.getPlayer(),
                    playerPhaseService.findForReportGeneral(reportGeneralForm, classPlayer.getPlayer().getId())
            ));
        }

        model.addAttribute("reportGeneral", list);

        return URL_TEACHER_REPORT_INDEX;
    }

    private String validateReportGeneral(ReportGeneralForm reportGeneralForm) {
        String msg = "";

        if (reportGeneralForm.getGame() == null || reportGeneralForm.getGame().getId() <= 0) {
            msg = msg + "<br />O curso precisa ser informado.";
        }

        if (reportGeneralForm.getClazz() == null || reportGeneralForm.getClazz().getId() <= 0) {
            msg = msg + "<br />A classe precisa ser informada.";
        }

        if (reportGeneralForm.getPhaseBegin() <= 0) {
            msg = msg + "<br />A aula inicial precisa ser informada.";
        }

        if (reportGeneralForm.getPhaseEnd() <= 0) {
            msg = msg + "<br />A aula final precisa ser informada.";
        }

        if (reportGeneralForm.getPhaseBegin() > reportGeneralForm.getPhaseEnd()) {
            msg = msg + "<br />A aula inicial precisa ser menor ou igual à aula final.";
        }

        return msg;
    }
}
