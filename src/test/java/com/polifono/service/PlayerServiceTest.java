package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.impl.PlayerServiceImpl;

/**
 * Unit test methods for the PlayerService.
 * 
 */
public class PlayerServiceTest extends AbstractTest {

    private IPlayerService service;
    
	@Mock
	private IPlayerRepository repository;
	
	@Mock
	private IPlayerGameService playerGameService;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
	private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";
	
	private final Integer GAME_ID_EXISTENT = 2;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
		service = new PlayerServiceImpl(repository, playerGameService);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Player getEntityStubData() {
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	
    	Player entity = new Player();
    	entity.setId(PLAYER_ID_EXISTENT);
        entity.setName("Name of the Player");
        entity.setEmail(PLAYER_EMAIL_EXISTENT);
        entity.setPassword("password");
        entity.setRole(Role.USER);
        
        entity.setPlayerGameList(playerGameList);
        
        return entity;
    }
    
    private List<Player> getEntityListStubData() {
    	List<Player> list = new ArrayList<Player>();
    	
    	Player entity1 = getEntityStubData();
    	Player entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    
    private Player getPlayerWithSpecificCreditsStubData() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	
    	PlayerGame playerGame = new PlayerGame();
    	playerGame.setCredit(20);
    	playerGame.setGame(game);
    	
    	playerGameList.add(playerGame);
    	
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);
    	player.setCredit(0); // With no generic credits.
    	player.setPlayerGameList(playerGameList); // With specific credits.
        
