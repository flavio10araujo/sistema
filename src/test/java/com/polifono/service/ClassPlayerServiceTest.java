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

import com.polifono.model.entity.Class;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;
import com.polifono.repository.IClassPlayerRepository;

/**
 * Unit test methods for the ClassPlayerService.
 */
@ExtendWith(MockitoExtension.class)
public class ClassPlayerServiceTest {

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
    public void whenSave_thenReturnClassPlayerSaved() {
        ClassPlayer entity = getEntityStubData();

        when(repository.save(entity)).thenReturn(entity);

        ClassPlayer entitySaved = service.save(entity);

        Assertions.assertNotNull(entitySaved, "failure - expected not null");
        Assertions.assertEquals(entity.getId(), entitySaved.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.isActive(), entitySaved.isActive(), "failure - expected active attribute match");
        Assertions.assertEquals(entity.getDtExc(), entitySaved.getDtExc(), "failure - expected dt exc attribute match");
        Assertions.assertEquals(entity.getStatus(), entitySaved.getStatus(), "failure - expected status attribute match");

        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void givenClassPlayerExists_whenDelete_thenReturnTrue() {
        ClassPlayer entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        Assertions.assertTrue(service.delete(CLASSPLAYER_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_EXISTENT);
        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassPlayerDoesNotExist_whenDelete_thenReturnFalse() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(CLASSPLAYER_ID_INEXISTENT), "failure - expected return false");
    }
    /* delete - end */

    /* findById - begin */
    @Test
    public void givenClassPlayerExist_whenFindOne_thenReturnClassPlayer() {
        ClassPlayer entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Optional<ClassPlayer> entityReturned = service.findById(CLASSPLAYER_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(CLASSPLAYER_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassPlayerDoesNotExist_whenFindOne_thenReturnNull() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Optional<ClassPlayer> entityReturned = service.findById(CLASSPLAYER_ID_INEXISTENT);
        Assertions.assertFalse(entityReturned.isPresent(), "failure - expected null");

        verify(repository, times(1)).findById(CLASSPLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findById - end */

    /* findAll - begin */
    @Test
    public void whenFindAll_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findAllByClassIdAndStatus - begin */
    @Test
    public void givenClassAndStatusExist_whenFindAllByClassIdAndStatus_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassAndStatusDoNotExist_whenFindAllByClassIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassExistsButStatusDoesNotExist_whenFindAllByClassIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenStatusExistsButClassDoesNotExist_whenFindAllByClassIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<>());

        List<ClassPlayer> list = service.findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStatus(CLASS_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByClassIdAndStatus - end */

    /* findAllByClassIdAndTeacherId - begin */
    @Test
    public void givenTeacherAndClassDoNotExist_whenFindAllByClassIdAndTeacherId_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenTeacherAndClassExist_whenFindAllByClassIdAndTeacherId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenTeacherExistsButClassDoesNotExist_whenFindAllByClassIdAndTeacherId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_INEXISTENT, TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassExistsButTeacherDoesNotExist_whenFindAllByClassIdAndTeacherId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndTeacherId(CLASS_ID_EXISTENT, TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByClassIdAndTeacherId - end */

    /* findAllByClassIdAndStudentId - begin */
    @Test
    public void givenClassAndPlayerExist_whenFindAllByClassIdAndStudentId_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassAndPlayerDoNotExist_whenFindAllByClassIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenClassExistsButPlayerDoesNotExist_whenFindAllByClassIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_EXISTENT, PLAYER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenPlayerExistsButClassDoesNotExist_whenFindAllByClassIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByClassIdAndStudentId(CLASS_ID_INEXISTENT, PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByClassIdAndStudentId - end */

    /* findAllByTeacherId - begin */
    @Test
    public void givenTeacherExists_whenFindAllByTeacherId_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByTeacherId(TEACHER_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByTeacherId(TEACHER_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByTeacherId(TEACHER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenTeacherDoesNotExist_whenFindAllByTeacherId_thenReturnEmptyList() {
        when(repository.findAllByTeacherId(TEACHER_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherId(TEACHER_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherId(TEACHER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByTeacherId - end */

    /* findAllByTeacherIdAndStudentId - begin */
    @Test
    public void givenTeacherAndStudentExist_whenFindAllByTeacherIdAndStudentId_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenTeacherAndStudentDoNotExist_whenFindAllByTeacherIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenTeacherExistsButStudentDoesNotExist_whenFindAllByTeacherIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_EXISTENT, STUDENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenStudentExistsButTeacherDoesNotExist_whenFindAllByTeacherIdAndStudentId_thenReturnEmptyList() {
        when(repository.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByTeacherIdAndStudentId(TEACHER_ID_INEXISTENT, STUDENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByTeacherIdAndStudentId - end */

    /* findAllByStudentIdAndStatus - begin */
    @Test
    public void givenPlayerAndStatusExist_whenFindAllByStudentIdAndStatus_thenReturnList() {
        List<ClassPlayer> list = getEntityListStubData();

        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED)).thenReturn(list);

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenPlayerAndStatusDoNotExist_whenFindAllByStudentIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenPlayerExistsButStatusDoesNotExist_whenFindAllByStudentIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_EXISTENT, 0);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenStatusExistsButPlayerDoesNotExist_whenFindAllByStudentIdAndStatus_thenReturnEmptyList() {
        when(repository.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED)).thenReturn(new ArrayList<>());

        List<ClassPlayer> listReturned = service.findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByStudentIdAndStatus(PLAYER_ID_INEXISTENT, STATUS_CONFIRMED);
        verifyNoMoreInteractions(repository);
    }
    /* findAllByStudentIdAndStatus - end */

    /* isMyStudent - begin */
    @Test
    public void givenItIsHisStudent_whenIsMyStudent_thenReturnTrue() {
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
    public void givenItIsNotHisStudent_whenIsMyStudent_thenReturnFalse() {
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

    /* changeStatus - begin */
    @Test
    public void givenClassPlayerExists_whenChangeStatus_thenReturnTrue() {
        ClassPlayer entity = getEntityStubData();

        when(repository.findById(CLASSPLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        Assertions.assertTrue(service.changeStatus(CLASSPLAYER_ID_EXISTENT, STATUS_CONFIRMED), "failure - expected true");

        verify(repository, times(1)).save(entity);
    }

    @Test
    public void givenClassPlayerDoesNotExist_whenChangeStatus_thenReturnFalse() {
        when(repository.findById(CLASSPLAYER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.changeStatus(CLASSPLAYER_ID_INEXISTENT, STATUS_CONFIRMED), "failure - expected false");
    }
    /* changeStatus - end */

    /* stubs - begin */
    private ClassPlayer getEntityStubData() {
        Class clazz = new Class();
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
        List<ClassPlayer> list = new ArrayList<>();

        ClassPlayer entity1 = getEntityStubData();
        ClassPlayer entity2 = getEntityStubData();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
