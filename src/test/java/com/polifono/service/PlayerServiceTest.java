package com.polifono.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    	Player entity = new Player();
        entity.setName("Name of the Player");
        entity.setEmail("teste@email.com");
        entity.setPassword("password");
        
        return entity;
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
    }
    /* create - end */
    
    /* save - begin */
    @Test
    public void save_WhenSavePlayer_ReturnPlayerSaved() {
        Integer id = new Integer(PLAYER_ID_EXISTENT);
        Player entity = service.findOne(id);

        // Changing all possible fields.
        // id - not possible to change.
        // dtInc - not possible to change.
        entity.setActive(!entity.isActive());
        // email - not possible to change.
        //password
        entity.setName("Name Changed");
        entity.setScore(entity.getScore() + 1);
        entity.setCredit(entity.getCredit() + 1);
        // role - not possible to change.
        entity.setIndEmailConfirmed(!entity.isIndEmailConfirmed());
        entity.setEmailConfirmed("Email Confirmed Changed");
        entity.setPasswordReset("Password Reset Changed");
        entity.setPhone("000-000");
        entity.setSex(entity.getSex() == 2 ? 1 : 2);
        entity.setDtBirth(new Date());
        entity.setAddress("Address Changed");
        
        // Saving the changes.
        Player changedEntity = service.save(entity);

        Assert.assertNotNull("failure - expected not null", changedEntity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), changedEntity.getId());
        
        // Get the entity in the database with the changes.
        Player updatedEntity = service.findOne(changedEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
        Assert.assertEquals("failure - expected email attribute match", entity.getEmail(), updatedEntity.getEmail());
        //Assert.assertEquals("failure - expected password attribute match", service.encryptPassword(auxEntity.getPassword()), updatedEntity.getPassword());
        
        Assert.assertEquals("failure - expected dtInc attribute match", 0, entity.getDtInc().compareTo(updatedEntity.getDtInc()));
        Assert.assertEquals("failure - expected active attribute match", entity.isActive(), updatedEntity.isActive());
        Assert.assertEquals("failure - expected credit attribute match", entity.getCredit(), updatedEntity.getCredit());
        Assert.assertEquals("failure - expected role attribute match", entity.getRole(), updatedEntity.getRole());
        
        Assert.assertEquals("failure - expected score attribute match", entity.getScore(), updatedEntity.getScore());
        Assert.assertEquals("failure - expected indEmailConfirmed attribute match", entity.isIndEmailConfirmed(), updatedEntity.isIndEmailConfirmed());
        Assert.assertEquals("failure - expected emailConfirmed attribute match", entity.getEmailConfirmed(), updatedEntity.getEmailConfirmed());
        Assert.assertEquals("failure - expected passwordReset attribute match", entity.getPasswordReset(), updatedEntity.getPasswordReset());
        Assert.assertEquals("failure - expected phone attribute match", entity.getPhone(), updatedEntity.getPhone());
        Assert.assertEquals("failure - expected sex attribute match", entity.getSex(), updatedEntity.getSex());
        Assert.assertEquals("failure - expected address attribute match", entity.getAddress(), updatedEntity.getAddress());
    }
    /* save - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerIsExistent_ReturnPlayer() {
        Integer id = new Integer(PLAYER_ID_EXISTENT);
        Player entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", PLAYER_ID_EXISTENT.intValue(), entity.getId());
    }
    
    @Test
    public void findOne_WhenPlayerIsInexistent_ReturnNull() {
        Integer id = PLAYER_ID_INEXISTENT;
    	Player entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllPlayers_ReturnList() {
    	List<Player> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */

    /* findByEmail - begin */
    @Test
    public void findByEmail_WhenSearchByPlayerExistent_ReturnPlayer() {
    	Player entity = service.findByEmail(PLAYER_EMAIL_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entity.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entity.getId() > 0);
        Assert.assertNotNull("failure - expected dtInc not null", entity.getDtInc());
        Assert.assertNotNull("failure - expected active not null", entity.isActive());
        Assert.assertNotNull("failure - expected email not null", entity.getEmail());
        Assert.assertNotNull("failure - expected password not null", entity.getPassword());
        Assert.assertNotNull("failure - expected name not null", entity.getName());
        Assert.assertNotNull("failure - expected score not null", entity.getScore());
        Assert.assertNotNull("failure - expected credit not null", entity.getCredit());
        Assert.assertNotNull("failure - expected role not null", entity.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entity.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entity.getSex());
    }

    @Test
    public void findByEmail_WhenSearchByPlayerInexistent_ReturnNull() {
    	Player entity = service.findByEmail(PLAYER_EMAIL_INEXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByEmail - end */

    /* findByEmailAndStatus - begin */
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentAndByRightStatus_ReturnPlayer() {
    	Player entity = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entity.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entity.getId() > 0);
        Assert.assertNotNull("failure - expected dtInc not null", entity.getDtInc());
        Assert.assertNotNull("failure - expected active not null", entity.isActive());
        Assert.assertNotNull("failure - expected email not null", entity.getEmail());
        Assert.assertNotNull("failure - expected password not null", entity.getPassword());
        Assert.assertNotNull("failure - expected name not null", entity.getName());
        Assert.assertNotNull("failure - expected score not null", entity.getScore());
        Assert.assertNotNull("failure - expected credit not null", entity.getCredit());
        Assert.assertNotNull("failure - expected role not null", entity.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entity.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entity.getSex());
    }
    
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentButByWrongStatus_ReturnNull() {
    	Player entity = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void findByEmailAndStatus_WhenSearchPlayerInexistent_ReturnNull() {
    	Player entity = service.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByEmailAndStatus - end */
    
    /* findByEmailAndStatusForLogin - begin */
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentAndActive_ReturnPlayer() {
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true).orElse(new Player());
    	
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected email attribute match", PLAYER_EMAIL_EXISTENT, entity.getEmail());
        Assert.assertTrue("failure - expected id attribute greater than 0", entity.getId() > 0);
        Assert.assertNotNull("failure - expected dtInc not null", entity.getDtInc());
        Assert.assertNotNull("failure - expected active not null", entity.isActive());
        Assert.assertNotNull("failure - expected email not null", entity.getEmail());
        Assert.assertNotNull("failure - expected password not null", entity.getPassword());
        Assert.assertNotNull("failure - expected name not null", entity.getName());
        Assert.assertNotNull("failure - expected score not null", entity.getScore());
        Assert.assertNotNull("failure - expected credit not null", entity.getCredit());
        Assert.assertNotNull("failure - expected role not null", entity.getRole());
        Assert.assertNotNull("failure - expected indEmailConfirmed not null", entity.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected sex not null", entity.getSex());
    }
    
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentButWrongStatus_ReturnNull() {
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false).orElse(null);
        Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerInexistent_ReturnNull() {
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true).orElse(null);
    	Assert.assertNull("failure - expected null", entity);
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
    	int playerId = PLAYER_ID_EXISTENT;
    	
    	Player player = new Player();
    	player.setCredit(10);
    	
    	Player entity = new Player();
    	player.setCredit(22);
    	
    	when(service.findOne(playerId)).thenReturn(player);
    	when(service.save(player)).thenReturn(entity);
    	
    	int credits = entity.getCredit();
    	int qtdCredits = 12;
    	Player entityUpdated = service.addCreditsToPlayer(PLAYER_ID_EXISTENT, qtdCredits);
    	Assert.assertEquals("failure - expected current credits equals old credits plus qtdCredits", credits + qtdCredits, entityUpdated.getCredit());
    	
    }
    /* addCreditsToPlayer - end */
    
    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenEverythingIsOK_ReturnPlayerWithLessCredits() {
    	int playerId = PLAYER_ID_EXISTENT;
    	
    	Player player = new Player();
    	player.setCredit(22);
    	
    	Player entity = new Player();
    	player.setCredit(10);
    	
    	when(service.findOne(playerId)).thenReturn(player);
    	when(service.save(player)).thenReturn(entity);
    	
    	int credits = entity.getCredit();
    	int qtdCredits = 12;
    	Player entityUpdated = service.removeCreditsFromPlayer(PLAYER_ID_EXISTENT, qtdCredits);
    	Assert.assertEquals("failure - expected current credits equals old credits plus qtdCredits", credits - qtdCredits, entityUpdated.getCredit());
    	
    }
    /* removeCreditsFromPlayer - end */
    
    /* removeOneCreditFromPlayer - begin */
    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerHasSpecificCreditsOfTheGame_ReturnPlayerWithOneSpecificCreditLess() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Game game = new Game();
    	game.setId(1);
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setCredit(0);
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	PlayerGame pg = new PlayerGame();
    	pg.setCredit(20);
    	pg.setGame(game);
    	playerGameList.add(pg);
    	player.setPlayerGameList(playerGameList);
    	
    	PlayerGame playerGame = new PlayerGame();
    	when(playerGameService.removeCreditsFromPlayer(playerGame, 1)).thenReturn(null);
    	
    	Player playerReturned = new Player();
    	playerReturned.setId(id);
    	playerReturned.setCredit(0);
    	List<PlayerGame> playerGameListReturned = new ArrayList<PlayerGame>();
    	PlayerGame pgReturned = new PlayerGame();
    	pgReturned.setCredit(19);
    	pgReturned.setGame(game);
    	playerGameListReturned.add(pg);
    	playerReturned.setPlayerGameList(playerGameListReturned);
    	
    	when(repository.findOne(id)).thenReturn(playerReturned);
    	
    	Player entity = service.removeOneCreditFromPlayer(player, game);
    	
    	Assert.assertEquals(player.getCredit(), entity.getCredit());
    	Assert.assertEquals(player.getPlayerGameList().get(0).getCredit() - 1, entity.getPlayerGameList().get(0).getCredit());
    }
    
    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerDoesntHaveSpecificCreditsOfTheGame_ReturnPlayerWithOneGenericCreditLess() {
    	Player player = new Player();
    	player.setId(1);
    	player.setCredit(10);
    	
    	Game game = new Game();
    	
    	Player returned = new Player();
    	returned.setCredit(9);
    	
    	when(repository.save(player)).thenReturn(returned);
    	
    	Player entity = service.removeOneCreditFromPlayer(player, game);
    	
    	Assert.assertEquals(player.getCredit() - 1, entity.getCredit());
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
    	int id = PLAYER_ID_EXISTENT;
    	
    	Game game = new Game();
    	game.setId(1);
    	
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	PlayerGame pg = new PlayerGame();
    	pg.setGame(game);
    	pg.setCredit(10);
    	playerGameList.add(pg);
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setCredit(0);
    	player.setPlayerGameList(playerGameList);
    	
    	Phase phase = new Phase();
    	Map map = new Map();
    	map.setGame(game);
    	
    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertTrue(service.playerHasCredits(player, phase));
    }
    
    @Test
    public void playerHasCredits_WhenPlayerHasGenericAndSpecificCredits_ReturnTrue() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Game game = new Game();
    	game.setId(1);
    	
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	PlayerGame pg = new PlayerGame();
    	pg.setGame(game);
    	pg.setCredit(10);
    	playerGameList.add(pg);
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setCredit(20);
    	player.setPlayerGameList(playerGameList);
    	
    	Phase phase = new Phase();
    	Map map = new Map();
    	map.setGame(game);
    	
    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertTrue(service.playerHasCredits(player, phase));
    }
    
    @Test
    public void playerHasCredits_WhenPlayerHasNotCredits_ReturnFalse() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Game game = new Game();
    	game.setId(1);
    	
    	List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();
    	PlayerGame pg = new PlayerGame();
    	pg.setGame(game);
    	pg.setCredit(0);
    	playerGameList.add(pg);
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setCredit(0);
    	player.setPlayerGameList(playerGameList);
    	
    	Phase phase = new Phase();
    	Map map = new Map();
    	map.setGame(game);
    	
    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertFalse(service.playerHasCredits(player, phase));
    }
    /* playerHasCredits - end */
    
    /* isEmailConfirmed - begin */
    @Test
    public void isEmailConfirmed_WhenEmailIsConfirmed_ReturnTrue() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setIndEmailConfirmed(true);
    	
    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertTrue(service.isEmailConfirmed(player));
    }
    
    @Test
    public void isEmailConfirmed_WhenEmailIsNotConfirmed_ReturnFalse() {
    	int id = PLAYER_ID_EXISTENT;
    	
    	Player player = new Player();
    	player.setId(id);
    	player.setIndEmailConfirmed(false);
    	
    	when(repository.findOne(id)).thenReturn(player);
    	
    	Assert.assertTrue(service.isEmailConfirmed(player));
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