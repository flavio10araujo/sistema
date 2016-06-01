package com.polifono.controller;

import java.util.List;

import javax.annotation.Nullable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;

public abstract class BaseController {

	/**
     * Used by child class controllers to obtain the currently authenticated user from Spring Security.
     */
    @Nullable
    final CurrentUser currentAuthenticatedUser() {
    	CurrentUser currentUser = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CurrentUser) {
            currentUser = (CurrentUser) authentication.getPrincipal();
        }
        
        return currentUser;
    }
    
    final void updateCurrentAuthenticateUser(Player player) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Authentication newAuth = new UsernamePasswordAuthenticationToken(new CurrentUser(player), auth.getCredentials(), auth.getAuthorities());
    	SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
    
    /**
	 * Verify if all the strings into an array can be converted to a int.
	 * Return true if all the string can be converted to int.
	 * 
	 * @param list
	 * @return
	 */
	public boolean isParameterInteger(List<String> list) {
		for (String s : list) {
			try {
				Integer.parseInt(s);
			}
			catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}
}