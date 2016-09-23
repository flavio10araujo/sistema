package com.polifono.controller.teacher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.controller.BaseController;
import com.polifono.domain.ClassPlayer;
import com.polifono.dto.teacher.ReportGeneralDTO;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IClassService;
import com.polifono.service.IGameService;
import com.polifono.service.IPlayerPhaseService;

@Controller
@RequestMapping("/teacher")
public class ReportController extends BaseController {
	
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IClassService classService;
	
	@Autowired
	private IClassPlayerService classPlayerService;
	
	@Autowired
	private IPlayerPhaseService playerPhaseService;

	@RequestMapping(value = {"/report"}, method = RequestMethod.GET)
	public String reportGeneral(Model model) {
		
		model.addAttribute("games", gameService.findAll());
		model.addAttribute("classes", classService.findClassesByTeacherAndStatus(currentAuthenticatedUser().getUser().getId(), true));
		// Form
		model.addAttribute("reportGeneralForm", new ReportGeneralForm());
		
		return "teacher/report/index";
	}
	
	@RequestMapping(value = {"/report"}, method = RequestMethod.POST)
	public String reportGeneralSubmit(@ModelAttribute("reportGeneralForm") ReportGeneralForm reportGeneralForm, Model model) {
		String msg = validateReportGeneral(reportGeneralForm);
		
		model.addAttribute("games", gameService.findAll());
		model.addAttribute("classes", classService.findClassesByTeacherAndStatus(currentAuthenticatedUser().getUser().getId(), true));
		// Form
		model.addAttribute("reportGeneralForm", reportGeneralForm);
		
		// If there is an error.
		if (!msg.equals("")) {
			model.addAttribute("message", "error");
			model.addAttribute("messageContent", msg);
			return "teacher/report/index";
		}
		
		List<ClassPlayer> classPlayers = classPlayerService.findClassPlayersByClassAndStatus(reportGeneralForm.getClazz().getId(), 2);
		List<ReportGeneralDTO> list = new ArrayList<ReportGeneralDTO>();
		
		for (ClassPlayer classPlayer : classPlayers) {
			list.add(new ReportGeneralDTO(reportGeneralForm.getPhaseBegin(), reportGeneralForm.getPhaseEnd(), classPlayer.getPlayer(), 
					playerPhaseService.findForReportGeneral(reportGeneralForm, classPlayer.getPlayer().getId())
				));
		}
		
		model.addAttribute("reportGeneral", list);
		
		return "teacher/report/index";
	}
	
	public String validateReportGeneral(ReportGeneralForm reportGeneralForm) {
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