package com.polifono.controller.admin.basic;

import java.util.ArrayList;
import java.util.Optional;

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
import com.polifono.form.admin.basic.ContentFilterForm;
import com.polifono.service.impl.ContentServiceImpl;
import com.polifono.service.impl.GameServiceImpl;
import com.polifono.service.impl.LevelServiceImpl;
import com.polifono.service.impl.MapServiceImpl;
import com.polifono.service.impl.PhaseServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class ContentController extends BaseController {

    public static final String URL_ADMIN_BASIC = "admin/basic/content";
    public static final String URL_ADMIN_BASIC_INDEX = "admin/basic/content/index";
    public static final String URL_ADMIN_BASIC_EDIT = "admin/basic/content/editPage";
    public static final String URL_ADMIN_BASIC_SAVE_PAGE = "admin/basic/content/savepage";

    public static final String URL_ADMIN_BASIC_TEST = "admin/basic/contentTest";
    public static final String URL_ADMIN_BASIC_INDEX_TEST = "admin/basic/contentTest/index";
    public static final String URL_ADMIN_BASIC_SAVE_PAGE_TEST = "admin/basic/contentTest/savepage";

    private final GameServiceImpl gameService;
    private final LevelServiceImpl levelService;
    private final MapServiceImpl mapService;
    private final PhaseServiceImpl phaseService;
    private final ContentServiceImpl contentService;

    @RequestMapping(value = { "/content", "/content/savepage" }, method = RequestMethod.GET)
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("content", new Content());

        // Filter.
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());

        ContentFilterForm contentFilterForm = (ContentFilterForm) session.getAttribute("contentFilterForm");

        if (contentFilterForm != null && contentFilterForm.getGame().getId() > 0) {
            // Form
            model.addAttribute("contentFilterForm", contentFilterForm);

            if (contentFilterForm.getLevel().getId() > 0) {
                // Filter.
                model.addAttribute("maps", mapService.findMapsByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));

                if (contentFilterForm.getMap().getId() > 0) {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByMap(contentFilterForm.getMap().getId()));

                    if (contentFilterForm.getPhase().getId() > 0) {
                        // List
                        model.addAttribute("contents", contentService.findContentsTextByPhase(contentFilterForm.getPhase().getId()));
                    } else {
                        // List
                        model.addAttribute("contents", contentService.findContentsTextByMap(contentFilterForm.getMap().getId()));
                    }
                } else {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
                    // List
                    model.addAttribute("contents", contentService.findContentsTextByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
                }
            } else {
                model.addAttribute("phases", phaseService.findByGame(contentFilterForm.getGame().getId()));
                // List
                model.addAttribute("contents", contentService.findContentsTextByGame(contentFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("contentFilterForm", new ContentFilterForm());
            model.addAttribute("phases", phaseService.findAll());
            // List
            model.addAttribute("contents", contentService.findAllText());
        }

        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/contentTest", "/contentTest/savepage" }, method = RequestMethod.GET)
    public String savePageTest(HttpSession session, Model model) {
        model.addAttribute("content", new Content());

        // Filter.
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());

        ContentFilterForm contentFilterForm = (ContentFilterForm) session.getAttribute("contentTestFilterForm");

        if (contentFilterForm != null && contentFilterForm.getGame().getId() > 0) {
            // Form
            model.addAttribute("contentTestFilterForm", contentFilterForm);

            if (contentFilterForm.getLevel().getId() > 0) {
                // Filter.
                model.addAttribute("maps", mapService.findMapsByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));

                if (contentFilterForm.getMap().getId() > 0) {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByMap(contentFilterForm.getMap().getId()));

                    if (contentFilterForm.getPhase().getId() > 0) {
                        // List
                        model.addAttribute("contents", contentService.findContentsTestByPhase(contentFilterForm.getPhase().getId()));
                    } else {
                        // List
                        model.addAttribute("contents", contentService.findContentsTestByMap(contentFilterForm.getMap().getId()));
                    }
                } else {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
                    // List
                    model.addAttribute("contents", contentService.findContentsTestByGameAndLevel(contentFilterForm.getGame().getId(),
                            contentFilterForm.getLevel().getId()));
                }
            } else {
                // Filter.
                model.addAttribute("phases", phaseService.findByGame(contentFilterForm.getGame().getId()));
                // List
                model.addAttribute("contents", contentService.findContentsTestByGame(contentFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("contentTestFilterForm", new ContentFilterForm());
            // Filter
            model.addAttribute("phases", phaseService.findAll());
            // List
            model.addAttribute("contents", contentService.findAllTest());
        }

        return URL_ADMIN_BASIC_INDEX_TEST;
    }

    @RequestMapping(value = { "/content" }, method = RequestMethod.POST)
    public String setFilter(HttpSession session, @ModelAttribute("contentFilterForm") ContentFilterForm contentFilterForm) {
        session.setAttribute("contentFilterForm", contentFilterForm);
        return "redirect:/" + URL_ADMIN_BASIC;
    }

    @RequestMapping(value = { "/contentTest" }, method = RequestMethod.POST)
    public String setFilterTest(HttpSession session, @ModelAttribute("contentTestFilterForm") ContentFilterForm contentFilterForm) {
        session.setAttribute("contentTestFilterForm", contentFilterForm);
        return "redirect:/" + URL_ADMIN_BASIC_TEST;
    }

    @RequestMapping(value = { "/content/save" }, method = RequestMethod.POST)
    public String save(@ModelAttribute("content") Content content, final RedirectAttributes redirectAttributes) {
        try {
            Contenttype contenttype = new Contenttype();
            contenttype.setId(2);
            content.setContenttype(contenttype);
            contentService.save(content);
            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = { "/contentTest/save" }, method = RequestMethod.POST)
    public String saveTest(@ModelAttribute("content") Content content, final RedirectAttributes redirectAttributes) {
        try {
            Contenttype contenttype = new Contenttype();
            contenttype.setId(1);
            content.setContenttype(contenttype);
            content.setOrder(0);

            contentService.save(content);
            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE_TEST;
    }

    @RequestMapping(value = "/content/{operation}/{id}", method = RequestMethod.GET)
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (contentService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        } else if (operation.equals("edit")) {
            Optional<Content> edit = contentService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("content", edit.get());
                model.addAttribute("phases", (ArrayList<Phase>) phaseService.findAll());
                return URL_ADMIN_BASIC_EDIT;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = "/contentTest/{operation}/{id}", method = RequestMethod.GET)
    public String editRemoveTest(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (contentService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE_TEST;
    }

    @RequestMapping(value = "/content/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") Content edit, final RedirectAttributes redirectAttributes) {

        try {
            Contenttype contenttype = new Contenttype();
            contenttype.setId(2);
            edit.setContenttype(contenttype);

            contentService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }
}
