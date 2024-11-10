package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_RESULT_SEARCH;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Phase;
import com.polifono.service.IPhaseService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final SecurityService securityService;
    private final IPhaseService phaseService;

    /**
     * Search q in all the classes that the user already studied.
     */
    @GetMapping(value = "/search", params = "q")
    public String searchContent(final Model model, @RequestParam(value = "q") String q) {

        if (q == null || q.trim().isEmpty()) {
            return URL_GAMES_INDEX;
        } else if (q.trim().length() < 3 || q.trim().length() > 25) {
            return URL_GAMES_INDEX;
        }

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Looking for the phases that have the q in its content and the user has already studied.
        List<Phase> phases = phaseService.findPhasesBySearchAndUser(HtmlUtils.htmlEscape(q), currentUser.get().getUser().getId());

        model.addAttribute("phases", phases);

        return URL_GAMES_RESULT_SEARCH;
    }
}
