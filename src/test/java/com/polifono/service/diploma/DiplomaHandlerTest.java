package com.polifono.service.diploma;

import static com.polifono.common.constant.TemplateConstants.URL_DIPLOMA_OPEN_SEARCH;
import static com.polifono.common.constant.TemplateConstants.URL_DIPLOMA_SEARCH;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.polifono.model.entity.Player;
import com.polifono.service.SecurityService;

@ExtendWith(MockitoExtension.class)
public class DiplomaHandlerTest {

    private static final String MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST = "The Informed Certificate Does Not Exist";

    @InjectMocks
    private DiplomaHandler diplomaHelperService;

    @Mock
    private MessageSource messagesResource;

    @Mock
    private SecurityService securityService;

    /* handleDiplomaSearch - BEGIN */
    @Test
    public void givenUserIsAuthenticated_whenHandleDiplomaSearch_thenReturnDiplomaSearchPage() {
        when(securityService.isAuthenticated()).thenReturn(true);

        String result = diplomaHelperService.handleDiplomaSearch(null);

        Assertions.assertEquals(URL_DIPLOMA_SEARCH, result);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenHandleDiplomaSearch_thenReturnDiplomaSearchOpenPage() {
        when(securityService.isAuthenticated()).thenReturn(false);
        Model model = mock(Model.class);

        String result = diplomaHelperService.handleDiplomaSearch(model);

        verify(model).addAttribute(eq("player"), argThat(argument -> argument instanceof Player));
        verify(model).addAttribute(eq("playerResend"), argThat(argument -> argument instanceof Player));
        Assertions.assertEquals(URL_DIPLOMA_OPEN_SEARCH, result);
    }
    /* handleDiplomaSearch - END */

    /* handleDiplomaNotFound - BEGIN */
    @Test
    public void givenUserIsAuthenticated_whenHandleDiplomaNotFound_thenReturnDiplomaSearchPageWithErrorMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(true);
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaHelperService.handleDiplomaNotFound(model, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals(URL_DIPLOMA_SEARCH, result);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenHandleDiplomaNotFound_thenReturnDiplomaSearchOpenPageWithErrorMessage() {
        Model model = mock(Model.class);
        Locale locale = getDefaultLocale();

        when(securityService.isAuthenticated()).thenReturn(false);
        when(messagesResource.getMessage("msg.TheInformedCertificateDoesNotExist", null, locale)).thenReturn(MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);

        String result = diplomaHelperService.handleDiplomaNotFound(model, locale);

        verify(model).addAttribute("message", "error");
        verify(model).addAttribute("messageContent", MSG_THE_INFORMED_CERTIFICATE_DOES_NOT_EXIST);
        Assertions.assertEquals(URL_DIPLOMA_OPEN_SEARCH, result);
    }
    /* handleDiplomaNotFound - END */

    private static Locale getDefaultLocale() {
        return new Locale("en");
    }
}
