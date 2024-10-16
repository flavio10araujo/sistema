package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_DIPLOMA_OPEN_SEARCH;
import static com.polifono.common.TemplateConstants.URL_DIPLOMA_SEARCH;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Diploma;
import com.polifono.domain.Player;
import com.polifono.service.IDiplomaService;
import com.polifono.service.impl.SecurityService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Controller
public class DiplomaController {

    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final IDiplomaService diplomaService;

    @GetMapping("/diploma")
    public String diplomaSearch(final Model model) {
        return handleDiplomaSearch(model);
    }

    @Validated
    @PostMapping("/diploma")
    public String diplomaSearchSubmit(final Model model, @RequestParam @NotBlank String code, Locale locale) {
        Optional<Diploma> diploma = diplomaService.findByCode(code);

        if (diploma.isEmpty()) {
            return handleDiplomaNotFound(model, locale);
        }

        model.addAttribute("message", "success");
        model.addAttribute("diploma", diploma.get());
        return handleDiplomaSearch(model);
    }

    @Validated
    @GetMapping("/diploma/{code}")
    public String diplomaGet(HttpServletResponse response, final Model model, @PathVariable("code") @NotBlank String code, Locale locale)
            throws JRException, IOException {

        Optional<Diploma> diploma = diplomaService.findByCode(code);

        if (diploma.isEmpty()) {
            return handleDiplomaNotFound(model, locale);
        }

        diplomaService.generateDiplomaPdf(response, diploma.get(), locale);
        return null;
    }

    private String handleDiplomaSearch(final Model model) {
        if (securityService.isAuthenticated()) {
            return URL_DIPLOMA_SEARCH;
        } else {
            prepareModelForUnauthenticatedUser(model);
            return URL_DIPLOMA_OPEN_SEARCH;
        }
    }

    private String handleDiplomaNotFound(final Model model, Locale locale) {
        model.addAttribute("message", "error");
        model.addAttribute("messageContent", messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale));
        return handleDiplomaSearch(model);
    }

    private void prepareModelForUnauthenticatedUser(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
    }
}
