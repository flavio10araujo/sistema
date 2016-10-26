package com.polifono.service;

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
import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.repository.IClassPlayerRepository;
import com.polifono.service.impl.ClassPlayerServiceImpl;

import static org.mockito.Mockito.*;

/**
 * Unit test methods for the ClassPlayerService.
 * 
 */
public class ClassPlayerServiceTest extends AbstractTest {

    private IClassPlayerService service;
    
    @Mock
    private IClassPlayerRepository repository;
	
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
    	MockitoAnnotations.initMocks(this);
    	service = new ClassPlayerServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private ClassPlayer getEntityStubData() {
    	com.polifono.domain.Class clazz = new com.polifono.domain.Class();
    	clazz.setId(CLASS_ID_EXISTENT);
    	
    	Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);
    	
        ClassPlayer entity = new ClassPlayer();
        entity.setId(CLASSPLAYER_ID_EXISTENT);
        entity.setClazz(clazz);
        entity.setPlayer(player);
        
        return entity;
    }
    
    private List<ClassPlayer> getEntityListStubData() {
    	List<ClassPlayer> list = new ArrayList<ClassPlayer>();
    	
    	ClassPlayer entity1 = getEntityStubData();
    	ClassPlayer entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveClassPlayer_ReturnClassPlayerSaved() {
    	ClassPlayer entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	ClassPlayer entitySaved = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), entitySaved.getId());
    	Assert.assertEquals("failure - expected active attribute match", entity.isActive(), entitySaved.isActive());
    	Assert.assertEquals("failure - expected dt exc attribute match", entity.getDtExc(), entitySaved.getDtExc());
    	Assert.assertEquals("failure - expected status attribute match", entity.getStatus(), entitySaved.getStatus());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenClassPlayerIsExistent_ReturnTrue() {
    	ClassPlayer entity = getEntityStubData();
    	
    	when(repository.findOne(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(CLASSPLAYER_ID_EXISTENT));
    	
    	verify(repository, times(1)).findOne(CLASSPLAYER_ID_EXISTENT);
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenClassPlayerIsInexistent_ReturnFalse() {
    	when(repository.findOne(CLASSPLAYER_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected return false", service.delete(CLASSPLAYER_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenClassPlayerIsExistent_ReturnClassPlayer() {
    	ClassPlayer entity = getEntityStubData();
    	
    	when(repository.findOne(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);
    	
    	ClassPlayer entityReturned = service.findOne(CLASSPLAYER_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", CLASSPLAYER_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(CLASSPLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenClassPlayerIsInexistent_ReturnNull() {
    	when(repository.findOne(CLASSPLAYER_ID_EXISTENT)).thenReturn(null);
    	
    	ClassPlayer entityReturned = service.findOne(CLASSPLAYER_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(CLASSPLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllClassPlayers_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* prepareClassPlayerToCreation - begin */
    @Test
    public void prepareClassPlayerToCreation_WhenEverythingIsOk_ReturnClassPlayerWithDefaultValuesForCreation() {
    	// Default values for creation are:
    	// dtInc = new Date();
		// active = true;
		// status = 1;
    	ClassPlayer entity = getEntityStubData();
    	ClassPlayer entityReturned = service.prepareClassPlayerForCreation(entity);

        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", entity.getId(), entityReturned.getId());
        Assert.assertEquals("failure - expected class attribute match", entity.getClazz().getId(), entityReturned.getClazz().getId());
        Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), entityReturned.getPlayer().getId());
        
        Assert.assertEquals("failure - expected dtInc attribute match", 0, entityReturned.getDtInc().compareTo(new Date()));
        Assert.assertEquals("failure - expected active attribute match", true, entityReturned.isActive());
        Assert.assertNull("failure - expected null", entityReturned.getDtExc());
        Assert.assertEquals("failure - expected status attribute match", 1, entityReturned.getStatus());
    }
    /* prepareClassPlayerToCreation - end */

    /* changeStatus - begin */
    @Test
    public void changeStatus_WhenClassPlayerIsExistent_ReturnTrue() {
    	ClassPlayer entity = getEntityStubData();
    	
    	when(repository.findOne(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Assert.assertTrue("failure - expected true", service.changeStatus(CLASSPLAYER_ID_EXISTENT.intValue(), STATUS_CONFIRMED));

    	verify(repository, times(1)).save(entity);
        //verifyNoMoreInteractions(repository);
    }

    @Test
    public void changeStatus_WhenClassPlayerIsInexistent_ReturnFalse() {
    	when(repository.findOne(CLASSPLAYER_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected false", service.changeStatus(CLASSPLAYER_ID_INEXISTENT.intValue(), STATUS_CONFIRMED));
    }
    /* changeStatus - end */
    
    /* prepareClassPlayerToChangeStatus - begin */
    @Test
    public void prepareClassPlayerToChangeStatus_WhenEverythingIsOK_ReturnEntityWithStatusChanged() {
    	ClassPlayer entity = getEntityStubData();
    	
    	ClassPlayer entityChanged = service.prepareClassPlayerToChangeStatus(entity, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_CONFIRMED.intValue(), entityChanged.getStatus());
    	
    	entityChanged = service.prepareClassPlayerToChangeStatus(entity, STATUS_DISABLED);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_DISABLED.intValue(), entityChanged.getStatus());
    }
    /* prepareClassPlayerToChangeStatus - end */
    
    /* findClassPlayersByTeacher - begin */
    @Test
    public void findClassPlayersByTeacher_WhenSearchByTeacherExistent_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByTeacher(TEACHER_ID_EXISTENT)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacher(TEACHER_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByTeacher(TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacher_WhenSearchByTeacherInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacher(TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacher(TEACHER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacher(TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacher - end */
    
    /* findClassPlayersByTeacherAndClass - begin */
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherAndClassExistents_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_EXISTENT)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherAndClassInexistents_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByTeacherExistentButClassInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndClass(TEACHER_ID_EXISTENT, CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByTeacherAndClass_WhenSearchByClassExistentButTeacherInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_EXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndClass(TEACHER_ID_INEXISTENT, CLASS_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacherAndClass - end */
    
    /* findClassPlayersByClassAndStatus - begin */
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassAndStatusExistents_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassAndStatusInexistents_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, 0)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByClassExistentButStatusInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, 0)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndStatus(CLASS_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByClassAndStatus_WhenSearchByStatusExistentButClassInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> list = service.findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByClassAndStatus - end */
    
    /* findClassPlayersByClassAndPlayer - begin */
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassAndPlayerExistents_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassAndPlayerInexistents_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByClassExistentButPlayerInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndPlayer(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByClassAndPlayer_WhenSearchByPlayerExistentButClassInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByClassAndPlayer(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByClassAndPlayer - end */
    
    /* findClassPlayersByPlayerAndStatus - begin */
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerAndStatusExistents_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerAndStatusInexistents_ReturnEmptyList() {
    	when(repository.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, 0)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByPlayerExistentButStatusInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, 0)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, 0);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByPlayerAndStatus(PLAYER_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByPlayerAndStatus_WhenSearchByStatusExistentButPlayerInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByPlayerAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByPlayerAndStatus - end */
    
    /* findClassPlayersByTeacherAndStudent - begin */
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherAndStudentExistents_ReturnList() {
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT)).thenReturn(list);
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherAndStudentInexistents_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByTeacherExistentButStudentInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassPlayersByTeacherAndStudent_WhenSearchByStudentExistentButTeacherInexistent_ReturnEmptyList() {
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	List<ClassPlayer> listReturned = service.findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacherAndStudent - end */
    
    /* isMyStudent - begin */
    @Test
    public void isMyStudent_WhenItIsHisStudent_ReturnTrue() {
    	Player teacher = new Player();
    	teacher.setId(TEACHER_ID_EXISTENT);
    	
    	Player student = new Player();
    	student.setId(PLAYER_ID_EXISTENT);
    	
    	List<ClassPlayer> list = getEntityListStubData();
    	
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, PLAYER_ID_EXISTENT)).thenReturn(list);
    	
    	Assert.assertTrue(service.isMyStudent(teacher, student));
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void isMyStudent_WhenItIsNotHisStudent_ReturnFalse() {
    	Player teacher = new Player();
    	teacher.setId(TEACHER_ID_EXISTENT);
    	
    	Player student = new Player();
    	student.setId(PLAYER_ID_INEXISTENT);
    	
    	when(repository.findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<ClassPlayer>());
    	
    	Assert.assertFalse(service.isMyStudent(teacher, student));
    	
    	verify(repository, times(1)).findClassPlayersByTeacherAndStudent(TEACHER_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* isMyStudent - end */
}