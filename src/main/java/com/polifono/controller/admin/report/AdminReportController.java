package com.polifono.controller.admin.report;

import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_REPORT_REGISTER;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polifono.model.entity.Player;
import com.polifono.model.form.admin.report.RegisterFilterForm;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/report")
public class AdminReportController {

    private final PlayerService playerService;

    @GetMapping("/register")
    public String register(Model model) {
        // Form
        model.addAttribute("registerFilterForm", new RegisterFilterForm());
        model.addAttribute("players", new ArrayList<Player>());
        return URL_ADMIN_REPORT_REGISTER;
    }

    @PostMapping("/register")
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
    private int getTotalFb(List<Player> players) {
        int totalFb = 0;

        for (Player p : players) {
            if (p.getIdFacebook() != null) {
                totalFb++;
            }
        }

        return totalFb;
    }
}
