package com.polifono.config;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;
import com.polifono.service.impl.LoginService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationSuccessHandlerTest {

    private static final CurrentUser currentUserTypeUser = getCurrentUserTypeUser();
    private static final CurrentUser currentUserTypeTeacher = getCurrentUserTypeTeacher();
    private static final CurrentUser currentUserTypeAdmin = getCurrentUserTypeAdmin();

    @InjectMocks
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Mock
    private RedirectStrategy redirectStrategy;

    @Mock
    private LoginService loginService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void givenOnAuthenticationSuccess_whenRegisterLogin_thenShouldRegisterTheRightUser() throws IOException {
        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthenticationUserTypeUser());

        verify(loginService, times(1)).registerLogin(currentUserTypeUser.getUser());
    }

    @Test
    public void givenOnAuthenticationSuccess_whenUserOfTypeAdminOpensDefaultPage_thenShouldRedirectToTheRightPage() throws IOException {
        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthenticationUserTypeAdmin());

        verify(redirectStrategy, times(1)).sendRedirect(request, response, "/admin");
    }

    @Test
    public void givenOnAuthenticationSuccess_whenUserOfTypeTeacherOpensDefaultPage_thenShouldRedirectToTheRightPage() throws IOException {
        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthenticationUserTypeTeacher());

        verify(redirectStrategy, times(1)).sendRedirect(request, response, "/teacher/report");
    }

    @Test
    public void givenOnAuthenticationSuccess_whenUserOfTypeUserOpensDefaultPage_thenShouldRedirectToTheRightPage() throws IOException {
        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthenticationUserTypeUser());

        verify(redirectStrategy, times(1)).sendRedirect(request, response, "/");
    }

    @Test
    public void givenOnAuthenticationSuccess_whenUserTriedToOpenPageBeforeLogin_thenShouldRedirectToTheDesiredPage() throws IOException {
        when(session.getAttribute("SPRING_SECURITY_SAVED_REQUEST")).thenReturn(getSavedRequest());

        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, getAuthenticationUserTypeUser());

        verify(redirectStrategy, times(1)).sendRedirect(request, response, "/specific-page");
    }

    private TestingAuthenticationToken getAuthenticationUserTypeAdmin() {
        return new TestingAuthenticationToken(
                currentUserTypeAdmin,
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    private TestingAuthenticationToken getAuthenticationUserTypeTeacher() {
        return new TestingAuthenticationToken(
                currentUserTypeTeacher,
                null,
                List.of(new SimpleGrantedAuthority("TEACHER"))
        );
    }

    private TestingAuthenticationToken getAuthenticationUserTypeUser() {
        return new TestingAuthenticationToken(
                currentUserTypeUser,
                null,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    private static CurrentUser getCurrentUserTypeAdmin() {
        Player player = new Player();
        player.setId(1);
        player.setEmail("admin@email.com");
        player.setPassword("admin_password");
        player.setRole(Role.ADMIN);

        return new CurrentUser(player);
    }

    private static CurrentUser getCurrentUserTypeTeacher() {
        Player player = new Player();
        player.setId(2);
        player.setEmail("teacher@email.com");
        player.setPassword("teacher_password");
        player.setRole(Role.TEACHER);

        return new CurrentUser(player);
    }

    private static CurrentUser getCurrentUserTypeUser() {
        Player player = new Player();
        player.setId(3);
        player.setEmail("user@email.com");
        player.setPassword("user_password");
        player.setRole(Role.USER);

        return new CurrentUser(player);
    }

    private static SavedRequest getSavedRequest() {
        return new SavedRequest() {
            @Override
            public String getRedirectUrl() {
                return "/specific-page";
            }

            @Override
            public List<Cookie> getCookies() {
                return List.of();
            }

            @Override
            public String getMethod() {
                return "";
            }

            @Override
            public List<String> getHeaderValues(String name) {
                return List.of();
            }

            @Override
            public Collection<String> getHeaderNames() {
                return List.of();
            }

            @Override
            public List<Locale> getLocales() {
                return List.of();
            }

            @Override
            public String[] getParameterValues(String name) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return Map.of();
            }
        };
    }
}
