package com.polifono.controller.admin.basic;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_QUESTION;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_QUESTION_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_QUESTION_EDIT_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_QUESTION_INDEX;

import java.util.ArrayList;
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
import com.polifono.model.entity.Question;
import com.polifono.model.form.admin.basic.QuestionFilterForm;
import com.polifono.service.IContentService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IQuestionService;
import com.polifono.service.impl.game.GameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class QuestionController {

    private final GameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IContentService contentService;
    private final IQuestionService questionService;

    @GetMapping({ "/question", "/question/savepage" })
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

        return URL_ADMIN_BASIC_QUESTION_INDEX;
    }

    @PostMapping("/question")
    public String setFilter(HttpSession session, @ModelAttribute("questionFilterForm") QuestionFilterForm questionFilterForm) {
        session.setAttribute("questionFilterForm", questionFilterForm);
        return REDIRECT_ADMIN_BASIC_QUESTION;
    }

    @PostMapping("/question/save")
    public String save(@ModelAttribute("question") Question question, final RedirectAttributes redirectAttributes) {
        if (questionService.save(question) != null) {
            redirectAttributes.addFlashAttribute("save", "success");
        } else {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_QUESTION_SAVE_PAGE;
    }

    @GetMapping("/question/{operation}/{id}")
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
                return URL_ADMIN_BASIC_QUESTION_EDIT_PAGE;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return REDIRECT_ADMIN_BASIC_QUESTION_SAVE_PAGE;
    }

    @PostMapping("/question/update")
    public String update(@ModelAttribute("edit") Question edit, final RedirectAttributes redirectAttributes) {

        try {
            questionService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_QUESTION_SAVE_PAGE;
    }
}
