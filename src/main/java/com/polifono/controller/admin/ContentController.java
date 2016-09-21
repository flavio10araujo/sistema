package com.polifono.controller.admin;

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
import com.polifono.domain.Content;
import com.polifono.domain.Contenttype;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.form.admin.ContentFilterForm;
import com.polifono.service.impl.ContentServiceImpl;
import com.polifono.service.impl.GameServiceImpl;
import com.polifono.service.impl.LevelServiceImpl;
import com.polifono.service.impl.MapServiceImpl;
import com.polifono.service.impl.PhaseServiceImpl;

@Controller
@RequestMapping("/admin/basic")
public class ContentController extends BaseController {

	public static final String URL_ADMIN_BASIC = "admin/basic/content";
	public static final String URL_ADMIN_BASIC_INDEX = "admin/basic/content/index";
	public static final String URL_ADMIN_BASIC_EDIT = "admin/basic/content/editPage";
	public static final String URL_ADMIN_BASIC_SAVEPAGE = "admin/basic/content/savepage";
	
	public static final String URL_ADMIN_BASIC_TEST = "admin/basic/contentTest";
	public static final String URL_ADMIN_BASIC_INDEX_TEST = "admin/basic/contentTest/index";
	public static final String URL_ADMIN_BASIC_SAVEPAGE_TEST = "admin/basic/contentTest/savepage";
	
	@Autowired
	private GameServiceImpl gameService;
	
	@Autowired
	private LevelServiceImpl levelService;
	
	@Autowired
	private MapServiceImpl mapService;
	
	@Autowired
	private PhaseServiceImpl phaseService;
	
	@Autowired
	private ContentServiceImpl contentService;
	
