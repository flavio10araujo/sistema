package com.polifono.service.impl;

import javax.annotation.Nullable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SecurityService {

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    /**
     * Check if the current user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof CurrentUser;
    }

    /**
     * Get the current authenticated user.
     *
     * @return the current authenticated user or null if the user is not authenticated.
     */
    @Nullable
    public CurrentUser getCurrentAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            return (CurrentUser) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * Update the current authenticated user.
     *
     * @param player the player to update.
     */
    public void updateCurrentAuthenticatedUser(Player player) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication authentication = new UsernamePasswordAuthenticationToken(new CurrentUser(player), auth.getCredentials(),
                AuthorityUtils.createAuthorityList(player.getRole().toString()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void createCurrentAuthenticatedUserFacebook(HttpServletRequest request, HttpServletResponse response, Player player) {
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(new CurrentUser(player), "",
                AuthorityUtils.createAuthorityList(player.getRole().toString()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
