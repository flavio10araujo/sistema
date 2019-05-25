package com.polifono.controller.admin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.domain.Player;
import com.polifono.form.admin.report.RegisterFilterForm;
import com.polifono.service.IPlayerService;

@Controller
@RequestMapping("/admin/report")
public class AdminReportController {
	
	public static final String URL_ADMIN_REPORT_REGISTER = "admin/report/register";
	
	@Autowired
	private IPlayerService playerService;
	
	@RequestMapping(value = {"/register"}, method = RequestMethod.GET)
	public String register(Model model) {
		
		// Form
		model.addAttribute("registerFilterForm", new RegisterFilterForm());
		
		model.addAttribute("players", new ArrayList<Player>());
		
		return URL_ADMIN_REPORT_REGISTER;
	}
	
	@RequestMapping(value = {"/register"}, method = RequestMethod.POST)
	public String registerSubmit(@ModelAttribute("registerFilterForm") RegisterFilterForm registerFilterForm, Model model) {
		
		String msg = validateReportRegister(registerFilterForm);
		
		// Form
		model.addAttribute("registerFilterForm", registerFilterForm);
		
		// If there is an error.
		if (!msg.equals("")) {
			model.addAttribute("message", "error");
			model.addAttribute("messageContent", msg);
			return URL_ADMIN_REPORT_REGISTER;
		}
		
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
	
	public String validateReportRegister(RegisterFilterForm registerFilterForm) {
		return "";
	}
	
	/**
	 * This method is just to verify how many players has a facebook id.
	 * 
	 * @param players
	 * @return
	 */
	public int getTotalFb(List<Player> players) {
		int totalFb = 0;
		
		for (Player p : players) {
			if (p.getIdFacebook() != null && !"".equals(p.getIdFacebook())) {
				totalFb++;
			}
		}
		
		return totalFb;
	}
}