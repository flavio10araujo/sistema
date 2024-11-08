package com.polifono.service.impl.player;

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

import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.enums.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.util.PasswordUtil;

/**
 * Unit test methods for the PlayerService.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService service;

    @Mock
    private IPlayerRepository repository;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
    private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";

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

        when(repository.findByEmail(PLAYER_EMAIL_EXISTENT)).thenReturn(entity);

        Optional<Player> entityReturnedOpt = service.findByEmail(PLAYER_EMAIL_EXISTENT);

        Player entityReturned = entityReturnedOpt.orElse(new Player());

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
        when(repository.findByEmail(PLAYER_EMAIL_INEXISTENT)).thenReturn(Optional.empty());

        Optional<Player> entityReturnedOpt = service.findByEmail(PLAYER_EMAIL_INEXISTENT);
        Player entityReturned = entityReturnedOpt.orElse(null);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findByEmail(PLAYER_EMAIL_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByEmail - end */

    /* findByEmailAndStatus - begin */
    @Test
    public void findByEmailAndStatus_WhenSearchByPlayerExistentAndByRightStatus_ReturnPlayer() {
        Optional<Player> entity = getEntityStubData();

        when(repository.findByEmailAndActive(PLAYER_EMAIL_EXISTENT, true)).thenReturn(entity);

        Optional<Player> entityReturnedOpt = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, true);
        Player entityReturned = entityReturnedOpt.orElse(new Player());

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
        when(repository.findByEmailAndActive(PLAYER_EMAIL_EXISTENT, false)).thenReturn(Optional.empty());

        Optional<Player> entityReturnedOpt = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        Player entityReturned = entityReturnedOpt.orElse(null);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findByEmailAndActive(PLAYER_EMAIL_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByEmailAndStatus_WhenSearchPlayerInexistent_ReturnNull() {
        when(repository.findByEmailAndActive(PLAYER_EMAIL_INEXISTENT, true)).thenReturn(Optional.empty());

        Optional<Player> entityReturnedOpt = service.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
        Player entityReturned = entityReturnedOpt.orElse(null);
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
        String ret = PasswordUtil.encryptPassword(rawPassword);
        Assertions.assertNotNull("failure - expected not null", ret);
        Assertions.assertTrue(ret.length() > 0, "failure - expected size bigger than 0");
    }
    /* encryptPassword - end */

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

    /* stubs - begin */
    private Optional<Player> getEntityStubData() {
        List<PlayerGame> playerGameList = new ArrayList<>();

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
        List<Player> list = new ArrayList<>();

        Player entity1 = getEntityStubData().get();
        Player entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
