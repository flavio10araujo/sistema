package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_PRIVACY;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PrivacyController {

    @RequestMapping(value = { "/privacy" }, method = RequestMethod.GET)
    public final String diplomaSearch() {
        return URL_PRIVACY;
    }
}
