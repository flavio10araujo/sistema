package com.polifono.controller.admin.basic;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_ANSWER;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_ANSWER_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_ANSWER_EDIT_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_ANSWER_INDEX;

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

import com.polifono.model.entity.Answer;
import com.polifono.model.entity.Question;
import com.polifono.model.form.admin.basic.AnswerFilterForm;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IQuestionService;
import com.polifono.service.impl.AnswerService;
import com.polifono.service.impl.LevelService;
import com.polifono.service.impl.game.GameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class AnswerController {

    private final GameService gameService;
    private final LevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IQuestionService questionService;
    private final AnswerService answerService;

    @GetMapping({ "/answer", "/answer/savepage" })
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("answer", new Answer());

        // Filter.
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());

        AnswerFilterForm answerFilterForm = (AnswerFilterForm) session.getAttribute("answerFilterForm");

        if (answerFilterForm != null && answerFilterForm.getGame().getId() > 0) {
            // Form
            model.addAttribute("answerFilterForm", answerFilterForm);

            if (answerFilterForm.getLevel().getId() > 0) {
                // Filter.
                model.addAttribute("maps", mapService.findMapsByGameAndLevel(answerFilterForm.getGame().getId(), answerFilterForm.getLevel().getId()));

                if (answerFilterForm.getMap().getId() > 0) {
                    // Filter.
                    model.addAttribute("phases", phaseService.findByMap(answerFilterForm.getMap().getId()));

                    if (answerFilterForm.getPhase().getId() > 0) {
                        // Filter
                        model.addAttribute("questions", questionService.findByPhase(answerFilterForm.getPhase().getId()));

                        if (answerFilterForm.getQuestion().getId() > 0) {
                            // List
                            model.addAttribute("answers", answerService.findAllByQuestionId(answerFilterForm.getQuestion().getId()));
                        } else {
                            // List
                            model.addAttribute("answers", answerService.findAllByPhaseId(answerFilterForm.getPhase().getId()));
                        }
                    } else {
                        // Filter
                        model.addAttribute("questions", questionService.findByMap(answerFilterForm.getMap().getId()));
                        // List
                        model.addAttribute("answers", answerService.findAllByMapId(answerFilterForm.getMap().getId()));
                    }
                } else {
                    // Filter.
                    model.addAttribute("questions",
                            questionService.findByGameAndLevel(answerFilterForm.getGame().getId(), answerFilterForm.getLevel().getId()));
                    // List
                    model.addAttribute("answers",
                            answerService.findAllByGameIdAndLevelId(answerFilterForm.getGame().getId(), answerFilterForm.getLevel().getId()));
                }
            } else {
                // Filter.
                model.addAttribute("questions", questionService.findByGame(answerFilterForm.getGame().getId()));
                // List
                model.addAttribute("answers", answerService.findAllByGameId(answerFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("answerFilterForm", new AnswerFilterForm());
            // Filter
            model.addAttribute("questions", new ArrayList<Question>());
            // List
            model.addAttribute("answers", new ArrayList<Answer>());
        }

        return URL_ADMIN_BASIC_ANSWER_INDEX;
    }

    @GetMapping("/answer")
    public String setFilter(HttpSession session, @ModelAttribute("answerFilterForm") AnswerFilterForm answerFilterForm) {
        session.setAttribute("answerFilterForm", answerFilterForm);
        return REDIRECT_ADMIN_BASIC_ANSWER;
    }

    @PostMapping("/answer/save")
    public String save(@ModelAttribute("answer") Answer answer, final RedirectAttributes redirectAttributes) {

        if (answerService.save(answer) != null) {
            redirectAttributes.addFlashAttribute("save", "success");
        } else {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_ANSWER_SAVE_PAGE;
    }

    @GetMapping("/answer/{operation}/{id}")
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (answerService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        } else if (operation.equals("edit")) {
            Optional<Answer> edit = answerService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("answer", edit.get());
                model.addAttribute("questions", questionService.findAll());
                return URL_ADMIN_BASIC_ANSWER_EDIT_PAGE;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return REDIRECT_ADMIN_BASIC_ANSWER_SAVE_PAGE;
    }

    @PostMapping("/answer/update")
    public String update(@ModelAttribute("edit") Answer edit, final RedirectAttributes redirectAttributes) {
        try {
            answerService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_ANSWER_SAVE_PAGE;
    }
}
