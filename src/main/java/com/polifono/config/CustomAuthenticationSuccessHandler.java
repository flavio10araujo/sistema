package com.polifono.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.polifono.model.CurrentUser;
import com.polifono.service.impl.LoginServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy;
    private final LoginServiceImpl loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        registerLogin(authentication);
        handle(request, response, authentication);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        if (savedRequest == null) {
            String targetUrl = determineTargetUrl(authentication);

            if (response.isCommitted()) {
                return;
            }

            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }
    }

    private void registerLogin(Authentication authentication) {
        loginService.registerLogin(currentAuthenticatedUser(authentication).getUser());
    }

    private CurrentUser currentAuthenticatedUser(Authentication authentication) {
        CurrentUser currentUser = null;

        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            currentUser = (CurrentUser) authentication.getPrincipal();
        }

        return currentUser;
    }

    /**
     * Builds the target URL according to the logic defined in the main class Javadoc.
     */
    private String determineTargetUrl(Authentication authentication) {
        Map<String, String> roleUrlMap = Map.of(
                "USER", "/",
                "TEACHER", "/teacher/report",
                "ADMIN", "/admin"
        );

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String url = roleUrlMap.get(authority.getAuthority());
            if (url != null) {
                return url;
            }
        }

        throw new IllegalStateException();
    }
}
