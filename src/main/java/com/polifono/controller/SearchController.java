package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_GAMES_RESULT_SEARCH;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.polifono.common.validation.ValidSearch;
import com.polifono.model.entity.Phase;
import com.polifono.service.PhaseService;
import com.polifono.service.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final SecurityService securityService;
    private final PhaseService phaseService;

    @Validated
    @GetMapping(value = "/search", params = "q")
    public String searchContent(final Model model,
            @RequestParam(value = "q") @ValidSearch String q) {

        List<Phase> phases = phaseService.findPhasesBySearchAndUser(HtmlUtils.htmlEscape(q), securityService.getUserId());
        model.addAttribute("phases", phases);
        return URL_GAMES_RESULT_SEARCH;
    }
}
