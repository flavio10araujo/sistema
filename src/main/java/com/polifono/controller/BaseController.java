package com.polifono.controller;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nullable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;

public abstract class BaseController {

	public static ResourceBundle applicationResourceBundle;
	public static ResourceBundle messagesResourceBundle;
	
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
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            currentUser = (CurrentUser) authentication.getPrincipal();
        }
        
        return currentUser;
    }
    
    protected final void updateCurrentAuthenticateUser(Player player) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Authentication newAuth = new UsernamePasswordAuthenticationToken(new CurrentUser(player), auth.getCredentials(), AuthorityUtils.createAuthorityList(player.getRole().toString()));
    	SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}