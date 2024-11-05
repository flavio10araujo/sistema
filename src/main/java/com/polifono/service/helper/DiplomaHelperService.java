package com.polifono.service.helper;

import static com.polifono.common.TemplateConstants.URL_DIPLOMA_OPEN_SEARCH;
import static com.polifono.common.TemplateConstants.URL_DIPLOMA_SEARCH;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.polifono.domain.Player;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DiplomaHelperService {

    private final MessageSource messagesResource;
    private final SecurityService securityService;

    public String handleDiplomaSearch(final Model model) {
        if (securityService.isAuthenticated()) {
            return URL_DIPLOMA_SEARCH;
        } else {
            prepareModelForUnauthenticatedUser(model);
            return URL_DIPLOMA_OPEN_SEARCH;
        }
    }

    public String handleDiplomaNotFound(final Model model, Locale locale) {
        model.addAttribute("message", "error");
        model.addAttribute("messageContent", messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale));
        return handleDiplomaSearch(model);
    }

    private void prepareModelForUnauthenticatedUser(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
    }
}
