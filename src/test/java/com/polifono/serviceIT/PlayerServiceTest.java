package com.polifono.serviceIT;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Player;
import com.polifono.domain.Role;
import com.polifono.service.IPlayerService;

/**
 * Unit test methods for the PlayerService.
 * 
 */
@Transactional
public class PlayerServiceTest extends AbstractTest {

	@Autowired
    private IPlayerService service;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
	private final String PLAYER_EMAIL_INEXISTENT = "email_inexistent";

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* create - begin */
    @Test
    public void create_PlayerCreationWrong_ExceptionThrown() {
        Player entity = new Player();
        entity.setName("Name of New the Player");
        entity.setEmail("new@email.com");
        entity.setPassword("password");

        Player createdEntity = service.create(entity);

        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, createdEntity.getId());
        
        Player newEntity = service.findOne(createdEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), newEntity.getName());
        Assert.assertEquals("failure - expected email attribute match", entity.getEmail(), newEntity.getEmail());
        //Assert.assertEquals("failure - expected password attribute match", service.encryptPassword(entity.getPassword()), newEntity.getPassword());
        
        Assert.assertEquals("failure - expected dtInc attribute match", 0, newEntity.getDtInc().compareTo(createdEntity.getDtInc()));
        Assert.assertEquals("failure - expected active attribute match", true, newEntity.isActive());
        Assert.assertEquals("failure - expected credit attribute match", 30, newEntity.getCredit());
        Assert.assertEquals("failure - expected role attribute match", Role.USER, newEntity.getRole());
        
        Assert.assertEquals("failure - expected score attribute match", 0, newEntity.getScore());
        Assert.assertEquals("failure - expected indEmailConfirmed attribute match", false, newEntity.isIndEmailConfirmed());
        Assert.assertNotNull("failure - expected emailConfirmed attribute not null", newEntity.getEmailConfirmed());
        Assert.assertNull("failure - expected passwordReset attribute null", newEntity.getPasswordReset());
        Assert.assertNull("failure - expected phone attribute null", newEntity.getPhone());
        Assert.assertEquals("failure - expected sex attribute match", 0, newEntity.getSex());
        Assert.assertNull("failure - expected address attribute null", newEntity.getAddress());
    }
    /* create - end */
    
    /* save - begin */
    @Test
    public void save_UpdatePlayer() {
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
    public void findOne_PlayerExistentButReturnNull_ExceptionThrown() {
        Integer id = new Integer(PLAYER_ID_EXISTENT);
        Player entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findOne_PlayerExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(PLAYER_ID_EXISTENT);
        Player entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_PlayerInexistent_ReturnNull() {
        Integer id = PLAYER_ID_INEXISTENT;
    	Player entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_ListIsNull_ExceptionThrown() {
    	List<Player> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    }

    @Test
    public void findAll_ListHasSizeZero_ExceptionThrown() {
    	List<Player> list = service.findAll();
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */

    /* findByEmail - begin */
    @Test
    public void findByEmail_SearchPlayerExistent() {
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
    public void findByEmail_SearchPlayerInexistent() {
    	Player entity = service.findByEmail(PLAYER_EMAIL_INEXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByEmail - end */

    /* findByEmailAndStatus - begin */
    @Test
    public void findByEmailAndStatus_SearchPlayerExistentActive_ReturnPlayer() {
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
    public void findByEmailAndStatus_SearchPlayerExistentWrongStatus_ReturnNull() {
    	Player entity = service.findByEmailAndStatus(PLAYER_EMAIL_EXISTENT, false);
        Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void findByEmailAndStatus_SearchPlayerInexistent() {
    	Player entity = service.findByEmailAndStatus(PLAYER_EMAIL_INEXISTENT, true);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByEmailAndStatus - end */
    
    /* findByEmailAndStatusForLogin - begin */
    @Test
    public void findByEmailAndStatusForLogin_SearchPlayerExistentActive_ReturnPlayer() {
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
    public void findByEmailAndStatusForLogin_SearchPlayerExistentWrongStatus_ReturnNull() {
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_EXISTENT, false).orElse(null);
        Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void findByEmailAndStatusForLogin_SearchPlayerInexistent() {
    	Player entity = service.findByEmailAndStatusForLogin(PLAYER_EMAIL_INEXISTENT, true).orElse(null);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByEmailAndStatusForLogin - end */
    
    /* encryptPassword - begin */
    @Test
    public void encryptPassword() {
    	String rawPassword = "12345";
    	String ret = service.encryptPassword(rawPassword);
    	Assert.assertNotNull("failure - expected not null", ret);
    	Assert.assertTrue("failure - expected size bigger than 0", ret.length() > 0);
    }
    /* encryptPassword - end */
    
    /* addCreditsToPlayer - begin */
    @Test
    public void addCreditsToPlayer() {
    	Player entity = service.findOne(PLAYER_ID_EXISTENT);
    	int credits = entity.getCredit();
    	int qtdCredits = 12;
    	Player entityUpdated = service.addCreditsToPlayer(PLAYER_ID_EXISTENT, qtdCredits);
    	Assert.assertEquals("failure - expected current credits equals old credits plus qtdCredits", credits + qtdCredits, entityUpdated.getCredit());
    	
    }
    /* addCreditsToPlayer - end */
    
    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer() {
    	Player entity = service.findOne(PLAYER_ID_EXISTENT);
    	int credits = entity.getCredit();
    	int qtdCredits = 12;
    	Player entityUpdated = service.removeCreditsFromPlayer(PLAYER_ID_EXISTENT, qtdCredits);
    	Assert.assertEquals("failure - expected current credits equals old credits plus qtdCredits", credits - qtdCredits, entityUpdated.getCredit());
    	
    }
    /* removeCreditsFromPlayer - end */
}