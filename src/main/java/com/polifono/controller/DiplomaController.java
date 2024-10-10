package com.polifono.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Diploma;
import com.polifono.domain.Player;
import com.polifono.service.IDiplomaService;

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
public class DiplomaController extends BaseController {

    private final IDiplomaService diplomaService;

    public static final String URL_DIPLOMA_SEARCH = "diplomaSearch";
    public static final String URL_DIPLOMAOPEN_SEARCH = "diplomaSearchOpen";

    @RequestMapping(value = { "/diploma" }, method = RequestMethod.GET)
    public final String diplomaSearch(final Model model) {
        // If the user is logged in.
        if (this.currentAuthenticatedUser() != null) {
            return URL_DIPLOMA_SEARCH;
        } else {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_DIPLOMAOPEN_SEARCH;
        }
    }

    @RequestMapping(value = { "/diploma" }, method = RequestMethod.POST)
    public final String diplomaSearchSubmit(final Model model, @RequestParam(value = "code", defaultValue = "") String code) {

        if (code == null || code.isEmpty()) {
            // If the user is logged in.
            if (currentAuthenticatedUser() != null) {
                return URL_DIPLOMA_SEARCH;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_DIPLOMAOPEN_SEARCH;
            }
        }

        Diploma diploma = diplomaService.findByCode(code);

        if (diploma != null) {
            model.addAttribute("message", "success");
            model.addAttribute("diploma", diploma);
        } else {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", "O certificado informado não existe.");
        }

        // If the user is logged in.
        if (currentAuthenticatedUser() != null) {
            return URL_DIPLOMA_SEARCH;
        } else {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_DIPLOMAOPEN_SEARCH;
        }
    }

    @RequestMapping(value = { "/diploma/{code}" }, method = RequestMethod.GET)
    public final String diplomaGet(HttpServletResponse response, final Model model, @PathVariable("code") String code) throws JRException, IOException {
        if (code == null || code.isEmpty()) {
            if (currentAuthenticatedUser() != null) {
                return URL_DIPLOMA_SEARCH;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_DIPLOMAOPEN_SEARCH;
            }
        }

        Diploma diploma = diplomaService.findByCode(code);

        if (diploma == null) {
            model.addAttribute("message", "error");

            if (currentAuthenticatedUser() != null) {
                return URL_DIPLOMA_SEARCH;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_DIPLOMAOPEN_SEARCH;
            }
        }

        List<Diploma> list = new ArrayList<>();
        list.add(diploma);

        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/compiled/diploma.jasper");

        Map<String, Object> params = new HashMap<>();
        params.put("company", messagesResourceBundle.getString("diploma.company"));
        params.put("url", messagesResourceBundle.getString("url") + "/diploma");

        // Load images from classpath as URLs
        params.put("img_selo", new ClassPathResource("img/diploma/selo.png").getURL());
        params.put("img_logo", new ClassPathResource("img/diploma/logo.png").getURL());
        params.put("img_assinatura", new ClassPathResource("img/diploma/assinatura.png").getURL());

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(list));

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=diploma.pdf");

        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);

        return null;
    }
}
