package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.enums.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.impl.PlayerServiceImpl;
import com.polifono.util.StringUtil;

/**
 * Unit test methods for the PlayerService.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @InjectMocks
    private PlayerServiceImpl service;

    @Mock
    private IPlayerRepository repository;

    @Mock
    private IPlayerGameService playerGameService;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
    private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";

    private final Integer GAME_ID_EXISTENT = 2;

    /* stubs - begin */
    private Optional<Player> getEntityStubData() {
        List<PlayerGame> playerGameList = new ArrayList<PlayerGame>();

        Player entity = new Player();
        entity.setId(PLAYER_ID_EXISTENT);
        entity.setName("Name of the Player");
        entity.setEmail(PLAYER_EMAIL_EXISTENT);
        entity.setPassword("password");
        entity.setRole(Role.USER);

        entity.setPlayerGameList(playerGameList);

        return Optional.of(entity);
    }

    private List<Player> getEntityListStubData() {
        List<Player> list = new ArrayList<Player>();

        Player entity1 = getEntityStubData().get();
        Player entity2 = getEntityStubData().get();

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
    /*@Test
    public void create_WhenCreatePlayer_ReturnPlayerCreated() {
        Optional<Player> entity = getEntityStubData();

        when(repository.save(entity)).thenReturn(entity);

        Player entityReturned = service.create(entity);

        Assertions.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
        Assertions.assertEquals("failure - expected email attribute match", entity.getEmail(), entityReturned.getEmail());
        //Assertions.assertEquals("failure - expected password attribute match", service.encryptPassword(entity.getPassword()), newEntity.getPassword());

        Assertions.assertEquals("failure - expected dtInc attribute match", 0, entity.getDtInc().compareTo(entityReturned.getDtInc()));
        Assertions.assertEquals("failure - expected active attribute match", true, entityReturned.isActive());
        Assertions.assertEquals("failure - expected credit attribute match", 30, entityReturned.getCredit());
        Assertions.assertEquals("failure - expected role attribute match", Role.USER, entityReturned.getRole());

        Assertions.assertEquals("failure - expected score attribute match", 0, entityReturned.getScore());
        Assertions.assertEquals("failure - expected indEmailConfirmed attribute match", false, entityReturned.isIndEmailConfirmed());
        Assertions.assertNotNull("failure - expected emailConfirmed attribute not null", entityReturned.getEmailConfirmed());
        Assertions.assertNull("failure - expected passwordReset attribute null", entityReturned.getPasswordReset());
        Assertions.assertNull("failure - expected phone attribute null", entityReturned.getPhone());
        Assertions.assertEquals("failure - expected sex attribute match", 0, entityReturned.getSex());
        Assertions.assertNull("failure - expected address attribute null", entityReturned.getAddress());

        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }*/
    /* create - end */

    /* preparePlayerForCreation - begin */
    /* preparePlayerForCreation - end */

    /* save - begin */
    @Test
    public void save_WhenSavePlayer_ReturnPlayerSaved() {
        Player entity = getEntityStubData().get();

        when(repository.save(entity)).thenReturn(entity);

        Player entityReturned = service.save(entity);

        Assertions.assertEquals(entity.getName(), entityReturned.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.getEmail(), entityReturned.getEmail(), "failure - expected email attribute match");
        //Assertions.assertEquals("failure - expected password attribute match", service.encryptPassword(auxEntity.getPassword()), updatedEntity.getPassword());

        Assertions.assertEquals(entity.isActive(), entityReturned.isActive(), "failure - expected active attribute match");
        Assertions.assertEquals(entity.getCredit(), entityReturned.getCredit(), "failure - expected credit attribute match");
        Assertions.assertEquals(entity.getRole(), entityReturned.getRole(), "failure - expected role attribute match");

        Assertions.assertEquals(entity.getScore(), entityReturned.getScore(), "failure - expected score attribute match");
        Assertions.assertEquals(entity.isIndEmailConfirmed(), entityReturned.isIndEmailConfirmed(), "failure - expected indEmailConfirmed attribute match");
        Assertions.assertEquals(entity.getEmailConfirmed(), entityReturned.getEmailConfirmed(), "failure - expected emailConfirmed attribute match");
        Assertions.assertEquals(entity.getPasswordReset(), entityReturned.getPasswordReset(), "failure - expected passwordReset attribute match");
        Assertions.assertEquals(entity.getPhone(), entityReturned.getPhone(), "failure - expected phone attribute match");
        Assertions.assertEquals(entity.getSex(), entityReturned.getSex(), "failure - expected sex attribute match");
        Assertions.assertEquals(entity.getAddress(), entityReturned.getAddress(), "failure - expected address attribute match");

        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerIsExistent_ReturnPlayer() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);

        Optional<Player> entityReturned = service.findById(PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenPlayerIsInexistent_ReturnNull() {
        when(repository.findById(PLAYER_ID_INEXISTENT)).thenReturn(null);

        Optional<Player> entityReturned = service.findById(PLAYER_ID_INEXISTENT);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllPlayers_ReturnList() {
        List<Player> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Player> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByEmail - begin */
    @Test
    public void findByEmail_WhenSearchByPlayerExistent_ReturnPlayer() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findByEmail(PLAYER_EMAIL_EXISTENT)).thenReturn(entity.get());

        Player entityReturned = service.findByEmail(PLAYER_EMAIL_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_EMAIL_EXISTENT, entityReturned.getEmail(), "failure - expected email attribute match");
        Assertions.assertTrue(entityReturned.getId() > 0, "failure - expected id attribute greater than 0");
        Assertions.assertNotNull(entityReturned.isActive(), "failure - expected active not null");
        Assertions.assertNotNull(entityReturned.getPassword(), "failure - expected password not null");
        Assertions.assertNotNull(entityReturned.getName(), "failure - expected name not null");
        Assertions.assertNotNull(entityReturned.getScore(), "failure - expected score not null");
        Assertions.assertNotNull(entityReturned.getCredit(), "failure - expected credit not null");
        Assertions.assertNotNull(entityReturned.getRole(), "failure - expected role not null");
        Assertions.assertNotNull(entityReturned.isIndEmailConfirmed(), "failure - expected indEmailConfirmed not null");
        Assertions.assertNotNull(entityReturned.getSex(), "failure - expected sex not null");

        verify(repository, times(1)).findByEmail(PLAYER_EMAIL_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByEmail_WhenSearchByPlayerInexistent_ReturnNull() {
        when(repository.findByEmail(PLAYER_EMAIL_INEXISTENT)).thenReturn(null);

        Player entityReturned = service.findByEmail(PLAYER_EMAIL_INEXISTENT);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findByEmail(PLAYER_EMAIL_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmail - end */

    /* findByEmailAndStatus - begin */
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentAndByRightStatus_ReturnPlayer() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findByEmailAndActive(PLAYER_EMAIL_EXISTENT, true)).thenReturn(entity.get());

        Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_EMAIL_EXISTENT, entityReturned.getEmail(), "failure - expected email attribute match");
        Assertions.assertTrue(entityReturned.getId() > 0, "failure - expected id attribute greater than 0");
        Assertions.assertNotNull(entityReturned.isActive(), "failure - expected active not null");
        Assertions.assertNotNull(entityReturned.getPassword(), "failure - expected password not null");
        Assertions.assertNotNull(entityReturned.getName(), "failure - expected name not null");
        Assertions.assertNotNull(entityReturned.getScore(), "failure - expected score not null");
        Assertions.assertNotNull(entityReturned.getCredit(), "failure - expected credit not null");
        Assertions.assertNotNull(entityReturned.getRole(), "failure - expected role not null");
        Assertions.assertNotNull(entityReturned.isIndEmailConfirmed(), "failure - expected indEmailConfirmed not null");
        Assertions.assertNotNull(entityReturned.getSex(), "failure - expected sex not null");

        verify(repository, times(1)).findByEmailAndActive(PLAYER_EMAIL_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentButByWrongStatus_ReturnNull() {
        when(repository.findByEmailAndActive(PLAYER_EMAIL_EXISTENT, false)).thenReturn(null);

        Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findByEmailAndActive(PLAYER_EMAIL_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByEmailAndStatus_WhenSearchPlayerInexistent_ReturnNull() {
        when(repository.findByEmailAndActive(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(null);

        Player entityReturned = service.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findByEmailAndActive(PLAYER_EMAIL_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmailAndStatus - end */

    /* findByEmailAndStatusForLogin - begin */
    @Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentAndActive_ReturnPlayer() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true)).thenReturn(entity);

        Player entityReturned = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true).orElse(new Player());

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_EMAIL_EXISTENT, entityReturned.getEmail(), "failure - expected email attribute match");
        Assertions.assertTrue(entityReturned.getId() > 0, "failure - expected id attribute greater than 0");
        Assertions.assertNotNull(entityReturned.isActive(), "failure - expected active not null");
        Assertions.assertNotNull(entityReturned.getPassword(), "failure - expected password not null");
        Assertions.assertNotNull(entityReturned.getName(), "failure - expected name not null");
        Assertions.assertNotNull(entityReturned.getScore(), "failure - expected score not null");
        Assertions.assertNotNull(entityReturned.getCredit(), "failure - expected credit not null");
        Assertions.assertNotNull(entityReturned.getRole(), "failure - expected role not null");
        Assertions.assertNotNull(entityReturned.isIndEmailConfirmed(), "failure - expected indEmailConfirmed not null");
        Assertions.assertNotNull(entityReturned.getSex(), "failure - expected sex not null");

        verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }

    /*@Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerExistentButWrongStatus_ReturnNull() {
        when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false)).thenReturn(Optional.empty());

        Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false).orElse(null);
        Assertions.assertNull("failure - expected null", entity);

        verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }*/

    /*@Test
    public void findByEmailAndStatusForLogin_WhenSearchByPlayerInexistent_ReturnNull() {
        when(repository.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(Optional.empty());

        Player entityReturned = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true).orElse(null);
        Assertions.assertNull("failure - expected null", entityReturned);

        verify(repository, times(1)).findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }*/
    /* findByEmailAndStatusForLogin - end */

    /* encryptPassword - begin */
    @Test
    public void encryptPassword_WhenEverythingIsOK_ReturnTextEncrypted() {
        String rawPassword = "12345";
        String ret = StringUtil.encryptPassword(rawPassword);
        Assertions.assertNotNull("failure - expected not null", ret);
        Assertions.assertTrue(ret.length() > 0, "failure - expected size bigger than 0");
    }
    /* encryptPassword - end */

    /* addCreditsToPlayer - begin */
    @Test
    public void addCreditsToPlayer_WhenEverythingIsOK_ReturnPlayerWithMoreCredits() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(repository.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.addCreditsToPlayer(PLAYER_ID_EXISTENT, 10);

        Assertions.assertNotNull(entityReturned, "failure - expected null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* addCreditsToPlayer - end */

    /* preparePlayerForAddingCredits - begin */
    /* preparePlayerForAddingCredits - end */

    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenEverythingIsOK_ReturnPlayerWithLessCredits() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(repository.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.removeCreditsFromPlayer(PLAYER_ID_EXISTENT, 5);

        Assertions.assertNotNull(entityReturned, "failure - expected null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(repository, times(1)).save(entity.get());
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
        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Player entityReturned = service.removeOneCreditFromPlayer(entity, game);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(playerGameService, times(1)).removeCreditsFromPlayer(playerGame, 1);
        verifyNoMoreInteractions(playerGameService);
        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerDoesntHaveSpecificCreditsOfTheGame_ReturnPlayerWithOneGenericCreditLess() {
        Optional<Player> entity = getEntityStubData();

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(repository.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.removeOneCreditFromPlayer(entity.get(), game);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(repository, times(1)).save(entity.get());
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

        when(repository.findById(id)).thenReturn(Optional.of(player));

        Assertions.assertTrue(service.playerHasCredits(player, phase));
    }

    @Test
    public void playerHasCredits_WhenPlayerHasSpecificCredits_ReturnTrue() {
        Player entity = getPlayerWithSpecificCreditsStubData();
        entity.setCredit(0); // Assuring that the player has no generic credits.

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Assertions.assertTrue(service.playerHasCredits(entity, getPhaseStubData()));

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void playerHasCredits_WhenPlayerHasGenericAndSpecificCredits_ReturnTrue() {
        Player entity = getPlayerWithSpecificCreditsStubData();
        entity.setCredit(30); // Assuring that the player has generic credits.

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Assertions.assertTrue(service.playerHasCredits(entity, getPhaseStubData()));

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void playerHasCredits_WhenPlayerHasNotCredits_ReturnFalse() {
        Optional<Player> entity = getEntityStubData();
        entity.get().setCredit(0); // Assuring that the player has no generic credits.

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);

        Assertions.assertFalse(service.playerHasCredits(entity.get(), getPhaseStubData()));

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* playerHasCredits - end */

    /* isEmailConfirmed - begin */
    @Test
    public void isEmailConfirmed_WhenEmailIsConfirmed_ReturnTrue() {
        Optional<Player> entity = getEntityStubData();
        entity.get().setIndEmailConfirmed(true);

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);

        Assertions.assertTrue(service.isEmailConfirmed(entity.get()));

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void isEmailConfirmed_WhenEmailIsNotConfirmed_ReturnFalse() {
        Optional<Player> entity = getEntityStubData();
        entity.get().setIndEmailConfirmed(false);

        when(repository.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);

        Assertions.assertFalse(service.isEmailConfirmed(entity.get()));

        verify(repository, times(1)).findById(PLAYER_ID_EXISTENT);
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

        Assertions.assertEquals("failure - expected msg returned equals", "", service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
        Player player = new Player();
        player.setName(null);
        player.setEmail("email@test.com");
        player.setPassword("password123");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />O nome precisa ser informado.", service.validateCreatePlayer(player));

        player.setName("");
        player.setEmail("email@test.com");
        player.setPassword("password123");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />O nome precisa ser informado.", service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenFieldEmailIsMissing_ReturnMsgEmailMissing() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail(null);
        player.setPassword("password123");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player));

        player.setName("Name Completed");
        player.setEmail("");
        player.setPassword("password123");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenFieldEmailIsInvalid_ReturnMsgEmailInvalid() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("invalid_email");
        player.setPassword("password123");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />O e-mail informado não é válido.", service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsMissing_ReturnMsgPasswordMissing() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword(null);

        Assertions.assertEquals("failure - expected msg returned equals", "<br />A senha precisa ser informada.", service.validateCreatePlayer(player));

        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />A senha precisa ser informada.", service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsInvalid_ReturnMsgPasswordInvalid() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("12345");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />A senha precisa possuir entre 6 e 20 caracteres.",
                service.validateCreatePlayer(player));

        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("123456");

        Assertions.assertEquals("failure - expected msg returned equals", "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.",
                service.validateCreatePlayer(player));
    }

    @Test
    public void validateCreatePlayer_WhenMoreThanOneFielsIsMissing_ReturnMsgFieldsMissing() {
        Player player = new Player();
        player.setName("");
        player.setEmail("");
        player.setPassword("");

        String msg = "<br />O nome precisa ser informado.<br />O e-mail precisa ser informado.<br />A senha precisa ser informada.";

        Assertions.assertEquals("failure - expected msg returned equals", msg, service.validateCreatePlayer(player));
    }
    /* validateCreatePlayer - end */

    /* validateUpdateProfile - begin */
    /*@Test
    public void validateUpdateProfile_WhenNoDataIsMissing_ReturnMsgEmpty() {
        Player player = new Player();
        player.setName("Name Completed");
        Assertions.assertEquals("failure - expected msg returned equals", "", service.validateUpdateProfile(player));
    }*/

    /*@Test
    public void validateUpdateProfile_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
        Player player = new Player();
        player.setName(null);
        Assertions.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));

        player.setName("");
        Assertions.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));
    }*/
    /* validateUpdateProfile - end */
}
