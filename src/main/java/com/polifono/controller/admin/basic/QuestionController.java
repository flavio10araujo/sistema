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
import com.polifono.domain.Question;
import com.polifono.form.admin.basic.QuestionFilterForm;
import com.polifono.service.IContentService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IQuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class QuestionController extends BaseController {

    public static final String URL_ADMIN_BASIC = "admin/basic/question";
    public static final String URL_ADMIN_BASIC_INDEX = "admin/basic/question/index";
    public static final String URL_ADMIN_BASIC_EDIT = "admin/basic/question/editPage";
    public static final String URL_ADMIN_BASIC_SAVE_PAGE = "admin/basic/question/savepage";

    private final IGameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IContentService contentService;
    private final IQuestionService questionService;

    @RequestMapping(value = { "/question", "/question/savepage" }, method = RequestMethod.GET)
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("question", new Question());

        // Filter.
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());

        QuestionFilterForm questionFilterForm = (QuestionFilterForm) session.getAttribute("questionFilterForm");

        if (questionFilterForm != null && questionFilterForm.getGame().getId() > 0) {
            // Form
            model.addAttribute("questionFilterForm", questionFilterForm);

            if (questionFilterForm.getLevel().getId() > 0) {
                // Filter.
                model.addAttribute("maps", mapService.findMapsByGameAndLevel(questionFilterForm.getGame().getId(), questionFilterForm.getLevel().getId()));

                if (questionFilterForm.getMap().getId() > 0) {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByMap(questionFilterForm.getMap().getId()));

                    if (questionFilterForm.getPhase().getId() > 0) {
                        // Filter
                        model.addAttribute("contents", contentService.findContentsTestByPhase(questionFilterForm.getPhase().getId()));
                        // List
                        model.addAttribute("questions", questionService.findByPhase(questionFilterForm.getPhase().getId()));
                    } else {
                        // Filter
                        model.addAttribute("contents", contentService.findContentsTestByMap(questionFilterForm.getMap().getId()));
                        // List
                        model.addAttribute("questions", questionService.findByMap(questionFilterForm.getMap().getId()));
                    }
                } else {
                    // Filter
                    model.addAttribute("contents", contentService.findContentsTestByGameAndLevel(questionFilterForm.getGame().getId(),
                            questionFilterForm.getLevel().getId()));
                    // List
                    model.addAttribute("questions", questionService.findByGameAndLevel(questionFilterForm.getGame().getId(),
                            questionFilterForm.getLevel().getId()));
                }
            } else {
                // Filter
                model.addAttribute("contents", contentService.findContentsTestByGame(questionFilterForm.getGame().getId()));
                // List
                model.addAttribute("questions", questionService.findByGame(questionFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("questionFilterForm", new QuestionFilterForm());
            // Filter
            model.addAttribute("contents", new ArrayList<Content>());
            // List
            model.addAttribute("questions", new ArrayList<Question>());
        }

        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/question" }, method = RequestMethod.POST)
    public String setFilter(HttpSession session, @ModelAttribute("questionFilterForm") QuestionFilterForm questionFilterForm) {
        session.setAttribute("questionFilterForm", questionFilterForm);
        return "redirect:/" + URL_ADMIN_BASIC;
    }

    @RequestMapping(value = { "/question/save" }, method = RequestMethod.POST)
    public String save(@ModelAttribute("question") Question question, final RedirectAttributes redirectAttributes) {
        if (questionService.save(question) != null) {
            redirectAttributes.addFlashAttribute("save", "success");
        } else {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = "/question/{operation}/{id}", method = RequestMethod.GET)
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (questionService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        } else if (operation.equals("edit")) {
            Optional<Question> edit = questionService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("question", edit.get());
                model.addAttribute("contents", (ArrayList<Content>) contentService.findAllTest());
                return URL_ADMIN_BASIC_EDIT;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = "/question/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") Question edit, final RedirectAttributes redirectAttributes) {

        try {
            questionService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }
}
