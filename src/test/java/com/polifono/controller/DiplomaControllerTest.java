package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_DIPLOMA_SEARCH;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.polifono.model.entity.Diploma;
import com.polifono.service.IDiplomaService;
import com.polifono.service.handler.DiplomaHandler;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

@ExtendWith(MockitoExtension.class)
public class DiplomaControllerTest {

    private static final String CODE_EXISTENT = "code_existent";
    private static final String CODE_INEXISTENT = "code_inexistent";

    @InjectMocks
    private DiplomaController diplomaController;

    @Mock
    private IDiplomaService diplomaService;

    @Mock
    private DiplomaHandler diplomaHelperService;

    /* diplomaSearch - BEGIN */
    @Test
    public void whenDiplomaSearch_thenReturnDiplomaSearchPage() {
        Model model = mock(Model.class);
        when(diplomaHelperService.handleDiplomaSearch(model)).thenReturn("page");

        diplomaController.diplomaSearch(model);

        verify(diplomaHelperService).handleDiplomaSearch(model);
    }
    /* diplomaSearch - END */

    /* diplomaSearchSubmit - BEGIN */
    @Test
    public void givenDiplomaIsNotFound_whenDiplomaSearchSubmit_thenReturnDiplomaSearchPageWithErrorMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(diplomaHelperService.handleDiplomaNotFound(model, locale)).thenReturn("page");

        diplomaController.diplomaSearchSubmit(model, CODE_INEXISTENT, locale);

        verify(diplomaHelperService).handleDiplomaNotFound(model, locale);
    }

    @Test
    public void givenDiplomaIsFound_whenDiplomaSearchSubmit_thenReturnDiplomaSearchPageWithSuccessMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        Optional<Diploma> diplomaOpt = getDiploma();

        when(diplomaService.findByCode(CODE_EXISTENT)).thenReturn(diplomaOpt);
        doNothing().when(diplomaHelperService).addMessageAndDiplomaToModel(model, diplomaOpt.orElse(null));
        when(diplomaHelperService.handleDiplomaSearch(model)).thenReturn(URL_DIPLOMA_SEARCH);

        diplomaController.diplomaSearchSubmit(model, CODE_EXISTENT, locale);

        verify(diplomaHelperService).addMessageAndDiplomaToModel(model, diplomaOpt.orElse(null));
        verify(diplomaHelperService).handleDiplomaSearch(model);
    }
    /* diplomaSearchSubmit - END */

    /* diplomaGet - BEGIN */
    @Test
    public void givenDiplomaIsNotFound_whenDiplomaGet_thenReturnDiplomaSearchPageWithErrorMessage() throws JRException, IOException {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(diplomaHelperService.handleDiplomaNotFound(model, locale)).thenReturn(URL_DIPLOMA_SEARCH);

        diplomaController.diplomaGet(null, model, CODE_INEXISTENT, locale);

        verify(diplomaHelperService).handleDiplomaNotFound(model, locale);
    }

    @Test
    public void givenDiplomaIsFound_whenDiplomaGet_thenGenerateDiplomaPdf() throws JRException, IOException {
        Locale locale = getDefaultLocale();
        HttpServletResponse response = mock(HttpServletResponse.class);
        Optional<Diploma> diploma = getDiploma();

        when(diplomaService.findByCode(CODE_EXISTENT)).thenReturn(diploma);
        doNothing().when(diplomaService).generateDiplomaPdf(response, diploma.orElse(null), locale);

        diplomaController.diplomaGet(response, null, CODE_EXISTENT, locale);

        verify(diplomaService).generateDiplomaPdf(response, diploma.orElse(null), locale);
    }
    /* diplomaGet - END */

    private static Locale getDefaultLocale() {
        return new Locale("en");
    }

    private static Optional<Diploma> getDiploma() {
        return Optional.of(Diploma.builder()
                .code(CODE_EXISTENT)
                .build());
    }
}
