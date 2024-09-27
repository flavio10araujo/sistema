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
import com.polifono.domain.Answer;
import com.polifono.domain.Question;
import com.polifono.form.admin.basic.AnswerFilterForm;
import com.polifono.service.IAnswerService;
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
public class AnswerController extends BaseController {

    public static final String URL_ADMIN_BASIC = "admin/basic/answer";
    public static final String URL_ADMIN_BASIC_INDEX = "admin/basic/answer/index";
    public static final String URL_ADMIN_BASIC_EDIT = "admin/basic/answer/editPage";
    public static final String URL_ADMIN_BASIC_SAVE_PAGE = "admin/basic/answer/savepage";

    private final IGameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IQuestionService questionService;
    private final IAnswerService answerService;

    @RequestMapping(value = { "/answer", "/answer/savepage" }, method = RequestMethod.GET)
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
                            model.addAttribute("answers", answerService.findByQuestion(answerFilterForm.getQuestion().getId()));
                        } else {
                            // List
                            model.addAttribute("answers", answerService.findByPhase(answerFilterForm.getPhase().getId()));
                        }
                    } else {
                        // Filter
                        model.addAttribute("questions", questionService.findByMap(answerFilterForm.getMap().getId()));
                        // List
                        model.addAttribute("answers", answerService.findByMap(answerFilterForm.getMap().getId()));
                    }
                } else {
                    // Filter.
                    model.addAttribute("questions", questionService.findByGameAndLevel(answerFilterForm.getGame().getId(), answerFilterForm.getLevel().getId()));
                    // List
                    model.addAttribute("answers", answerService.findByGameAndLevel(answerFilterForm.getGame().getId(), answerFilterForm.getLevel().getId()));
                }
            } else {
                // Filter.
                model.addAttribute("questions", questionService.findByGame(answerFilterForm.getGame().getId()));
                // List
                model.addAttribute("answers", answerService.findByGame(answerFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("answerFilterForm", new AnswerFilterForm());
            // Filter
            model.addAttribute("questions", new ArrayList<Question>());
            // List
            model.addAttribute("answers", new ArrayList<Answer>());
        }

        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/answer" }, method = RequestMethod.POST)
    public String setFilter(HttpSession session, @ModelAttribute("answerFilterForm") AnswerFilterForm answerFilterForm) {
        session.setAttribute("answerFilterForm", answerFilterForm);
        return "redirect:/" + URL_ADMIN_BASIC;
    }

    @RequestMapping(value = { "/answer/save" }, method = RequestMethod.POST)
    public String save(@ModelAttribute("answer") Answer answer, final RedirectAttributes redirectAttributes) {

        if (answerService.save(answer) != null) {
            redirectAttributes.addFlashAttribute("save", "success");
        } else {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = "/answer/{operation}/{id}", method = RequestMethod.GET)
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
                return URL_ADMIN_BASIC_EDIT;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }

    @RequestMapping(value = "/answer/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") Answer edit, final RedirectAttributes redirectAttributes) {

        try {
            answerService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVE_PAGE;
    }
}
