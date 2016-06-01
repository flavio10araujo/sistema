package com.polifono.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.controller.BaseController;

@Controller
public class AdminController extends BaseController {

	@RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
	public final String listGames(final Model model) {
		return "admin/index";
	}
}