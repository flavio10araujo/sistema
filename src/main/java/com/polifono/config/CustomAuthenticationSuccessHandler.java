package com.polifono.config;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.polifono.domain.bean.CurrentUser;
import com.polifono.service.impl.LoginServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private LoginServiceImpl loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        registerLogin(authentication);
        handle(request, response, authentication);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        if (savedRequest == null) {
            String targetUrl = determineTargetUrl(authentication);

            if (response.isCommitted()) {
                LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }

            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }
    }

    private final void registerLogin(Authentication authentication) {
        loginService.registerLogin(currentAuthenticatedUser(authentication).getUser());
    }

    private final CurrentUser currentAuthenticatedUser(Authentication authentication) {
        CurrentUser currentUser = null;

        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            currentUser = (CurrentUser) authentication.getPrincipal();
        }

        return currentUser;
    }

    /**
     * Builds the target URL according to the logic defined in the main class Javadoc.
     */
    protected String determineTargetUrl(Authentication authentication) {
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
