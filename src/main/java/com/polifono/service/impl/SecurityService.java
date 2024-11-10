package com.polifono.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Player;

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
     */
    public Optional<CurrentUser> getCurrentAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            return Optional.of((CurrentUser) authentication.getPrincipal());
        }

        return Optional.empty();
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

    /**
     * Get the user's ID.
     */
    public int getUserId() {
        Optional<CurrentUser> currentUser = getCurrentAuthenticatedUser();
        return currentUser.map(user -> user.getUser().getId()).orElseThrow(() -> new IllegalStateException("User not authenticated"));
    }

    public Player getUser() {
        Optional<CurrentUser> currentUser = getCurrentAuthenticatedUser();
        return currentUser.map(CurrentUser::getUser).orElseThrow(() -> new IllegalStateException("User not authenticated"));
    }
}
