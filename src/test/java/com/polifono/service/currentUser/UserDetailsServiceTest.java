package com.polifono.service.currentUser;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.domain.enums.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;

/**
 * Unit test methods for the UserDetailsServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Mock
    private IPlayerService userService;

    @Mock
    private IPlayerGameService playerGameService;

    @Mock
    private IPlayerRepository playerRepository;

    private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
    private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";

    /* loadUserByUsername - begin */
    @Test
    public void loadUserByUsername_WhenUserNotFoundByEmailAndStatus_ThrowUsernameNotFoundException() {
        when(userService.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(PLAYER_EMAIL_INEXISTENT);
        });
    }

    @Test
    public void loadUserByUsername_WhenUserIsFoundByEmailAndStatus_ReturnUser() {
        Player player = new Player();
        player.setId(123);
        player.setRole(Role.USER);
        player.setEmail("test@email.com");
        player.setPassword("T12345");
        Optional<Player> returned = Optional.of(player);

        when(userService.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true)).thenReturn(returned);

        CurrentUser currentUser = service.loadUserByUsername(PLAYER_EMAIL_EXISTENT);

        Assertions.assertNotNull(currentUser.getUser());
        Assertions.assertTrue(currentUser.getId() > 0, "failure - expected id user bigger than zero");
        Assertions.assertTrue(
                (currentUser.getRole().toString().equals("ADMIN")) ||
                        (currentUser.getRole().toString().equals("USER")) ||
                        (currentUser.getRole().toString().equals("TEACHER"))
                , "failure - expected role user [ADMIN, USER or TEACHER]");
    }
    /* loadUserByUsername - end */
}
