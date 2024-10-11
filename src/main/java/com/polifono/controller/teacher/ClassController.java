package com.polifono.controller.teacher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.controller.BaseController;
import com.polifono.domain.ClassPlayer;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IClassService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class ClassController extends BaseController {

    public static final String URL_ADMIN_BASIC_INDEX = "teacher/class/index";
    public static final String URL_ADMIN_BASIC_EDIT = "teacher/class/editPage";
    public static final String URL_ADMIN_BASIC_SAVEPAGE = "teacher/class/savepage";

    public static final String REDIRECT_HOME = "redirect:/";

    private final IClassService classService;
    private final IClassPlayerService classPlayerService;

    @RequestMapping(value = { "/class", "/class/savepage" }, method = RequestMethod.GET)
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("class", new com.polifono.domain.Class());
        model.addAttribute("classes", classService.findByTeacherAndStatus(currentAuthenticatedUser().getUser().getId(), true));
        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/class/save" }, method = RequestMethod.POST)
    public String save(@ModelAttribute("class") com.polifono.domain.Class clazz, final RedirectAttributes redirectAttributes) {
        try {
            clazz.setPlayer(Objects.requireNonNull(this.currentAuthenticatedUser()).getUser());
            classService.save(classService.prepareClassForCreation(clazz));
            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }

    @RequestMapping(value = "/class/{operation}/{id}", method = RequestMethod.GET)
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        // The teacher only can edit/delete/duplicate his own classes.
        com.polifono.domain.Class current = classService.findById(id.intValue()).get();

        if (current.getPlayer().getId() != Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId())
            return REDIRECT_HOME;

        switch (operation) {
        case "delete" -> {
            if (classService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        }
        case "edit" -> {
            Optional<com.polifono.domain.Class> edit = classService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("class", edit.get());
                return URL_ADMIN_BASIC_EDIT;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }
        case "duplicate" -> {
            com.polifono.domain.Class newClass = classService.clone(current);
            newClass.setName(newClass.getName() + " CLONE");
            newClass = classService.save(classService.prepareClassForCreation(newClass));

            List<ClassPlayer> students = classPlayerService.findByClassAndStatus(current.getId(), 2);

            for (ClassPlayer student : students) {
                ClassPlayer studentClone = new ClassPlayer();
                studentClone.setClazz(newClass);
                studentClone.setPlayer(student.getPlayer());

                classPlayerService.create(studentClone);
            }

            redirectAttributes.addFlashAttribute("save", "success");
        }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }

    @RequestMapping(value = "/class/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") com.polifono.domain.Class edit, final RedirectAttributes redirectAttributes) {

        com.polifono.domain.Class current = classService.findById(edit.getId()).get();

        // The teacher only can edit his own classes.
        if (current.getPlayer().getId() != Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId())
            return REDIRECT_HOME;

        edit.setPlayer(current.getPlayer());
        edit.setDtInc(current.getDtInc());
        edit.setActive(current.isActive());

        if (classService.save(edit) != null) {
            redirectAttributes.addFlashAttribute("edit", "success");
        } else {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }
}
