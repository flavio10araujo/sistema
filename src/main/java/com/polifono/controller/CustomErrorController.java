package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_ERROR_404;
import static com.polifono.common.TemplateConstants.URL_ERROR_500;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomErrorController {

    @RequestMapping("/error404")
    public String error404() {
        log.debug("Inside /error404");
        return URL_ERROR_404;
    }

    @RequestMapping("/error500")
    public String error500() {
        log.debug("Inside /error500");
        return URL_ERROR_500;
    }
}
