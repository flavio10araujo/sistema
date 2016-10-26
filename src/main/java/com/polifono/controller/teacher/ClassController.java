package com.polifono.controller.teacher;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.controller.BaseController;
import com.polifono.service.IClassService;

@Controller
@RequestMapping("/teacher")
public class ClassController extends BaseController {

	public static final String URL_ADMIN_BASIC_INDEX = "teacher/class/index";
	public static final String URL_ADMIN_BASIC_EDIT = "teacher/class/editPage";
	public static final String URL_ADMIN_BASIC_SAVEPAGE = "teacher/class/savepage";
	
	public static final String REDIRECT_HOME = "redirect:/";
	
	@Autowired
	private IClassService classService;

	@RequestMapping(value = {"/class", "/class/savepage"}, method = RequestMethod.GET)
	public String savePage(HttpSession session, Model model) {
		model.addAttribute("class", new com.polifono.domain.Class());
		model.addAttribute("classes", (ArrayList<com.polifono.domain.Class>) classService.findClassesByTeacherAndStatus(currentAuthenticatedUser().getUser().getId(), true));
		
		return URL_ADMIN_BASIC_INDEX;
	}
	
	@RequestMapping(value = {"/class/save"}, method = RequestMethod.POST)
	public String save(@ModelAttribute("class") com.polifono.domain.Class clazz, final RedirectAttributes redirectAttributes) {

		try {
			clazz.setPlayer(this.currentAuthenticatedUser().getUser());
			classService.save(classService.prepareClassForCreation(clazz));
			redirectAttributes.addFlashAttribute("save", "success");
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("save", "unsuccess");
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}

	@RequestMapping(value = "/class/{operation}/{id}", method = RequestMethod.GET)
	public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes, Model model) {

		// The teacher only can edit/delete his own classes.
		com.polifono.domain.Class current = classService.findOne(id.intValue());
		
		if (current.getPlayer().getId() != this.currentAuthenticatedUser().getUser().getId()) return REDIRECT_HOME;
		
		if (operation.equals("delete")) {
			if (classService.delete(id.intValue())) {
				redirectAttributes.addFlashAttribute("deletion", "success");
			}
			else {
				redirectAttributes.addFlashAttribute("deletion", "unsuccess");
			}
		}
		else if (operation.equals("edit")) {
			com.polifono.domain.Class edit = classService.findOne(id.intValue());

			if (edit != null) {
				model.addAttribute("class", edit);
				return URL_ADMIN_BASIC_EDIT;
			}
			else {
				redirectAttributes.addFlashAttribute("status", "notfound");
			}
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}
	
	@RequestMapping(value = "/class/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("edit") com.polifono.domain.Class edit, final RedirectAttributes redirectAttributes) {
		
		com.polifono.domain.Class current = classService.findOne(edit.getId());
		
		// The teacher only can edit his own classes.
		if (current.getPlayer().getId() != currentAuthenticatedUser().getUser().getId()) return REDIRECT_HOME;
		
		edit.setPlayer(current.getPlayer());
		edit.setDtInc(current.getDtInc());
		edit.setActive(current.isActive());
		
		if (classService.save(edit) != null) {
			redirectAttributes.addFlashAttribute("edit", "success");
		}
		else {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		
		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}
}