package com.polifono.controller.admin;

import static com.polifono.common.TemplateConstants.URL_ADMIN_INDEX;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.controller.BaseController;

@Controller
public class AdminController extends BaseController {

    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public final String listGames() {
        return URL_ADMIN_INDEX;
    }
}
