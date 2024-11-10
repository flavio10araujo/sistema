package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_ERROR_404;
import static com.polifono.common.TemplateConstants.URL_ERROR_500;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error404")
    public String error404() {
        return URL_ERROR_404;
    }

    @GetMapping("/error500")
    public String error500() {
        return URL_ERROR_500;
    }
}
