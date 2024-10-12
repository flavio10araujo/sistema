package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.TemplateConstants.URL_GAMES_RESULT_SEARCH;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.polifono.domain.Phase;
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
    @RequestMapping(value = { "/search" }, method = RequestMethod.GET, params = { "q" })
    public final String searchContent(final Model model, @RequestParam(value = "q") String q) {

        if (q == null || q.trim().isEmpty()) {
            return URL_GAMES_INDEX;
        } else if (q.trim().length() < 3 || q.trim().length() > 25) {
            return URL_GAMES_INDEX;
        }

        // Looking for the phases that have the q in its content and the user has already studied.
        List<Phase> phases = phaseService.findPhasesBySearchAndUser(HtmlUtils.htmlEscape(q),
                Objects.requireNonNull(securityService.getCurrentAuthenticatedUser()).getUser().getId());

        model.addAttribute("phases", phases);

        return URL_GAMES_RESULT_SEARCH;
    }
}
