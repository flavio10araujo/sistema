package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_DIPLOMA_OPEN_SEARCH;
import static com.polifono.common.TemplateConstants.URL_DIPLOMA_SEARCH;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

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
        Diploma diploma = diplomaService.findByCode(code);

        if (diploma == null) {
            return handleDiplomaNotFound(model, locale);
        }

        model.addAttribute("message", "success");
        model.addAttribute("diploma", diploma);
        return handleDiplomaSearch(model);
    }

    @Validated
    @GetMapping("/diploma/{code}")
    public String diplomaGet(HttpServletResponse response, final Model model, @PathVariable("code") @NotBlank String code, Locale locale)
            throws JRException, IOException {

        Diploma diploma = diplomaService.findByCode(code);

        if (diploma == null) {
            return handleDiplomaNotFound(model, locale);
        }

        generateDiplomaPdf(response, diploma, locale);
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

    private void generateDiplomaPdf(HttpServletResponse response, Diploma diploma, Locale locale) throws JRException, IOException {
        List<Diploma> list = new ArrayList<>();
        list.add(diploma);

        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/compiled/diploma.jasper");

        Map<String, Object> params = new HashMap<>();
        params.put("company", messagesResource.getMessage("diploma.company", null, locale));
        params.put("url", messagesResource.getMessage("url", null, locale) + "/diploma");
        params.put("img_selo", new ClassPathResource("img/diploma/selo.png").getURL());
        params.put("img_logo", new ClassPathResource("img/diploma/logo.png").getURL());
        params.put("img_assinatura", new ClassPathResource("img/diploma/assinatura.png").getURL());

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=diploma.pdf");

        OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }
}
