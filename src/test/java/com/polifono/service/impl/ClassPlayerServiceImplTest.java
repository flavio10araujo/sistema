package com.polifono.service.impl;

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

import com.polifono.model.entity.Class;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;
import com.polifono.repository.IClassPlayerRepository;

/**
 * Unit test methods for the ClassPlayerService.
 */
@ExtendWith(MockitoExtension.class)
public class ClassPlayerServiceImplTest {

    @InjectMocks
    private ClassPlayerService service;

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

    /* save - begin */
    @Test
    public void save_whenSaveClassPlayer_ReturnClassPlayerSaved() {
        Optional<ClassPlayer> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        ClassPlayer entitySaved = service.save(entity.get());

        Assertions.assertNotNull(entitySaved, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entitySaved.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().isActive(), entitySaved.isActive(), "failure - expected active attribute match");
        Assertions.assertEquals(entity.get().getDtExc(), entitySaved.getDtExc(), "failure - expected dt exc attribute match");
        Assertions.assertEquals(entity.get().getStatus(), entitySaved.getStatus(), "failure - expected status attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_whenClassPlayerIsExistent_ReturnTrue() {
        Optional<ClassPlayer> entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);
        when(repository.save(entity.get())).thenReturn(entity.get());

        Assertions.assertTrue(service.delete(CLASSPLAYER_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_EXISTENT);
        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_whenClassPlayerIsInexistent_ReturnFalse() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(CLASSPLAYER_ID_INEXISTENT), "failure - expected return false");
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_whenClassPlayerIsExistent_ReturnClassPlayer() {
        Optional<ClassPlayer> entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);

        Optional<ClassPlayer> entityReturned = service.findById(CLASSPLAYER_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(CLASSPLAYER_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_whenClassPlayerIsInexistent_ReturnNull() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Optional<ClassPlayer> entityReturned = service.findById(CLASSPLAYER_ID_INEXISTENT);
        Assertions.assertFalse(entityReturned.isPresent(), "failure - expected null");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_whenListAllClassPlayers_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* prepareClassPlayerToCreation - begin */
    /*@Test
    public void prepareClassPlayerToCreation_whenEverythingIsOk_ReturnClassPlayerWithDefaultValuesForCreation() {
    	// Default values for creation are:
    	// dtInc = new Date();
		// active = true;
		// status = 1;
    	ClassPlayer entity = getEntityStubData();
    	ClassPlayer entityReturned = service.create(entity);

        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", entity.getId(), entityReturned.getId());
        Assert.assertEquals("failure - expected class attribute match", entity.getClazz().getId(), entityReturned.getClazz().getId());
        Assert.assertEquals("failure - expected player attribute match", entity.getPlayer().getId(), entityReturned.getPlayer().getId());

        Assert.assertEquals("failure - expected dtInc attribute match", 0, entityReturned.getDtInc().compareTo(new Date()));
        Assert.assertEquals("failure - expected active attribute match", true, entityReturned.isActive());
        Assert.assertNull("failure - expected null", entityReturned.getDtExc());
        Assert.assertEquals("failure - expected status attribute match", 1, entityReturned.getStatus());
    }*/
    /* prepareClassPlayerToCreation - end */

    /* changeStatus - begin */
    @Test
    public void changeStatus_whenClassPlayerIsExistent_ReturnTrue() {
        Optional<ClassPlayer> entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(entity);
        when(repository.save(entity.get())).thenReturn(entity.get());

        Assertions.assertTrue(service.changeStatus(CLASSPLAYER_ID_EXISTENT.intValue(), STATUS_CONFIRMED), "failure - expected true");

        verify(repository, times(1)).save(entity.get());
        //verifyNoMoreInteractions(repository);
    }

    @Test
    public void changeStatus_whenClassPlayerIsInexistent_ReturnFalse() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.changeStatus(CLASSPLAYER_ID_INEXISTENT.intValue(), STATUS_CONFIRMED), "failure - expected false");
    }
    /* changeStatus - end */

    /* prepareClassPlayerToChangeStatus - begin
    @Test
    public void prepareClassPlayerToChangeStatus_whenEverythingIsOK_ReturnEntityWithStatusChanged() {
    	ClassPlayer entity = getEntityStubData();

    	ClassPlayer entityChanged = service.prepareClassPlayerToChangeStatus(entity, STATUS_CONFIRMED);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_CONFIRMED.intValue(), entityChanged.getStatus());

    	entityChanged = service.prepareClassPlayerToChangeStatus(entity, STATUS_DISABLED);
    	Assert.assertEquals("failure - expected attribute status match", STATUS_DISABLED.intValue(), entityChanged.getStatus());
    }
    prepareClassPlayerToChangeStatus - end */

    /* findClassPlayersByTeacher - begin */
    @Test
    public void findClassPlayersByTeacher_whenSearchByTeacherExistent_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByTeacherId(TEACHER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByTeacherId(TEACHER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByTeacherId(TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacher_whenSearchByTeacherInexistent_ReturnEmptyList() {
        when(repository.findAllByTeacherId(TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherId(TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherId(TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacher - end */

    /* findClassPlayersByTeacherAndClass - begin */
    @Test
    public void findClassPlayersByTeacherAndClass_whenSearchByTeacherAndClassExistents_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndClass_whenSearchByTeacherAndClassInexistents_ReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndClass_whenSearchByTeacherExistentButClassInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndClass_whenSearchByClassExistentButTeacherInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacherAndClass - end */

    /* findClassPlayersByClassAndStatus - begin */
    @Test
    public void findClassPlayersByClassAndStatus_whenSearchByClassAndStatusExistents_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndStatus_whenSearchByClassAndStatusInexistents_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndStatus_whenSearchByClassExistentButStatusInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndStatus_whenSearchByStatusExistentButClassInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<>());

        List<ClassPlayer> list = service.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByClassAndStatus - end */

    /* findClassPlayersByClassAndPlayer - begin */
    @Test
    public void findClassPlayersByClassAndPlayer_whenSearchByClassAndPlayerExistents_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndPlayer_whenSearchByClassAndPlayerInexistents_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndPlayer_whenSearchByClassExistentButPlayerInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByClassAndPlayer_whenSearchByPlayerExistentButClassInexistent_ReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByClassAndPlayer - end */

    /* findClassPlayersByPlayerAndStatus - begin */
    @Test
    public void findClassPlayersByPlayerAndStatus_whenSearchByPlayerAndStatusExistents_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByPlayerAndStatus_whenSearchByPlayerAndStatusInexistents_ReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByPlayerAndStatus_whenSearchByPlayerExistentButStatusInexistent_ReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByPlayerAndStatus_whenSearchByStatusExistentButPlayerInexistent_ReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByPlayerAndStatus - end */

    /* findClassPlayersByTeacherAndStudent - begin */
    @Test
    public void findClassPlayersByTeacherAndStudent_whenSearchByTeacherAndStudentExistents_ReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndStudent_whenSearchByTeacherAndStudentInexistents_ReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndStudent_whenSearchByTeacherExistentButStudentInexistent_ReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findClassPlayersByTeacherAndStudent_whenSearchByStudentExistentButTeacherInexistent_ReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findClassPlayersByTeacherAndStudent - end */

    /* isMyStudent - begin */
    @Test
    public void isMyStudent_whenItIsHisStudent_ReturnTrue() {
        Player teacher = new Player();
        teacher.setId(TEACHER_ID_EXISTENT);

        Player student = new Player();
        student.setId(PLAYER_ID_EXISTENT);

        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, PLAYER_ID_EXISTENT)).thenReturn(list);

        Assertions.assertTrue(service.isMyStudent(teacher, student));

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void isMyStudent_whenItIsNotHisStudent_ReturnFalse() {
        Player teacher = new Player();
        teacher.setId(TEACHER_ID_EXISTENT);

        Player student = new Player();
        student.setId(PLAYER_ID_INEXISTENT);

        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        Assertions.assertFalse(service.isMyStudent(teacher, student));

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* isMyStudent - end */

    /* stubs - begin */
    private Optional<ClassPlayer> getEntityStubData() {
        Class clazz = new Class();
        clazz.setId(CLASS_ID_EXISTENT);

        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);

        ClassPlayer entity = new ClassPlayer();
        entity.setId(CLASSPLAYER_ID_EXISTENT);
        entity.setClazz(clazz);
        entity.setPlayer(player);

        return Optional.of(entity);
    }

    private List<ClassPlayer> getEntityListStubData() {
        List<ClassPlayer> list = new ArrayList<>();

        ClassPlayer entity1 = getEntityStubData().get();
        ClassPlayer entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
