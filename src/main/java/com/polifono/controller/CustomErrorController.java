package com.polifono.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomErrorController {

    @RequestMapping("/error404")
    String error404() {
        log.debug("Inside /error404");
        return "error/404";
    }

    @RequestMapping("/error500")
    String error500() {
        log.debug("Inside /error500");
        return "error/500";
    }
}
