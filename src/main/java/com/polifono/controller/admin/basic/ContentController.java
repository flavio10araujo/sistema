package com.polifono.controller.admin.basic;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_CONTENT;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_CONTENT_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_CONTENT_TEST;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_CONTENT_TEST_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_CONTENT_EDIT_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_CONTENT_INDEX;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_CONTENT_TEST_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.model.entity.Content;
import com.polifono.model.entity.Contenttype;
import com.polifono.model.form.admin.basic.ContentFilterForm;
import com.polifono.service.ContentService;
import com.polifono.service.LevelService;
import com.polifono.service.MapService;
import com.polifono.service.PhaseService;
import com.polifono.service.game.GameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class ContentController {

    private final GameService gameService;
    private final LevelService levelService;
    private final MapService mapService;
    private final PhaseService phaseService;
    private final ContentService contentService;

    @GetMapping({ "/content", "/content/savepage" })
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
                    model.addAttribute("contents",
                            contentService.findContentsTextByGameAndLevel(contentFilterForm.getGame().getId(), contentFilterForm.getLevel().getId()));
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

        return URL_ADMIN_BASIC_CONTENT_INDEX;
    }

    @GetMapping({ "/contentTest", "/contentTest/savepage" })
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

        return URL_ADMIN_BASIC_CONTENT_TEST_INDEX;
    }

    @PostMapping("/content")
    public String setFilter(HttpSession session, @ModelAttribute("contentFilterForm") ContentFilterForm contentFilterForm) {
        session.setAttribute("contentFilterForm", contentFilterForm);
        return REDIRECT_ADMIN_BASIC_CONTENT;
    }

    @PostMapping("/contentTest")
    public String setFilterTest(HttpSession session, @ModelAttribute("contentTestFilterForm") ContentFilterForm contentFilterForm) {
        session.setAttribute("contentTestFilterForm", contentFilterForm);
        return REDIRECT_ADMIN_BASIC_CONTENT_TEST;
    }

    @PostMapping("/content/save")
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

        return REDIRECT_ADMIN_BASIC_CONTENT_SAVE_PAGE;
    }

    @PostMapping("/contentTest/save")
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

        return REDIRECT_ADMIN_BASIC_CONTENT_TEST_SAVE_PAGE;
    }

    @GetMapping("/content/{operation}/{id}")
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
                model.addAttribute("phases", phaseService.findAll());
                return URL_ADMIN_BASIC_CONTENT_EDIT_PAGE;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return REDIRECT_ADMIN_BASIC_CONTENT_SAVE_PAGE;
    }

    @GetMapping("/contentTest/{operation}/{id}")
    public String editRemoveTest(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes) {
        if (operation.equals("delete")) {
            if (contentService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        }

        return REDIRECT_ADMIN_BASIC_CONTENT_TEST_SAVE_PAGE;
    }

    @PostMapping("/content/update")
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

        return REDIRECT_ADMIN_BASIC_CONTENT_SAVE_PAGE;
    }
}
