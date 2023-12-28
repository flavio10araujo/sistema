package com.polifono.service.currentUser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.domain.enums.Role;

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
    public void canAccessUser_WhenCurrentUserIsNull_ReturnFalse() {
        CurrentUser currentUser = null;
        Assertions.assertFalse(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }

    @Test
    public void canAccessUser_WhenRoleIsAdmin_ReturnTrue() {
        Player player = new Player();
        player.setRole(Role.ADMIN);
        player.setEmail("test@email.com");
        player.setPassword("T12345");
        CurrentUser currentUser = new CurrentUser(player);
        Assertions.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }

    @Test
    public void canAccessUser_WhenCurrentUserIdIsEqualsUserId_ReturnTrue() {
        Player player = new Player();
        player.setRole(Role.ADMIN);
        player.setEmail("test@email.com");
        player.setPassword("T12345");
        CurrentUser currentUser = new CurrentUser(player);
        Assertions.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    /* canAccessUser - end */
}
