package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_GAMES;
import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.polifono.model.entity.Player;
import com.polifono.service.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final SecurityService securityService;

    @GetMapping("/")
    public String index(final Model model) {
        if (securityService.isAuthenticated()) {
            return REDIRECT_GAMES;
        }

        prepareModel(model);
        return URL_INDEX;
    }

    private void prepareModel(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
    }
}
