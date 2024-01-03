package com.polifono.controller;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nullable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseController {

    public static ResourceBundle applicationResourceBundle;
    public static ResourceBundle messagesResourceBundle;

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    static {
        applicationResourceBundle = ResourceBundle.getBundle("application", Locale.getDefault());
        messagesResourceBundle = ResourceBundle.getBundle("messages/messages", Locale.getDefault());
    }

    /**
     * Used by child class controllers to obtain the currently authenticated user from Spring Security.
     */
    @Nullable
    protected final CurrentUser currentAuthenticatedUser() {
        CurrentUser currentUser = null;
        SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            currentUser = (CurrentUser) authentication.getPrincipal();
        }

        return currentUser;
    }

    protected final void updateCurrentAuthenticateUser(Player player) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication authentication = new UsernamePasswordAuthenticationToken(new CurrentUser(player), auth.getCredentials(),
                AuthorityUtils.createAuthorityList(player.getRole().toString()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected final void createCurrentAuthenticateUserFacebook(HttpServletRequest request, HttpServletResponse response, Player player) {
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(new CurrentUser(player), "",
                AuthorityUtils.createAuthorityList(player.getRole().toString()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
