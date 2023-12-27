package com.polifono.service;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Login;
import com.polifono.repository.ILoginRepository;
import com.polifono.service.impl.LoginServiceImpl;

/**
 * Unit test methods for the LoginService.
 */
public class LoginServiceTest extends AbstractTest {

    private ILoginService service;

    @Mock
    private ILoginRepository repository;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    @BeforeEach
    public void setUp() {
        // Do something before each test method.
        MockitoAnnotations.initMocks(this);
        service = new LoginServiceImpl(repository);
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test method.
    }

    /* stubs - begin */
    private Optional<Login> getEntityStubData() {
        Login entity = new Login();
        entity.setId(2);
        return Optional.of(entity);
    }

    private List<Login> getEntityListStubData() {
        List<Login> list = new ArrayList<Login>();

        Login entity1 = getEntityStubData().get();
        Login entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

    /* registerLogin - begin
    @Test
    public void registerLogin_WhenPlayerExistent_RegisterLogin() {
    	Login entity = getEntityStubData();

    	when(repository.save(Matchers.any(Login.class))).thenReturn(entity);

    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);

    	Login entityReturned = service.registerLogin(player);

    	Assertions.assertNotEquals("failure - not expected ID equals 0", 0, entityReturned.getId());

    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
     registerLogin - end */

    /* prepareForRegisterLogin - begin */
    /* prepareForRegisterLogin - end */

    /* findByPlayer - begin */
    @Test
    @Disabled
    public void findByPlayer_WhenSearchByPlayerExistent_ReturnList() {
        List<Login> list = getEntityListStubData();

        ////DESCOMENTARwhen(repository.findByPlayer(PLAYER_ID_EXISTENT)).thenReturn(list);

        List<Login> listReturned = service.findByPlayer(PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not size 0");

        ////DESCOMENTARverify(repository, times(1)).findByPlayer(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled
    public void findByPlayer_WhenSearchByPlayerInexistent_ReturnNull() {
        ////DESCOMENTARwhen(repository.findByPlayer(PLAYER_ID_INEXISTENT)).thenReturn(null);

        List<Login> listReturned = service.findByPlayer(PLAYER_ID_INEXISTENT);
        Assertions.assertNull(listReturned, "failure - expected null");

        ////DESCOMENTARverify(repository, times(1)).findByPlayer(PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByPlayer - end */
}
