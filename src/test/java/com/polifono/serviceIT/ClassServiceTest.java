package com.polifono.serviceIT;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Player;
import com.polifono.service.IClassService;

/**
 * Unit test methods for the ClassService.
 * 
 */
@Transactional
public class ClassServiceTest extends AbstractTest {

	@Autowired
    private IClassService service;
	
	private final Integer CLASS_ID_EXISTENT = 1;
	private final Integer CLASS_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer TEACHER_ID_EXISTENT = 2;
	private final Integer TEACHER_ID_INEXISTENT = Integer.MAX_VALUE;

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
    public void create_ClassCreationWrong_ExceptionThrown() {
    	Player player = new Player();
        player.setId(TEACHER_ID_EXISTENT);
    	
    	com.polifono.domain.Class entity = new com.polifono.domain.Class();
        entity.setName("CLASS NAME");
        entity.setDescription("CLASS DESCRIPTION");

        com.polifono.domain.Class createdEntity = service.create(entity, player);

        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, createdEntity.getId());
        
        com.polifono.domain.Class newEntity = service.findOne(createdEntity.getId());
        
        Assert.assertEquals("failure - expected player attribute match", TEACHER_ID_EXISTENT.intValue(), newEntity.getPlayer().getId());
        Assert.assertEquals("failure - expected dtInc attribute match", 0, newEntity.getDtInc().compareTo(createdEntity.getDtInc()));
        Assert.assertEquals("failure - expected active attribute match", true, newEntity.isActive());
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), newEntity.getName());
        Assert.assertEquals("failure - expected description attribute match", entity.getDescription(), newEntity.getDescription());
    }
    /* create - end */
    
    /* save - begin */
    @Test
    public void save() {
    	com.polifono.domain.Class entity = service.findOne(CLASS_ID_EXISTENT);

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());

    	// Changing all possible fields.
    	entity.setActive(!entity.isActive());
    	entity.setName(entity.getName() + " CHANGED");
    	entity.setDescription(entity.getDescription() + " CHANGED");
    	
    	com.polifono.domain.Class updatedEntity = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", updatedEntity);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), updatedEntity.getId());
    	Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), updatedEntity.getPlayer().getId());
    	Assert.assertEquals("failure - expected dtInc attribute match", 0, entity.getDtInc().compareTo(updatedEntity.getDtInc()));
    	Assert.assertEquals("failure - expected active attribute match", entity.isActive(), updatedEntity.isActive());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
    	Assert.assertEquals("failure - expected description attribute match", entity.getDescription(), updatedEntity.getDescription());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_ClassExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(CLASS_ID_EXISTENT));
    	com.polifono.domain.Class entity = service.findOne(CLASS_ID_EXISTENT);
    	Assert.assertEquals("failure - expected active attribute match", false, entity.isActive());
    }

    @Test
    public void delete_ClassInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(CLASS_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_ClassExistentButReturnNull_ExceptionThrown() {
    	com.polifono.domain.Class entity = service.findOne(CLASS_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }

    @Test
    public void findOne_ClassExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(CLASS_ID_EXISTENT);
        com.polifono.domain.Class entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_ClassInexistent_ReturnNull() {
    	com.polifono.domain.Class entity = service.findOne(CLASS_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_ListIsNullOrEmpty_ExceptionThrown() {
    	List<com.polifono.domain.Class> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findClassesByTeacherAndStatus - begin */
    @Test
    public void findClassesByTeacherAndStatus_SearchTeacherAndStatusExistents_ReturnList() {
    	List<com.polifono.domain.Class> list = service.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, true);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findClassesByTeacherAndStatus_SearchTeacherAndStatusInexistents_ReturnListEmpty() {
    	List<com.polifono.domain.Class> list = service.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, false);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassesByTeacherAndStatus_SearchTeacherExistentButStatusInexistent_ReturnListEmpty() {
    	List<com.polifono.domain.Class> list = service.findClassesByTeacherAndStatus(TEACHER_ID_EXISTENT, false);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findClassesByTeacherAndStatus_SearchStatusExistentButTeacherInexistent_ReturnListEmpty() {
    	List<com.polifono.domain.Class> list = service.findClassesByTeacherAndStatus(TEACHER_ID_INEXISTENT, true);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findClassesByTeacherAndStatus - end */
}