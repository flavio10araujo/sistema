package com.polifono.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Diploma;
import com.polifono.service.IDiplomaService;
import com.polifono.service.helper.DiplomaHelperService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Controller
public class DiplomaController {

    private final IDiplomaService diplomaService;
    private final DiplomaHelperService diplomaHelperService;

    @GetMapping("/diplomas")
    public String diplomaSearch(final Model model) {
        return diplomaHelperService.handleDiplomaSearch(model);
    }

    @Validated
    @PostMapping("/diplomas")
    public String diplomaSearchSubmit(final Model model, @RequestParam @NotBlank String code, Locale locale) {
        Optional<Diploma> diplomaOpt = findDiplomaByCode(code);

        if (diplomaOpt.isEmpty()) {
            return diplomaHelperService.handleDiplomaNotFound(model, locale);
        }

        model.addAttribute("message", "success");
        model.addAttribute("diploma", diplomaOpt.get());

        return diplomaHelperService.handleDiplomaSearch(model);
    }

    @Validated
    @GetMapping("/diplomas/{code}")
    public String diplomaGet(HttpServletResponse response, final Model model, @PathVariable("code") @NotBlank String code, Locale locale)
            throws JRException, IOException {

        Optional<Diploma> diplomaOpt = findDiplomaByCode(code);

        if (diplomaOpt.isEmpty()) {
            return diplomaHelperService.handleDiplomaNotFound(model, locale);
        }

        generateDiplomaPdf(response, diplomaOpt.get(), locale);

        return null;
    }

    private Optional<Diploma> findDiplomaByCode(String code) {
        return diplomaService.findByCode(code);
    }

    private void generateDiplomaPdf(HttpServletResponse response, Diploma diploma, Locale locale) throws JRException, IOException {
        diplomaService.generateDiplomaPdf(response, diploma, locale);
    }
}
