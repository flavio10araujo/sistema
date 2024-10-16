package com.polifono.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.polifono.domain.Diploma;
import com.polifono.repository.IDiplomaRepository;
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
@Service
public class DiplomaServiceImpl implements IDiplomaService {

    private final IDiplomaRepository repository;
    private final MessageSource messagesResource;

    public Diploma save(Diploma diploma) {
        return repository.save(diploma);
    }

    @Override
    public List<Diploma> findByPlayer(int playerId) {
        List<Diploma> list = repository.findByPlayer(playerId);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;
    }

    @Override
    public Optional<Diploma> findByCode(String code) {
        return repository.findByCode(code);
    }

    public void generateDiplomaPdf(HttpServletResponse response, Diploma diploma, Locale locale) throws JRException, IOException {
        List<Diploma> list = new ArrayList<>();
        list.add(diploma);

        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/compiled/diploma.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, createParams(locale), new JRBeanCollectionDataSource(list));

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=diploma.pdf");

        OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }

    private Map<String, Object> createParams(Locale locale) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("company", messagesResource.getMessage("diploma.company", null, locale));
        params.put("url", messagesResource.getMessage("url", null, locale) + "/diploma");
        params.put("img_selo", new ClassPathResource("img/diploma/selo.png").getURL());
        params.put("img_logo", new ClassPathResource("img/diploma/logo.png").getURL());
        params.put("img_assinatura", new ClassPathResource("img/diploma/assinatura.png").getURL());
        return params;
    }
}
