package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_PRIVACY;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyController {

    @GetMapping("/privacy")
    public String diplomaSearch() {
        return URL_PRIVACY;
    }
}
