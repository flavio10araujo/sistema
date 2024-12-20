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

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;
import com.polifono.service.player.PlayerService;

/**
 * Unit test methods for the UserDetailsServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Mock
    private PlayerService userService;

    private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
    private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";

    /* loadUserByUsername - begin */
    @Test
    public void givenLoadUserByUsername_whenUserNotFoundByEmailAndStatus_thenThrowUsernameNotFoundException() {
        when(userService.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(PLAYER_EMAIL_INEXISTENT);
        });
    }

    @Test
    public void givenLoadUserByUsername_whenUserIsFoundByEmailAndStatus_thenReturnUser() {
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
