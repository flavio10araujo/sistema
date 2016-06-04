package com.polifono.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.polifono.domain.CurrentUser;
import com.polifono.service.LoginService;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);
 
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Autowired
	private LoginService loginService;
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    	handle(request, response, authentication);
    	//clearAuthenticationAttributes(request);
    }
 
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        registerLogin(authentication);
    	
    	//String targetUrl = determineTargetUrl(authentication);
    	String targetUrl = "/";
 
        if (response.isCommitted()) {
        	LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
 
        redirectStrategy.sendRedirect(request, response, targetUrl);
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
 
    /** Builds the target URL according to the logic defined in the main class Javadoc. */
    /*protected String determineTargetUrl(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
 
        if (isUser) {
            return "/homepage.html";
        } else if (isAdmin) {
            return "/console.html";
        } else {
            throw new IllegalStateException();
        }
    }*/
 
    /*protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }*/
 
    /*public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }*/
    
    /*protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }*/
}