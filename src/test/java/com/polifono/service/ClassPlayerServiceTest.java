package com.polifono.service;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

/**
 * Unit test methods for the ClassPlayerService.
 * 
 */
@Transactional
public class ClassPlayerServiceTest extends AbstractTest {

	@Autowired
    private IClassPlayerService service;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer CLASS_ID_EXISTENT = 1;
	private final Integer CLASS_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer CLASSPLAYER_ID_EXISTENT = 1;
	private final Integer CLASSPLAYER_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer STATUS_CONFIRMED = 2;
	private final Integer STATUS_DISABLED = 3;
	
	private final Integer TEACHER_ID_EXISTENT = 2;
	private final Integer TEACHER_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer STUDENT_ID_EXISTENT = 1;
	private final Integer STUDENT_ID_INEXISTENT = Integer.MAX_VALUE;

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
    public void create_WhenCreateClassPlayer_ReturnClassPlayerCreated() {
    	
    	com.polifono.domain.Class clazz = new com.polifono.domain.Class();
    	clazz.setId(CLASS_ID_EXISTENT);
    	
    	Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);
    	
        ClassPlayer entity = new ClassPlayer();
        entity.setClazz(clazz);
        entity.setPlayer(player);

        ClassPlayer createdEntity = service.create(entity);

        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, createdEntity.getId());
        
        ClassPlayer newEntity = service.findOne(createdEntity.getId());
        
        Assert.assertEquals("failure - expected class attribute match", entity.getClazz().getId(), newEntity.getClazz().getId());
        Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), newEntity.getPlayer().getId());
        Assert.assertEquals("failure - expected dtInc attribute match", 0, newEntity.getDtInc().compareTo(createdEntity.getDtInc()));
        Assert.assertEquals("failure - expected active attribute match", true, newEntity.isActive());
        Assert.assertNull("failure - expected null", newEntity.getDtExc());
        Assert.assertEquals("failure - expected status attribute match", 1, newEntity.getStatus());
    }
    /* create - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveClassPlayer_ReturnClassPlayerSaved() {
    	ClassPlayer entity = service.findOne(CLASSPLAYER_ID_EXISTENT); 

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	// Changing all possible fields.
    	entity.setActive(!entity.isActive());
    	entity.setDtExc(new Date());
    	entity.setStatus(STATUS_CONFIRMED);
    	
    	ClassPlayer updatedEntity = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", updatedEntity);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), updatedEntity.getId());
    	Assert.assertEquals("failure - expected active attribute match", entity.isActive(), updatedEntity.isActive());
    	Assert.assertEquals("failure - expected dt exc attribute match", entity.getDtExc(), updatedEntity.getDtExc());
    	Assert.assertEquals("failure - expected status attribute match", entity.getStatus(), updatedEntity.getStatus());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenClassPlayerIsExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(CLASSPLAYER_ID_EXISTENT));
    	ClassPlayer entity = service.findOne(CLASSPLAYER_ID_EXISTENT);
    	Assert.assertEquals("failure - expected status attribute match", STATUS_DISABLED.intValue(), entity.getStatus());
    }

    @Test
    public void delete_WhenClassPlayerIsInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(CLASSPLAYER_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenClassPlayerIsExistent_ReturnClassPlayer() {
    	ClassPlayer entity = service.findOne(CLASSPLAYER_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", CLASSPLAYER_ID_EXISTENT.intValue(), entity.getId());
    }

    @Test
    public void findOne_WhenClassPlayerIsInexistent_ReturnNull() {
    	ClassPlayer entity = service.findOne(CLASSPLAYER_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllClassPlayers_ReturnList() {
    	List<ClassPlayer> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */

    /* changeStatus - begin */
    @Test
    public void changeStatus_WhenClassPlayerIsExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected true", service.changeStatus(CLASSPLAYER_ID_EXISTENT.intValue(), STATUS_CONFIRMED));

    	ClassPlayer entity = service.findOne(CLASSPLAYER_ID_EXISTENT);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_CONFIRMED.intValue(), entity.getStatus());
    	
    	Assert.assertTrue("failure - expected true", service.changeStatus(CLASSPLAYER_ID_EXISTENT.intValue(), STATUS_DISABLED));
    	
    	entity = service.findOne(CLASSPLAYER_ID_EXISTENT);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_DISABLED.intValue(), entity.getStatus());
    }

    @Test
    public void changeStatus_WhenClassPlayerIsInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected false", service.changeStatus(CLASSPLAYER_ID_INEXISTENT.intValue(), STATUS_CONFIRMED));
    }
    /* changeStatus - end */
    
    /* findClassPlayersByTeacher - begin */
    @Test
    public void findClassPlayersByTeacher_WhenSearchByTeacherExistent_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacher(TEACHER_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByTeacher_WhenSearchByTeacherInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacher(TEACHER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByTeacher - end */
    
    /* findClassPlayersByTeacherAndClass - begin */
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherAndClassExistents_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherAndClassInexistents_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherExistentButClassInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByClassExistentButTeacherInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByTeacherAndClass - end */
    
    /* findClassPlayersByClassAndStatus - begin */
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassAndStatusExistents_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassAndStatusInexistents_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassExistentButStatusInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByStatusExistentButClassInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByClassAndStatus - end */
    
    /* findClassPlayersByClassAndPlayer - begin */
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassAndPlayerExistents_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassAndPlayerInexistents_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassExistentButPlayerInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByPlayerExistentButClassInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByClassAndPlayer - end */
    
    /* findClassPlayersByPlayerAndStatus - begin */
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerAndStatusExistents_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerAndStatusInexistents_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerExistentButStatusInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByStatusExistentButPlayerInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByPlayerAndStatus - end */
    
    /* findClassPlayersByTeacherAndStudent - begin */
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherAndStudentExistents_ReturnList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherAndStudentInexistents_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherExistentButStudentInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByStudentExistentButTeacherInexistent_ReturnEmptyList() {
    	List<ClassPlayer> list = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassPlayersByTeacherAndStudent - end */
    
    /* isMyStudent - begin */
    @Test
    public void isMyStudent_WhenItIsHisStudent_ReturnTrue() {
    	Player teacher = new Player();
    	teacher.setId(TEACHER_ID_EXISTENT);
    	
    	Player student = new Player();
    	student.setId(PLAYER_ID_EXISTENT);
    	
    	Assert.assertTrue(service.isMyStudent(teacher, student));
    }
    
    @Test
    public void isMyStudent_WhenItIsNotHisStudent_ReturnFalse() {
    	Player teacher = new Player();
    	teacher.setId(TEACHER_ID_EXISTENT);
    	
    	Player student = new Player();
    	student.setId(PLAYER_ID_INEXISTENT);
    	
    	Assert.assertFalse(service.isMyStudent(teacher, student));
    }
    /* isMyStudent - end */
}