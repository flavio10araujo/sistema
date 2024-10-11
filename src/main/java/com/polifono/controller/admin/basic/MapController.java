package com.polifono.controller.admin.basic;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.controller.BaseController;
import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/basic")
public class MapController extends BaseController {

    public static final String URL_ADMIN_BASIC = "admin/basic/map";
    public static final String URL_ADMIN_BASIC_INDEX = "admin/basic/map/index";
    public static final String URL_ADMIN_BASIC_EDIT = "admin/basic/map/editPage";
    public static final String URL_ADMIN_BASIC_SAVEPAGE = "admin/basic/map/savepage";

    private final IGameService gameService;
    private final ILevelService levelService;
    private final IMapService mapService;

    @RequestMapping(value = { "/map", "/map/savepage" }, method = RequestMethod.GET)
    public String savePage(HttpSession session, Model model) {
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("levels", levelService.findAll());
        model.addAttribute("map", new Map());

        if (session.getAttribute("mapGameId") != null) {
            Game filterGame = new Game();
            filterGame.setId((int) session.getAttribute("mapGameId"));
            model.addAttribute("mapGame", filterGame);

            model.addAttribute("maps", mapService.findMapsByGame((int) session.getAttribute("mapGameId")));
        } else {
            model.addAttribute("mapGame", new Game());
            model.addAttribute("maps", mapService.findAll());
        }

        return URL_ADMIN_BASIC_INDEX;
    }

    @RequestMapping(value = { "/map" }, method = RequestMethod.POST)
    public String setFilter(HttpSession session, @ModelAttribute("game") Game game) {
        if (game.getId() > 0) {
            session.setAttribute("mapGameId", game.getId());
        } else {
            session.setAttribute("mapGameId", null);
        }

        return "redirect:/" + URL_ADMIN_BASIC;
    }

    @RequestMapping(value = { "/map/save" }, method = RequestMethod.POST)
    public String save(@ModelAttribute("map") Map map, final RedirectAttributes redirectAttributes) {
        try {
            mapService.save(map);
            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }

    @RequestMapping(value = "/map/{operation}/{id}", method = RequestMethod.GET)
    public String editRemove(@PathVariable("operation") String operation, @PathVariable("id") Long id, final RedirectAttributes redirectAttributes,
            Model model) {

        if (operation.equals("delete")) {
            if (mapService.delete(id.intValue())) {
                redirectAttributes.addFlashAttribute("deletion", "success");
            } else {
                redirectAttributes.addFlashAttribute("deletion", "unsuccess");
            }
        } else if (operation.equals("edit")) {
            Optional<Map> edit = mapService.findById(id.intValue());

            if (edit.isPresent()) {
                model.addAttribute("map", edit.get());
                model.addAttribute("games", gameService.findAll());
                model.addAttribute("levels", levelService.findAll());
                return URL_ADMIN_BASIC_EDIT;
            } else {
                redirectAttributes.addFlashAttribute("status", "notfound");
            }
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }

    @RequestMapping(value = "/map/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") Map edit, final RedirectAttributes redirectAttributes) {
        if (mapService.save(edit) != null) {
            redirectAttributes.addFlashAttribute("edit", "success");
        } else {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/" + URL_ADMIN_BASIC_SAVEPAGE;
    }
}
