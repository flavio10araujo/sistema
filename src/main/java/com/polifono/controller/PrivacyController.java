package com.polifono.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.ServletContext;

@Controller
public class PrivacyController extends BaseController {

    @Autowired
    ServletContext context;

    public static final String URL_PRIVACY = "privacy";

    @RequestMapping(value = { "/privacy" }, method = RequestMethod.GET)
    public final String diplomaSearch(final Model model) {
        return URL_PRIVACY;
    }
}
