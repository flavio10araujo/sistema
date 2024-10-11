package com.polifono.controller.admin.report;

import static com.polifono.common.TemplateConstants.URL_ADMIN_REPORT_REGISTER;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.domain.Player;
import com.polifono.form.admin.report.RegisterFilterForm;
import com.polifono.service.IPlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/report")
public class AdminReportController {

    private final IPlayerService playerService;

    @RequestMapping(value = { "/register" }, method = RequestMethod.GET)
    public String register(Model model) {
        // Form
        model.addAttribute("registerFilterForm", new RegisterFilterForm());
        model.addAttribute("players", new ArrayList<Player>());
        return URL_ADMIN_REPORT_REGISTER;
    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerSubmit(@ModelAttribute("registerFilterForm") RegisterFilterForm registerFilterForm, Model model) {
        // Form
        model.addAttribute("registerFilterForm", registerFilterForm);

        if (registerFilterForm.getDateBegin() == null || registerFilterForm.getDateEnd() == null) {
            registerFilterForm.setDateBegin(new Date());
            registerFilterForm.setDateEnd(new Date());
        }

        List<Player> players = playerService.findByDateIncRange(registerFilterForm.getDateBegin(), registerFilterForm.getDateEnd());

        model.addAttribute("players", players);
        model.addAttribute("registerFilterFormTotal", players.size());
        model.addAttribute("registerFilterFormTotalFb", getTotalFb(players));

        return URL_ADMIN_REPORT_REGISTER;
    }

    /**
     * This method is just to verify how many players has a facebook id.
     */
    public int getTotalFb(List<Player> players) {
        int totalFb = 0;

        for (Player p : players) {
            if (p.getIdFacebook() != null) {
                totalFb++;
            }
        }

        return totalFb;
    }
}
