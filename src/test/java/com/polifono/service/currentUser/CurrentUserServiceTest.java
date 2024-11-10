package com.polifono.service.currentUser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;

/**
 * Unit test methods for the CurrentUserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class CurrentUserServiceTest {

    @InjectMocks
    private CurrentUserServiceImpl service;

    private final Integer PLAYER_ID_EXISTENT = 1;

    /* canAccessUser - begin */
    @Test
    public void givenCanAccessUser_whenCurrentUserIsNull_thenReturnFalse() {
        CurrentUser currentUser = null;
        Assertions.assertFalse(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }

    @Test
    public void givenCanAccessUser_whenRoleIsAdmin_thenReturnTrue() {
        Player player = new Player();
        player.setRole(Role.ADMIN);
        player.setEmail("test@email.com");
        player.setPassword("T12345");
        CurrentUser currentUser = new CurrentUser(player);
        Assertions.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }

    @Test
    public void givenCanAccessUser_whenCurrentUserIdIsEqualsUserId_thenReturnTrue() {
        Player player = new Player();
        player.setRole(Role.ADMIN);
        player.setEmail("test@email.com");
        player.setPassword("T12345");
        CurrentUser currentUser = new CurrentUser(player);
        Assertions.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    /* canAccessUser - end */
}
