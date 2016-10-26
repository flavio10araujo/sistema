package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Login;
import com.polifono.domain.Player;
import com.polifono.repository.ILoginRepository;
import com.polifono.service.impl.LoginServiceImpl;

/**
 * Unit test methods for the LoginService.
 * 
 */
public class LoginServiceTest extends AbstractTest {

    private ILoginService service;
    
    @Mock
    private ILoginRepository repository;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new LoginServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Login getEntityStubData() {
    	Login entity = new Login();
    	entity.setId(2);
    	return entity;
    }
    
    private List<Login> getEntityListStubData() {
    	List<Login> list = new ArrayList<Login>();
    	
    	Login entity1 = getEntityStubData();
    	Login entity2 = getEntityStubData();
    	
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
    	
    	Assert.assertNotEquals("failure - not expected ID equals 0", 0, entityReturned.getId());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
     registerLogin - end */
    
    /* prepareForRegisterLogin - begin */
    /* prepareForRegisterLogin - end */

    /* findByPlayer - begin */
    @Test
    public void findByPlayer_WhenSearchByPlayerExistent_ReturnList() {
    	List<Login> list = getEntityListStubData();
    	
    	when(repository.findByPlayer(PLAYER_ID_EXISTENT)).thenReturn(list);
    	
    	List<Login> listReturned = service.findByPlayer(PLAYER_ID_EXISTENT);
    	Assert.assertNotNull("failure - not expected null", listReturned);
    	Assert.assertNotEquals("failure - not size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByPlayer(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByPlayer_WhenSearchByPlayerInexistent_ReturnNull() {
    	when(repository.findByPlayer(PLAYER_ID_INEXISTENT)).thenReturn(null);
    	
    	List<Login> listReturned = service.findByPlayer(PLAYER_ID_INEXISTENT);
    	Assert.assertNull("failure - expected null", listReturned);
    	
    	verify(repository, times(1)).findByPlayer(PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByPlayer - end */
}