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

import com.polifono.domain.Player;
import com.polifono.repository.IClassRepository;
import com.polifono.service.impl.ClassServiceImpl;

/**
 * Unit test methods for the ClassService.
 */
@ExtendWith(MockitoExtension.class)
public class ClassServiceTest {

    @InjectMocks
    private ClassServiceImpl service;

    @Mock
    private IClassRepository repository;

    private final Integer CLASS_ID_EXISTENT = 1;
    private final Integer CLASS_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer TEACHER_ID_EXISTENT = 2;
    private final Integer TEACHER_ID_INEXISTENT = Integer.MAX_VALUE;

    /* stubs - begin */
    private Optional<com.polifono.domain.Class> getEntityStubData() {
        Player player = new Player();
        player.setId(123);

        com.polifono.domain.Class clazz = new com.polifono.domain.Class();
        clazz.setId(CLASS_ID_EXISTENT);
        clazz.setName("Class Name");
        clazz.setDescription("Class Description");
        clazz.setPlayer(player);

        return Optional.of(clazz);
    }

    private List<com.polifono.domain.Class> getEntityListStubData() {
        List<com.polifono.domain.Class> list = new ArrayList<com.polifono.domain.Class>();

        com.polifono.domain.Class entity1 = getEntityStubData().get();
        com.polifono.domain.Class entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

    /* save - begin */
    @Test
    public void save_WhenSaveClass_ReturnClassSaved() {
        Optional<com.polifono.domain.Class> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        com.polifono.domain.Class entityReturned = service.save(entity.get());

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entityReturned.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().getPlayer().getId(), entityReturned.getPlayer().getId(), "failure - expected player attribute match");
        Assertions.assertEquals(entity.get().isActive(), entityReturned.isActive(), "failure - expected active attribute match");
        Assertions.assertEquals(entity.get().getName(), entityReturned.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.get().getDescription(), entityReturned.getDescription(), "failure - expected description attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenClassIsExistent_ReturnTrue() {
        Optional<com.polifono.domain.Class> entity = getEntityStubData();

        when(repository.findById(CLASS_ID_EXISTENT)).thenReturn(entity);

        Assertions.assertTrue(service.delete(CLASS_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(CLASS_ID_EXISTENT);
        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenClassIsInexistent_ReturnFalse() {
        when(repository.findById(CLASS_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(CLASS_ID_INEXISTENT), "failure - expected return false");

        verify(repository, times(1)).findById(CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenClassIsExistent_ReturnClass() {
        Optional<com.polifono.domain.Class> entity = getEntityStubData();

        when(repository.findById(CLASS_ID_EXISTENT)).thenReturn(entity);

        Optional<com.polifono.domain.Class> entityReturned = service.findById(CLASS_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(CLASS_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(CLASS_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenClassIsInexistent_ReturnNull() {
        when(repository.findById(CLASS_ID_INEXISTENT)).thenReturn(null);

        Optional<com.polifono.domain.Class> entity = service.findById(CLASS_ID_INEXISTENT);
        Assertions.assertNull(entity, "failure - expected null");

        verify(repository, times(1)).findById(CLASS_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllClasses_ReturnList() {
        List<com.polifono.domain.Class> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<com.polifono.domain.Class> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

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
        Optional<com.polifono.domain.Class> entity = getEntityStubData();
        com.polifono.domain.Class entityReturned = service.prepareClassForCreation(entity.get());

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entityReturned.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().getPlayer().getId(), entityReturned.getPlayer().getId(), "failure - expected player attribute match");

        Assertions.assertTrue(entityReturned.isActive(), "failure - expected active attribute match");
        Assertions.assertEquals(entity.get().getName(), entityReturned.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.get().getDescription(), entityReturned.getDescription(), "failure - expected description attribute match");
    }
    /* prepareClassForCreation - end */

    /* prepareClassForChangingStatus - begin */
    @Test
    public void prepareClassForChangingStatus_WhenEverythingIsOK_ReturnEntityWithStatusChanged() {
        Optional<com.polifono.domain.Class> entity = getEntityStubData();

        com.polifono.domain.Class entityChanged = service.prepareClassForChangingStatus(entity.get(), true);
        Assertions.assertEquals(true, entityChanged.isActive(), "failure - expected attribute status match");

        entityChanged = service.prepareClassForChangingStatus(entity.get(), false);
        Assertions.assertEquals(false, entityChanged.isActive(), "failure - expected attribute status match");
    }
    /* prepareClassForChangingStatus - end */

    /* findByTeacherAndStatus - begin */
    @Test
    public void findByTeacherAndStatus_WhenSearchByTeacherAndStatusExistents_ReturnList() {
        List<com.polifono.domain.Class> list = getEntityListStubData();

        when(repository.findByTeacherAndStatus(TEACHER_ID_EXISTENT, true)).thenReturn(list);

        List<com.polifono.domain.Class> listReturned = service.findByTeacherAndStatus(TEACHER_ID_EXISTENT, true);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByTeacherAndStatus(TEACHER_ID_EXISTENT, true);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByTeacherAndStatus_WhenSearchByTeacherAndStatusInexistents_ReturnEmptyList() {
        when(repository.findByTeacherAndStatus(TEACHER_ID_INEXISTENT, false)).thenReturn(new ArrayList<com.polifono.domain.Class>());

        List<com.polifono.domain.Class> listReturned = service.findByTeacherAndStatus(TEACHER_ID_INEXISTENT, false);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByTeacherAndStatus(TEACHER_ID_INEXISTENT, false);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByTeacherAndStatus_WhenSearchByTeacherExistentButStatusInexistent_ReturnEmptyList() {
        when(repository.findByTeacherAndStatus(TEACHER_ID_EXISTENT, false)).thenReturn(new ArrayList<com.polifono.domain.Class>());

        List<com.polifono.domain.Class> listReturned = service.findByTeacherAndStatus(TEACHER_ID_EXISTENT, false);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByTeacherAndStatus(TEACHER_ID_EXISTENT, false);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByTeacherAndStatus_WhenSearchStatusExistentButTeacherInexistent_ReturnEmptyList() {
        when(repository.findByTeacherAndStatus(TEACHER_ID_INEXISTENT, true)).thenReturn(new ArrayList<com.polifono.domain.Class>());

        List<com.polifono.domain.Class> listReturned = service.findByTeacherAndStatus(TEACHER_ID_INEXISTENT, true);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByTeacherAndStatus(TEACHER_ID_INEXISTENT, true);
        verifyNoMoreInteractions(repository);
    }
    /* findByTeacherAndStatus - end */
}
