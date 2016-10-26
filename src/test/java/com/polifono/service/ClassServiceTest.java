package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import com.polifono.domain.Player;
import com.polifono.repository.IClassRepository;
import com.polifono.service.impl.ClassServiceImpl;

/**
 * Unit test methods for the ClassService.
 * 
 */
public class ClassServiceTest extends AbstractTest {

    private IClassService service;
    
    @Mock
    private IClassRepository repository;
	
	private final Integer CLASS_ID_EXISTENT = 1;
	private final Integer CLASS_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer TEACHER_ID_EXISTENT = 2;
	private final Integer TEACHER_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new ClassServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private com.polifono.domain.Class getEntityStubData() {
    	Player player = new Player();
    	player.setId(123);
    	
    	com.polifono.domain.Class clazz = new com.polifono.domain.Class();
    	clazz.setId(CLASS_ID_EXISTENT);
    	clazz.setName("Class Name");
    	clazz.setDescription("Class Description");
    	clazz.setPlayer(player);
    	
    	return clazz;
    }
    
    private List<com.polifono.domain.Class> getEntityListStubData() {
    	List<com.polifono.domain.Class> list = new ArrayList<com.polifono.domain.Class>();
    	
    	com.polifono.domain.Class entity1 = getEntityStubData();
    	com.polifono.domain.Class entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveClass_ReturnClassSaved() {
    	com.polifono.domain.Class entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	com.polifono.domain.Class entityReturned = service.save(entity);

    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), entityReturned.getId());
    	Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), entityReturned.getPlayer().getId());
    	Assert.assertEquals("failure - expected active attribute match", entity.isActive(), entityReturned.isActive());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
    	Assert.assertEquals("failure - expected description attribute match", entity.getDescription(), entityReturned.getDescription());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenClassIsExistent_ReturnTrue() {
    	com.polifono.domain.Class entity = getEntityStubData();
    	
    	when(repository.findOne(CLASS_ID_EXISTENT)).thenReturn(entity);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(CLASS_ID_EXISTENT));
    	
    	verify(repository, times(1)).findOne(CLASS_ID_EXISTENT);
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenClassIsInexistent_ReturnFalse() {
    	when(repository.findOne(CLASS_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected return false", service.delete(CLASS_ID_INEXISTENT));
    	
    	verify(repository, times(1)).findOne(CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenClassIsExistent_ReturnClass() {
    	com.polifono.domain.Class entity = getEntityStubData();
    	
    	when(repository.findOne(CLASS_ID_EXISTENT)).thenReturn(entity);
    	
    	com.polifono.domain.Class entityReturned = service.findOne(CLASS_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", CLASS_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(CLASS_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenClassIsInexistent_ReturnNull() {
    	when(repository.findOne(CLASS_ID_INEXISTENT)).thenReturn(null);
    	
    	com.polifono.domain.Class entity = service.findOne(CLASS_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
        
        verify(repository, times(1)).findOne(CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllClasses_ReturnList() {
    	List<com.polifono.domain.Class> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<com.polifono.domain.Class> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* prepareClassForCreation - begin */
    @Test
    public void prepareClassForCreation_WhenEverythingIsOk_ReturnClassWithDefaultValuesForCreation() {
    	// Default values for creation are:
    	// dtInc = new Date();
		// active = true;
    	com.polifono.domain.Class entity = getEntityStubData();
    	com.polifono.domain.Class entityReturned = service.prepareClassForCreation(entity);

        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", entity.getId(), entityReturned.getId());
        Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), entityReturned.getPlayer().getId());
        
        Assert.assertEquals("failure - expected dtInc attribute match", 0, entityReturned.getDtInc().compareTo(new Date()));
        Assert.assertEquals("failure - expected active attribute match", true, entityReturned.isActive());
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
        Assert.assertEquals("failure - expected description attribute match", entity.getDescription(), entityReturned.getDescription());
    }
    /* prepareClassForCreation - end */
    
    /* prepareClassForChangingStatus - begin */
    @Test
    public void prepareClassForChangingStatus_WhenEverythingIsOK_ReturnEntityWithStatusChanged() {
    	com.polifono.domain.Class entity = getEntityStubData();
    	
    	com.polifono.domain.Class entityChanged = service.prepareClassForChangingStatus(entity, true);
    	Assert.assertEquals("failure - expected attribute status match", true, entityChanged.isActive());
    	
    	entityChanged = service.prepareClassForChangingStatus(entity, false);
    	Assert.assertEquals("failure - expected attribute status match", false, entityChanged.isActive());
    }
    /* prepareClassForChangingStatus - end */
    
    /* findClassesByTeacherAndStatus - begin */
    @Test
    public void findClassesByTeacherAndStatus_WhenSearchByTeacherAndStatusExistents_ReturnList() {
    	List<com.polifono.domain.Class> list = getEntityListStubData();
    	
    	when(repository.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, true)).thenReturn(list);
    	
    	List<com.polifono.domain.Class> listReturned = service.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, true);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassesByTeacherAndStatus_WhenSearchByTeacherAndStatusInexistents_ReturnEmptyList() {
    	when(repository.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, false)).thenReturn(new ArrayList<com.polifono.domain.Class>());
    	
    	List<com.polifono.domain.Class> listReturned = service.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, false);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, false);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassesByTeacherAndStatus_WhenSearchByTeacherExistentButStatusInexistent_ReturnEmptyList() {
    	when(repository.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, false)).thenReturn(new ArrayList<com.polifono.domain.Class>());
    	
    	List<com.polifono.domain.Class> listReturned = service.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, false);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findClassesByTeacherAndStatus_WhenSearchStatusExistentButTeacherInexistent_ReturnEmptyList() {
    	when(repository.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, true)).thenReturn(new ArrayList<com.polifono.domain.Class>());
    	
    	List<com.polifono.domain.Class> listReturned = service.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, true);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    /* findClassesByTeacherAndStatus - end */
}