	@RequestMapping(value = {"/content", "/content/savepage"}, method = RequestMethod.GET)
	public String savePage(HttpSession session, Model model) {
		model.addAttribute("content", new Content());
		
		// Filter.
		model.addAttribute("games", (ArrayList<Game>) gameService.findAll());
		model.addAttribute("levels", (ArrayList<Level>) levelService.findAll());
		
		ContentFilterForm contentFilterForm = (ContentFilterForm) session.getAttribute("contentFilterForm");
		
		if (contentFilterForm != null && contentFilterForm.getGame().getId() > 0) {
			// Form
			model.addAttribute("contentFilterForm", contentFilterForm);

			if (contentFilterForm.getLevel().getId() > 0) {
				// Filter.
				model.addAttribute("maps", (ArrayList<Map>) mapService.findMapsByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
				
				if (contentFilterForm.getMap().getId() > 0) {
					// Filter.
					model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByMap(contentFilterForm.getMap().getId()));
					
					if (contentFilterForm.getPhase().getId() > 0) {
						// List
						model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTextByPhase(contentFilterForm.getPhase().getId()));
					}
					else {
						// List
						model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTextByMap(contentFilterForm.getMap().getId()));
					}
				}
				else {
					// Filter.
					model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
					// List
					model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTextByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
				}
			}
			else {
				// Filter.
				//model.addAttribute("maps", (ArrayList<Map>) mapService.findMapsByGame(contentFilterForm.getGame().getId()));
				model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByGame(contentFilterForm.getGame().getId()));
				// List
				model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTextByGame(contentFilterForm.getGame().getId()));
			}
		}
		else {
			// Form
			model.addAttribute("contentFilterForm", new ContentFilterForm());
			// Filter
			//model.addAttribute("maps", (ArrayList<Map>) mapService.findAll());
			model.addAttribute("phases", (ArrayList<Phase>) phaseService.findAll());
			// List
			model.addAttribute("contents", (ArrayList<Content>) contentService.findAllText());
		}
		
		return URL_ADMIN_BASIC_INDEX;
	}
	
	@RequestMapping(value = {"/contentTest", "/contentTest/savepage"}, method = RequestMethod.GET)
	public String savePageTest(HttpSession session, Model model) {
		model.addAttribute("content", new Content());
		
		// Filter.
		model.addAttribute("games", (ArrayList<Game>) gameService.findAll());
		model.addAttribute("levels", (ArrayList<Level>) levelService.findAll());
		
		ContentFilterForm contentFilterForm = (ContentFilterForm) session.getAttribute("contentTestFilterForm");
		
		if (contentFilterForm != null && contentFilterForm.getGame().getId() > 0) {
			// Form
			model.addAttribute("contentTestFilterForm", contentFilterForm);

			if (contentFilterForm.getLevel().getId() > 0) {
				// Filter.
				model.addAttribute("maps", (ArrayList<Map>) mapService.findMapsByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
				
				if (contentFilterForm.getMap().getId() > 0) {
					// Filter.
					model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByMap(contentFilterForm.getMap().getId()));
					
					if (contentFilterForm.getPhase().getId() > 0) {
						// List
						model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTestByPhase(contentFilterForm.getPhase().getId()));
					}
					else {
						// List
						model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTestByMap(contentFilterForm.getMap().getId()));
					}
				}
				else {
					// Filter.
					model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
					// List
					model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTestByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
				}
			}
			else {
				// Filter.
				model.addAttribute("phases", (ArrayList<Phase>) phaseService.findPhasesByGame(contentFilterForm.getGame().getId()));
				// List
				model.addAttribute("contents", (ArrayList<Content>) contentService.findContentsTestByGame(contentFilterForm.getGame().getId()));
			}
		}
		else {
			// Form
			model.addAttribute("contentTestFilterForm", new ContentFilterForm());
			// Filter
			model.addAttribute("phases", (ArrayList<Phase>) phaseService.findAll());
			// List
			model.addAttribute("contents", (ArrayList<Content>) contentService.findAllTest());
		}
		
		return URL_ADMIN_BASIC_INDEX_TEST;
	}
	
	@RequestMapping(value = {"/content"}, method = RequestMethod.POST)
	public String setFilter(HttpSession session, @ModelAttribute("contentFilterForm") ContentFilterForm contentFilterForm) {
		session.setAttribute("contentFilterForm", contentFilterForm);
		return "redirect:/" + URL_ADMIN_BASIC;
	}
	
	@RequestMapping(value = {"/contentTest"}, method = RequestMethod.POST)
	public String setFilterTest(HttpSession session, @ModelAttribute("contentTestFilterForm") ContentFilterForm contentFilterForm) {
		session.setAttribute("contentTestFilterForm", contentFilterForm);
		return "redirect:/" + URL_ADMIN_BASIC_TEST;
	}

	@RequestMapping(value = {"/content/save"}, method = RequestMethod.POST)
	public String save(@ModelAttribute("content") Content content, final RedirectAttributes redirectAttributes) {

		Contenttype contenttype = new Contenttype();
		contenttype.setId(2);
		content.setContenttype(contenttype);
		
		if (contentService.save(content) != null) {
			redirectAttributes.addFlashAttribute("save", "success");
		}
		else {
			redirectAttributes.addFlashAttribute("save", "unsuccess");
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}
	
	@RequestMapping(value = {"/contentTest/save"}, method = RequestMethod.POST)
	public String saveTest(@ModelAttribute("content") Content content, final RedirectAttributes redirectAttributes) {

		Contenttype contenttype = new Contenttype();
		contenttype.setId(1);
		content.setContenttype(contenttype);
		content.setOrder(0);
		
		if (contentService.save(content) != null) {
			redirectAttributes.addFlashAttribute("save", "success");
		}
		else {
			redirectAttributes.addFlashAttribute("save", "unsuccess");
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE_TEST;
	}
	
	@RequestMapping(value = "/content/{operation}/{id}", method = RequestMethod.GET)
	public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes, Model model) {

		if (operation.equals("delete")) {
			if (contentService.delete(id.intValue())) {
				redirectAttributes.addFlashAttribute("deletion", "success");
			}
			else {
				redirectAttributes.addFlashAttribute("deletion", "unsuccess");
			}
		}
		else if (operation.equals("edit")) {
			Content edit = contentService.findOne(id.intValue());

			if (edit != null) {
				model.addAttribute("content", edit);
				model.addAttribute("phases", (ArrayList<Phase>) phaseService.findAll());
				return URL_ADMIN_BASIC_EDIT;
			}
			else {
				redirectAttributes.addFlashAttribute("status", "notfound");
			}
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}
	
	@RequestMapping(value = "/contentTest/{operation}/{id}", method = RequestMethod.GET)
	public String editRemoveTest(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes, Model model) {

		if (operation.equals("delete")) {
			if (contentService.delete(id.intValue())) {
				redirectAttributes.addFlashAttribute("deletion", "success");
			}
			else {
				redirectAttributes.addFlashAttribute("deletion", "unsuccess");
			}
		}

		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE_TEST;
	}
	
	@RequestMapping(value = "/content/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("edit") Content edit, final RedirectAttributes redirectAttributes) {
		
		try  {
			Contenttype contenttype = new Contenttype();
			contenttype.setId(2);
			edit.setContenttype(contenttype);
			
			contentService.save(edit);
			redirectAttributes.addFlashAttribute("edit", "success");
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		
		return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
	}
}