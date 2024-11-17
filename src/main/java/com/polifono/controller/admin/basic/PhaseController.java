package com.polifono.controller.admin.basic;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_PHASE;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_ADMIN_BASIC_PHASE_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_PHASE_EDIT_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_BASIC_PHASE_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.model.entity.Phase;
import com.polifono.model.form.admin.basic.PhaseFilterForm;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.impl.game.GameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class PhaseController {

    private final GameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;
    private final IPhaseService phaseService;

    @GetMapping({ "/phase", "/phase/savepage" })
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("phase", new Phase());

        // Filter.
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());

        PhaseFilterForm phaseFilterForm = (PhaseFilterForm) session.getAttribute("phaseFilterForm");

        if (phaseFilterForm != null && phaseFilterForm.getGame().getId() > 0) {
            // Form
            model.addAttribute("phaseFilterForm", phaseFilterForm);

            if (phaseFilterForm.getLevel().getId() > 0) {
                // Filter.
                model.addAttribute("maps", mapService.findMapsByGameAndLevel(phaseFilterForm.getGame().getId(), phaseFilterForm.getLevel().getId()));

                if (phaseFilterForm.getMap().getId() > 0) {
                    // List
                    model.addAttribute("phases", phaseService.findByMap(phaseFilterForm.getMap().getId()));
                } else {
                    // List
                    model.addAttribute("phases", phaseService.findByGameAndLevel(phaseFilterForm.getGame().getId(), phaseFilterForm.getLevel().getId()));
                }
            } else {
                // Filter
                model.addAttribute("maps", mapService.findMapsByGame(phaseFilterForm.getGame().getId()));
                // List
                model.addAttribute("phases", phaseService.findByGame(phaseFilterForm.getGame().getId()));
            }
        } else {
            // Form
            model.addAttribute("phaseFilterForm", new PhaseFilterForm());
            // Filter
            model.addAttribute("maps", mapService.findAll());
            // List
            model.addAttribute("phases", phaseService.findAll());
        }

        return URL_ADMIN_BASIC_PHASE_INDEX;
    }

    @PostMapping("/phase")
    public String setFilter(HttpSession session, @ModelAttribute("phaseFilterForm") PhaseFilterForm phaseFilterForm) {
        session.setAttribute("phaseFilterForm", phaseFilterForm);
        return REDIRECT_ADMIN_BASIC_PHASE;
    }

    @PostMapping("/phase/save")
    public String save(@ModelAttribute("phase") Phase phase, final RedirectAttributes redirectAttributes) {
        if (phaseService.save(phase) != null) {
            redirectAttributes.addFlashAttribute("save", "success");
        } else {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_PHASE_SAVE_PAGE;
    }

    @GetMapping("/phase/{operation}/{id}")
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (phaseService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        } else if (operation.equals("edit")) {
            Optional<Phase> edit = phaseService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("phase", edit.get());
                model.addAttribute("maps", mapService.findAll());
                return URL_ADMIN_BASIC_PHASE_EDIT_PAGE;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return REDIRECT_ADMIN_BASIC_PHASE_SAVE_PAGE;
    }

    @PostMapping("/phase/update")
    public String update(@ModelAttribute("edit") Phase edit, final RedirectAttributes redirectAttributes) {
        try {
            phaseService.save(edit);
            redirectAttributes.addFlashAttribute("edit", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return REDIRECT_ADMIN_BASIC_PHASE_SAVE_PAGE;
    }
}
