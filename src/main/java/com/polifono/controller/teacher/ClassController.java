package com.polifono.controller.teacher;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_TEACHER_CLASS_SAVE_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_TEACHER_CLASS_EDIT_PAGE;
import static com.polifono.common.constant.TemplateConstants.URL_TEACHER_CLASS_INDEX;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Class;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.service.impl.ClassPlayerService;
import com.polifono.service.impl.ClassService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class ClassController {

    private final SecurityService securityService;
    private final ClassService classService;
    private final ClassPlayerService classPlayerService;

    @GetMapping({ "/class", "/class/savepage" })
    public String savePage(Model model) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        model.addAttribute("class", new Class());
        model.addAttribute("classes", classService.findByTeacherAndStatus(currentUser.get().getUser().getId(), true));
        return URL_TEACHER_CLASS_INDEX;
    }

    @PostMapping("/class/save")
    public String save(@ModelAttribute("class") Class clazz, final RedirectAttributes redirectAttributes) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            clazz.setPlayer(currentUser.get().getUser());
            classService.save(classService.prepareClassForCreation(clazz));
            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_TEACHER_CLASS_SAVE_PAGE;
    }

    @GetMapping("/class/{operation}/{id}")
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // The teacher only can edit/delete/duplicate his own classes.
        Class current = classService.findById(id.intValue()).get();

        if (current.getPlayer().getId() != currentUser.get().getUser().getId()) {
            return REDIRECT_HOME;
        }

        switch (operation) {
        case "delete" -> {
            if (classService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        }
        case "edit" -> {
            Optional<Class> edit = classService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("class", edit.get());
                return URL_TEACHER_CLASS_EDIT_PAGE;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }
        case "duplicate" -> {
            Class newClass = classService.clone(current);
            newClass.setName(newClass.getName() + " CLONE");
            newClass = classService.save(classService.prepareClassForCreation(newClass));

            List<ClassPlayer> students = classPlayerService.findAllByClassIdAndStatus(current.getId(), 2);

            for (ClassPlayer student : students) {
                ClassPlayer studentClone = new ClassPlayer();
                studentClone.setClazz(newClass);
                studentClone.setPlayer(student.getPlayer());

                classPlayerService.create(studentClone);
            }

            redirectAttributes.addFlashAttribute("save", "success");
        }
        }

        return REDIRECT_TEACHER_CLASS_SAVE_PAGE;
    }

    @PostMapping("/class/update")
    public String update(@ModelAttribute("edit") Class edit, final RedirectAttributes redirectAttributes) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Class> current = classService.findById(edit.getId());

        if (current.isEmpty()) {
            return REDIRECT_HOME;
        }

        // The teacher only can edit his own classes.
        if (current.get().getPlayer().getId() != currentUser.get().getUser().getId()) {
            return REDIRECT_HOME;
        }

        edit.setPlayer(current.get().getPlayer());
        edit.setDtInc(current.get().getDtInc());
        edit.setActive(current.get().isActive());

        if (classService.save(edit) != null) {
            redirectAttributes.addFlashAttribute("edit", "success");
        } else {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return REDIRECT_TEACHER_CLASS_SAVE_PAGE;
    }
}
