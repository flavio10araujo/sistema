package com.polifono.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.polifono.domain.bean.CurrentUser;
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
        boolean isUser = false, isTeacher = false, isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("TEACHER")) {
                isTeacher = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            return "/";
        } else if (isTeacher) {
            return "/teacher/report";
        } else if (isAdmin) {
            return "/admin";
        } else {
            throw new IllegalStateException();
        }
    }
}