        return player;
    }
    
    private Phase getPhaseStubData() {
    	Phase phase = new Phase();
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	map.setGame(game);
    	phase.setMap(map);
    	
    	return phase;
    }
    /* stubs - end */
    
    /* create - begin */
    @Test
    public void create_WhenCreatePlayer_ReturnPlayerCreated() {
        Player entity = getEntityStubData();
        
        when(repository.save(entity)).thenReturn(entity);

        Player entityReturned = service.create(entity);

        Assert.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
        Assert.assertEquals("failure - expected email attribute match", entity.getEmail(), entityReturned.getEmail());
        //Assert.assertEquals("failure - expected password attribute match", service.encryptPassword(entity.getPassword()), newEntity.getPassword());
        
        Assert.assertEquals("failure - expected dtInc attribute match", 0, entity.getDtInc().compareTo(entityReturned.getDtInc()));
        Assert.assertEquals("failure - expected active attribute match", true, entityReturned.isActive());
        Assert.assertEquals("failure - expected credit attribute match", 30, entityReturned.getCredit());
        Assert.assertEquals("failure - expected role attribute match", Role.USER, entityReturned.getRole());
        
        Assert.assertEquals("failure - expected score attribute match", 0, entityReturned.getScore());
        Assert.assertEquals("failure - expected indEmailConfirmed attribute match", false, entityReturned.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected emailConfirmed attribute not null", entityReturned.getEmailConfirmed());
        Assert.assertNull("failure - expected passwordReset attribute null", entityReturned.getPasswordReset());
        Assert.assertNull("failure - expected phone attribute null", entityReturned.getPhone());
        Assert.assertEquals("failure - expected sex attribute match", 0, entityReturned.getSex());
        Assert.assertNull("failure - expected address attribute null", entityReturned.getAddress());
        
        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* create - end */
    
    /* preparePlayerForCreation - begin */
    /* preparePlayerForCreation - end */
    
    /* save - begin */
    @Test
    public void save_WhenSavePlayer_ReturnPlayerSaved() {
    	Player entity = getEntityStubData();
        
        when(repository.save(entity)).thenReturn(entity);

        Player entityReturned = service.save(entity);
    	
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
        Assert.assertEquals("failure - expected email attribute match", entity.getEmail(), entityReturned.getEmail());
        //Assert.assertEquals("failure - expected password attribute match", service.encryptPassword(auxEntity.getPassword()), updatedEntity.getPassword());
        
        Assert.assertEquals("failure - expected active attribute match", entity.isActive(), entityReturned.isActive());
        Assert.assertEquals("failure - expected credit attribute match", entity.getCredit(), entityReturned.getCredit());
        Assert.assertEquals("failure - expected role attribute match", entity.getRole(), entityReturned.getRole());
        
        Assert.assertEquals("failure - expected score attribute match", entity.getScore(), entityReturned.getScore());
        Assert.assertEquals("failure - expected indEmailConfirmed attribute match", entity.isIndEmailConfirmed(), entityReturned.isIndEmailConfirmed());
        Assert.assertEquals("failure - expected emailConfirmed attribute match", entity.getEmailConfirmed(), entityReturned.getEmailConfirmed());
        Assert.assertEquals("failure - expected passwordReset attribute match", entity.getPasswordReset(), entityReturned.getPasswordReset());
        Assert.assertEquals("failure - expected phone attribute match", entity.getPhone(), entityReturned.getPhone());
        Assert.assertEquals("failure - expected sex attribute match", entity.getSex(), entityReturned.getSex());
        Assert.assertEquals("failure - expected address attribute match", entity.getAddress(), entityReturned.getAddress());
        
        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerIsExistent_ReturnPlayer() {
    	Player entity = getEntityStubData();
        
        when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
        Player entityReturned = service.findOne(PLAYER_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findOne_WhenPlayerIsInexistent_ReturnNull() {
    	when(repository.findOne(PLAYER_ID_INEXISTENT)).thenReturn(null);
    	
    	Player entityReturned = service.findOne(PLAYER_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllPlayers_ReturnList() {
    	List<Player> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<Player> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByEmail - begin */
    @Test
    public void findByEmail_WhenSearchByPlayerExistent_ReturnPlayer() {
    	Player entity = getEntityStubData();
    	
    	when(repository.findByEmail(PLAYER_EMAIL_EXISTENT)).thenReturn(entity);
    	
    	Player entityReturned = service.findByEmail(PLAYER_EMAIL_EXISTENT);
    	
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entityReturned.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entityReturned.getId() > 0);
        Assert.assertNotNull("failure - expected active not null", entityReturned.isActive());
        Assert.assertNotNull("failure - expected password not null", entityReturned.getPassword());
        Assert.assertNotNull("failure - expected name not null", entityReturned.getName());
        Assert.assertNotNull("failure - expected score not null", entityReturned.getScore());
        Assert.assertNotNull("failure - expected credit not null", entityReturned.getCredit());
        Assert.assertNotNull("failure - expected role not null", entityReturned.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entityReturned.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entityReturned.getSex());
        
        verify(repository, times(1)).findByEmail(PLAYER_EMAIL_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByEmail_WhenSearchByPlayerInexistent_ReturnNull() {
    	when(repository.findByEmail(PLAYER_EMAIL_INEXISTENT)).thenReturn(null);
    	
    	Player entityReturned = service.findByEmail(PLAYER_EMAIL_INEXISTENT);
    	Assert.assertNull("failure - expected null", entityReturned);
    	
    	verify(repository, times(1)).findByEmail(PLAYER_EMAIL_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmail - end */

    /* findByEmailAndStatus - begin */
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentAndByRightStatus_ReturnPlayer() {
    	Player entity = getEntityStubData();
    	
    	when(repository.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true)).thenReturn(entity);
    	
    	Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true);
        
    	Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entityReturned.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entityReturned.getId() > 0);
        Assert.assertNotNull("failure - expected active not null", entityReturned.isActive());
        Assert.assertNotNull("failure - expected password not null", entityReturned.getPassword());
        Assert.assertNotNull("failure - expected name not null", entityReturned.getName());
        Assert.assertNotNull("failure - expected score not null", entityReturned.getScore());
        Assert.assertNotNull("failure - expected credit not null", entityReturned.getCredit());
        Assert.assertNotNull("failure - expected role not null", entityReturned.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entityReturned.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entityReturned.getSex());
        
        verify(repository, times(1)).findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentButByWrongStatus_ReturnNull() {
    	when(repository.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false)).thenReturn(null);
    	
    	Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByEmailAndStatus_WhenSearchPlayerInexistent_ReturnNull() {
    	when(repository.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(null);
    	
    	Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
    	Assert.assertNull("failure - expected null", entityReturned);
    	
    	verify(repository, times(1)).findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmailAndStatus - end */
    
    /* findByEmailAndStatusForLogin - begin */
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentAndActive_ReturnPlayer() {
    	Optional<Player> entity = Optional.of(getEntityStubData());
    	
    	when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true)).thenReturn(entity);
    	
    	Player entityReturned = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true).orElse(new Player());
    	
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entityReturned.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entityReturned.getId() > 0);
        Assert.assertNotNull("failure - expected active not null", entityReturned.isActive());
        Assert.assertNotNull("failure - expected password not null", entityReturned.getPassword());
        Assert.assertNotNull("failure - expected name not null", entityReturned.getName());
        Assert.assertNotNull("failure - expected score not null", entityReturned.getScore());
        Assert.assertNotNull("failure - expected credit not null", entityReturned.getCredit());
        Assert.assertNotNull("failure - expected role not null", entityReturned.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entityReturned.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entityReturned.getSex());
        
        verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentButWrongStatus_ReturnNull() {
    	when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false)).thenReturn(Optional.empty());
    	
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false).orElse(null);
        Assert.assertNull("failure - expected null", entity);
        
        verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerInexistent_ReturnNull() {
    	when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(Optional.empty());
    	
    	Player entityReturned = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true).orElse(null);
    	Assert.assertNull("failure - expected null", entityReturned);
    	
    	verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmailAndStatusForLogin - end */
    
    /* encryptPassword - begin */
    @Test
    public void encryptPassword_WhenEverythingIsOK_ReturnTextEncrypted() {
    	String rawPassword = "12345";
    	String ret = service.encryptPassword(rawPassword);
    	Assert.assertNotNull("failure - expected not null", ret);
    	Assert.assertTrue("failure - expected size bigger than 0", ret.length() > 0);
    }
    /* encryptPassword - end */
    
    /* addCreditsToPlayer - begin */
    @Test
    public void addCreditsToPlayer_WhenEverythingIsOK_ReturnPlayerWithMoreCredits() {
    	Player entity = getEntityStubData();
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Player entityReturned = service.addCreditsToPlayer(PLAYER_ID_EXISTENT, 10);
    	
    	Assert.assertNotNull("failure - expected null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* addCreditsToPlayer - end */
    
    /* preparePlayerForAddingCredits - begin */
    /* preparePlayerForAddingCredits - end */
    
    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenEverythingIsOK_ReturnPlayerWithLessCredits() {
    	Player entity = getEntityStubData();
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Player entityReturned = service.removeCreditsFromPlayer(PLAYER_ID_EXISTENT, 5);
    	
    	Assert.assertNotNull("failure - expected null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* removeCreditsFromPlayer - end */
    
    /* prepareForRemovingCredits - begin */
    /* prepareForRemovingCredits - end */
    
    /* removeOneCreditFromPlayer - begin */
    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerHasSpecificCreditsOfTheGame_ReturnPlayerWithOneSpecificCreditLess() {
    	Player entity = getPlayerWithSpecificCreditsStubData();
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerGame playerGame = entity.getPlayerGameList().get(0);
    	
    	when(playerGameService.removeCreditsFromPlayer(playerGame, 1)).thenReturn(playerGame);
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Player entityReturned = service.removeOneCreditFromPlayer(entity, game);
    	
    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
    	
    	verify(playerGameService, times(1)).removeCreditsFromPlayer(playerGame, 1);
    	verifyNoMoreInteractions(playerGameService);
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerDoesntHaveSpecificCreditsOfTheGame_ReturnPlayerWithOneGenericCreditLess() {
    	Player entity = getEntityStubData();
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Player entityReturned = service.removeOneCreditFromPlayer(entity, game);
    	
    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verify(repository, times(1)).save(entity);
    	verifyNoMoreInteractions(repository);
    }
    /* removeOneCreditFromPlayer - end */
    
    /* playerHasCredits - begin */
    @Test
    public void playerHasCredits_WhenPlayerHasGenericCredits_ReturnTrue() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setCredit(30);
    	
    	Phase phase = new Phase();

    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertTrue(service.playerHasCredits(player, phase));
    }
    
    @Test
    public void playerHasCredits_WhenPlayerHasSpecificCredits_ReturnTrue() {
    	Player entity = getPlayerWithSpecificCreditsStubData();
    	entity.setCredit(0); // Assuring that the player has no generic credits.
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertTrue(service.playerHasCredits(entity, getPhaseStubData()));
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void playerHasCredits_WhenPlayerHasGenericAndSpecificCredits_ReturnTrue() {
    	Player entity = getPlayerWithSpecificCreditsStubData();
    	entity.setCredit(30); // Assuring that the player has generic credits.
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertTrue(service.playerHasCredits(entity, getPhaseStubData()));
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void playerHasCredits_WhenPlayerHasNotCredits_ReturnFalse() {
    	Player entity = getEntityStubData();
    	entity.setCredit(0); // Assuring that the player has no generic credits.
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertFalse(service.playerHasCredits(entity, getPhaseStubData()));
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    /* playerHasCredits - end */
    
    /* isEmailConfirmed - begin */
    @Test
    public void isEmailConfirmed_WhenEmailIsConfirmed_ReturnTrue() {
    	Player entity = getEntityStubData();
    	entity.setIndEmailConfirmed(true);
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertTrue(service.isEmailConfirmed(entity));
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void isEmailConfirmed_WhenEmailIsNotConfirmed_ReturnFalse() {
    	Player entity = getEntityStubData();
    	entity.setIndEmailConfirmed(false);
    	
    	when(repository.findOne(PLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertFalse(service.isEmailConfirmed(entity));
    	
    	verify(repository, times(1)).findOne(PLAYER_ID_EXISTENT);
    	verifyNoMoreInteractions(repository);
    }
    /* isEmailConfirmed - end */
    
    /* validateCreatePlayer - begin */
    @Test
    public void validateCreatePlayer_WhenNoDataIsMissing_ReturnMsgEmpty() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	player.setEmail("email@test.com");
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
    	Player player = new Player();
    	player.setName(null);
    	player.setEmail("email@test.com");
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />O nome precisa ser informado.", service.validateCreatePlayer(player));
    	
    	player.setName("");
    	player.setEmail("email@test.com");
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />O nome precisa ser informado.", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenFieldEmailIsMissing_ReturnMsgEmailMissing() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	player.setEmail(null);
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player));
    	
    	player.setName("Name Completed");
    	player.setEmail("");
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenFieldEmailIsInvalid_ReturnMsgEmailInvalid() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	player.setEmail("invalid_email");
    	player.setPassword("password123");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />O e-mail informado não é válido.", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsMissing_ReturnMsgPasswordMissing() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	player.setEmail("email@test.com");
    	player.setPassword(null);
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />A senha precisa ser informada.", service.validateCreatePlayer(player));
    	
    	player.setName("Name Completed");
    	player.setEmail("email@test.com");
    	player.setPassword("");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />A senha precisa ser informada.", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsInvalid_ReturnMsgPasswordInvalid() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	player.setEmail("email@test.com");
    	player.setPassword("12345");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />A senha precisa possuir entre 6 e 20 caracteres.", service.validateCreatePlayer(player));
    	
    	player.setName("Name Completed");
    	player.setEmail("email@test.com");
    	player.setPassword("123456");
    	
    	Assert.assertEquals("failure - expected msg returned equals", "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.", service.validateCreatePlayer(player));
    }
    
    @Test
    public void validateCreatePlayer_WhenMoreThanOneFielsIsMissing_ReturnMsgFieldsMissing() {
    	Player player = new Player();
    	player.setName("");
    	player.setEmail("");
    	player.setPassword("");
    	
    	String msg = "<br />O nome precisa ser informado.<br />O e-mail precisa ser informado.<br />A senha precisa ser informada.";
    	
    	Assert.assertEquals("failure - expected msg returned equals", msg, service.validateCreatePlayer(player));
    }
    /* validateCreatePlayer - end */
    
    /* validateUpdateProfile - begin */
    @Test
    public void validateUpdateProfile_WhenNoDataIsMissing_ReturnMsgEmpty() {
    	Player player = new Player();
    	player.setName("Name Completed");
    	Assert.assertEquals("failure - expected msg returned equals", "", service.validateUpdateProfile(player));
    }
    
    @Test
    public void validateUpdateProfile_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
    	Player player = new Player();
    	player.setName(null);
    	Assert.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));
    	
    	player.setName("");
    	Assert.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));
    }
    /* validateUpdateProfile - end */
}