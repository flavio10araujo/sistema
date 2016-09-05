package com.polifono.service.currentUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;
import com.polifono.service.impl.PlayerServiceImpl;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);
    private final PlayerServiceImpl userService;

    @Autowired
    public CurrentUserDetailsService(PlayerServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));
        Player user = userService.getUserByEmailAndStatusForLogin(email, true).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
        
        return new CurrentUser(user);
    }
}