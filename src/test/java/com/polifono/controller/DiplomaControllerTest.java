package com.polifono.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.polifono.domain.Diploma;
import com.polifono.service.IDiplomaService;
import com.polifono.service.impl.SecurityService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

@ExtendWith(MockitoExtension.class)
public class DiplomaControllerTest {

    public static final String CODE_EXISTENT = "code_existent";
    public static final String CODE_INEXISTENT = "code_inexistent";
    public static final String MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST = "The Informed Certificate Does Not Exist";

    @InjectMocks
    private DiplomaController diplomaController;

    @Mock
    private MessageSource messagesResource;

    @Mock
    private SecurityService securityService;

    @Mock
    private IDiplomaService diplomaService;

    /* diplomaSearch - BEGIN */
    @Test
    public void givenDiplomaSearch_whenUserIsAuthenticated_thenReturnDiplomaSearchPage() {
        when(securityService.isAuthenticated()).thenReturn(true);

        String result = diplomaController.diplomaSearch(null);
        Assertions.assertEquals("diplomaSearch", result);
    }

    @Test
    public void givenDiplomaSearch_whenUserIsNotAuthenticated_thenReturnDiplomaSearchOpenPage() {
        when(securityService.isAuthenticated()).thenReturn(false);
        Model model = mock(Model.class);

        String result = diplomaController.diplomaSearch(model);
        Assertions.assertEquals("diplomaSearchOpen", result);
    }
    /* diplomaSearch - END */

    /* diplomaSearchSubmit - BEGIN */
    @Test
    public void givenDiplomaSearchSubmit_whenUserIsAuthenticatedAndDiplomaIsNotFound_thenReturnDiplomaSearchPageWithErrorMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(true);
        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaController.diplomaSearchSubmit(model, CODE_INEXISTENT, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals("diplomaSearch", result);
    }

    @Test
    public void givenDiplomaSearchSubmit_whenUserIsNotAuthenticatedAndDiplomaIsNotFound_thenReturnDiplomaSearchOpenPageWithErrorMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(false);
        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaController.diplomaSearchSubmit(model, CODE_INEXISTENT, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals("diplomaSearchOpen", result);
    }

    @Test
    public void givenDiplomaSearchSubmit_whenUserIsAuthenticatedAndDiplomaIsFound_thenReturnDiplomaSearchPageWithSuccessMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        Optional<Diploma> diploma = getDiploma();

        when(securityService.isAuthenticated()).thenReturn(true);
        when(diplomaService.findByCode(CODE_EXISTENT)).thenReturn(diploma);

        String result = diplomaController.diplomaSearchSubmit(model, CODE_EXISTENT, locale);

        verify(model).addAttribute("message", "success");
        verify(model).addAttribute("diploma", diploma.get());

        Assertions.assertEquals("diplomaSearch", result);
    }

    @Test
    public void givenDiplomaSearchSubmit_whenUserIsNotAuthenticatedAndDiplomaIsFound_thenReturnDiplomaSearchOpenPageWithSuccessMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        Optional<Diploma> diploma = getDiploma();

        when(securityService.isAuthenticated()).thenReturn(false);
        when(diplomaService.findByCode(CODE_EXISTENT)).thenReturn(diploma);

        String result = diplomaController.diplomaSearchSubmit(model, CODE_EXISTENT, locale);

        verify(model).addAttribute("message", "success");
        verify(model).addAttribute("diploma", diploma.get());

        Assertions.assertEquals("diplomaSearchOpen", result);
    }
    /* diplomaSearchSubmit - END */

    /* diplomaGet - BEGIN */
    @Test
    public void givenDiplomaGet_whenUserIsAuthenticatedAndDiplomaIsNotFound_thenReturnDiplomaSearchPageWithErrorMessage() throws JRException, IOException {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(true);
        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaController.diplomaGet(null, model, CODE_INEXISTENT, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals("diplomaSearch", result);
    }

    @Test
    public void givenDiplomaGet_whenUserIsNotAuthenticatedAndDiplomaIsNotFound_thenReturnDiplomaSearchOpenPageWithErrorMessage()
            throws JRException, IOException {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(false);
        when(diplomaService.findByCode(CODE_INEXISTENT)).thenReturn(Optional.empty());
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaController.diplomaGet(null, model, CODE_INEXISTENT, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals("diplomaSearchOpen", result);
    }

    @Test
    public void givenDiplomaGet_whenDiplomaIsFound_thenGenerateDiplomaPdf() throws JRException, IOException {
        Locale locale = getDefaultLocale();
        HttpServletResponse response = mock(HttpServletResponse.class);
        Optional<Diploma> diploma = getDiploma();

        when(diplomaService.findByCode(CODE_EXISTENT)).thenReturn(diploma);

        diplomaController.diplomaGet(response, null, CODE_EXISTENT, locale);

        verify(diplomaService).generateDiplomaPdf(response, diploma.get(), locale);
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
