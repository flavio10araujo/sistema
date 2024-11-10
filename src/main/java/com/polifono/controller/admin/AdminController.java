package com.polifono.controller.admin;

import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_INDEX;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String listGames() {
        return URL_ADMIN_INDEX;
    }
}
