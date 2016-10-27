package com.polifono.service.currentUser;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;
import com.polifono.domain.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;
import com.polifono.service.impl.PlayerServiceImpl;

/**
 * Unit test methods for the UserDetailsServiceImpl.
 * 
 */
@Transactional
public class UserDetailsServiceTest {

	private UserDetailsServiceImpl service;
	
	@Mock
	IPlayerService userService;
	
	@Mock
	private IPlayerGameService playerGameService;
	
	@Mock
	private IPlayerRepository playerRepository;
	
	private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
	private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";
	
	@Before
    public void setUp() {
        // Do something before each test method.
		MockitoAnnotations.initMocks(this);
		userService = new PlayerServiceImpl(playerRepository, playerGameService);
		service = new UserDetailsServiceImpl(userService);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* loadUserByUsername - begin */
    @Test(expected=UsernameNotFoundException.class)
    public void loadUserByUsername_WhenUserNotFoundByEmailAndStatus_ThrowUsernameNotFoundException() {
    	String email = PLAYER_EMAIL_INEXISTENT;
    	when(userService.findByEmailAndStatusForLogin(email, true)).thenReturn(Optional.empty());
    	service.loadUserByUsername(PLAYER_EMAIL_INEXISTENT);
    }
    
    @Test
    public void loadUserByUsername_WhenUserIsFoundByEmailAndStatus_ReturnUser() {
    	String email = PLAYER_EMAIL_EXISTENT;
    	
    	Player player = new Player();
    	player.setId(123);
    	player.setRole(Role.USER);
    	player.setEmail("test@email.com");
    	player.setPassword("T12345");
    	Optional<Player> returned = Optional.of(player);
    	
    	when(userService.findByEmailAndStatusForLogin(email, true)).thenReturn(returned);
    	
    	CurrentUser currentUser = service.loadUserByUsername(PLAYER_EMAIL_EXISTENT);
    	
    	Assert.assertNotNull(currentUser.getUser());
    	Assert.assertTrue("failure - expected id user bigger than zero", currentUser.getId() > 0);
    	Assert.assertTrue("failure - expected role user [ADMIN, USER or TEACHER]", 
    			(currentUser.getRole().toString().equals("ADMIN")) ||
    			(currentUser.getRole().toString().equals("USER")) ||
    			(currentUser.getRole().toString().equals("TEACHER"))
    		);
    }
    /* loadUserByUsername - end */
}