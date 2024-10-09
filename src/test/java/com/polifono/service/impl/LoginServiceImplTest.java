package com.polifono.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.domain.Login;
import com.polifono.domain.Player;
import com.polifono.repository.ILoginRepository;

/**
 * Unit test methods for the LoginService.
 */
@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl service;

    @Mock
    private ILoginRepository repository;

    @Captor
    private ArgumentCaptor<Login> loginCaptor;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    /* registerLogin - begin */
    @Test
    public void registerLogin_WhenPlayerExistent_RegisterLogin() {
        Login entity = getEntityStubData().get();

        when(repository.save(any())).thenReturn(entity);

        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);

        Login entityReturned = service.registerLogin(player);

        Assertions.assertNotEquals(0, entityReturned.getId(), "failure - not expected ID equals 0");

        // Use ArgumentCaptor to capture the Login object passed as parameter to the method save:
        verify(repository).save(loginCaptor.capture());
        Login loginCaptured = loginCaptor.getValue();

        Assertions.assertEquals(PLAYER_ID_EXISTENT, loginCaptured.getPlayer().getId(), "failure - expected player ID equals " + PLAYER_ID_EXISTENT);

        verify(repository, times(1)).save(loginCaptured);
        verifyNoMoreInteractions(repository);
    }
    /* registerLogin - end */

    /* prepareForRegisterLogin - begin */
    /* prepareForRegisterLogin - end */

    /* findByPlayer - begin */
    @Test
    public void findByPlayer_WhenSearchByPlayerExistent_ReturnList() {
        List<Date> list = getEntityListDateStubData();

        when(repository.findByPlayer(eq(PLAYER_ID_EXISTENT), any())).thenReturn(list);

        List<Login> listReturned = service.findByPlayer(PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not size 0");
        Assertions.assertEquals(list.size(), listReturned.size(), "failure - not size expected");

        verify(repository, times(1)).findByPlayer(eq(PLAYER_ID_EXISTENT), any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByPlayer_WhenSearchByPlayerInexistent_ReturnNull() {
        when(repository.findByPlayer(eq(PLAYER_ID_INEXISTENT), any())).thenReturn(new ArrayList<>());

        List<Login> listReturned = service.findByPlayer(PLAYER_ID_INEXISTENT);
        Assertions.assertNull(listReturned, "failure - expected null");

        verify(repository, times(1)).findByPlayer(eq(PLAYER_ID_INEXISTENT), any());
        verifyNoMoreInteractions(repository);
    }
    /* findByPlayer - end */

    /* stubs - begin */
    private Optional<Login> getEntityStubData() {
        Login entity = new Login();
        entity.setId(2);
        return Optional.of(entity);
    }

    private List<Date> getEntityListDateStubData() {
        List<Date> list = new ArrayList<>();

        Date entity1 = new Date();
        Date entity2 = new Date();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
