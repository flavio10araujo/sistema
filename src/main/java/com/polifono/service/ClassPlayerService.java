package com.polifono.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;
import com.polifono.repository.IClassPlayerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClassPlayerService {

    private final IClassPlayerRepository repository;

    public ClassPlayer save(ClassPlayer classPlayer) {
        return repository.save(classPlayer);
    }

    public boolean delete(Integer id) {
        Optional<ClassPlayer> temp = repository.findById(id);

        if (temp.isPresent()) {
            try {
                repository.save(prepareClassPlayerToChangeStatus(temp.get(), 3));
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public Optional<ClassPlayer> findById(int id) {
        return repository.findById(id);
    }

    public List<ClassPlayer> findAll() {
        return repository.findAll();
    }

    public List<ClassPlayer> findAllByClassIdAndStatus(int clazzId, int status) {
        return repository.findAllByClassIdAndStatus(clazzId, status);
    }

    /**
     * Get all the students of a specific class (clazzId) of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    public List<ClassPlayer> findAllByClassIdAndTeacherId(int clazzId, int teacherId) {
        return repository.findAllByClassIdAndTeacherId(clazzId, teacherId);
    }

    public List<ClassPlayer> findAllByClassIdAndStudentId(int clazzId, int studentId) {
        return repository.findAllByClassIdAndStudentId(clazzId, studentId);
    }

    /**
     * Get all the students of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    public List<ClassPlayer> findAllByTeacherId(int teacherId) {
        return repository.findAllByTeacherId(teacherId);
    }

    /**
     * Method used to see if studentId is student of teacherId in any class.
     * The studentId must be in status 2 (confirmed).
     */
    public List<ClassPlayer> findAllByTeacherIdAndStudentId(int teacherId, int studentId) {
        return repository.findAllByTeacherIdAndStudentId(teacherId, studentId);
    }

    public List<ClassPlayer> findAllByStudentIdAndStatus(int studentId, int status) {
        return repository.findAllByStudentIdAndStatus(studentId, status);
    }

    public ClassPlayer create(ClassPlayer classPlayer) {
        return repository.save(prepareClassPlayerForCreation(classPlayer));
    }

    /**
     * Method used to see if student is student of teacher in any class.
     * Return true if student is student of teacher.
     */
    public boolean isMyStudent(Player teacher, Player student) {
        List<ClassPlayer> classPlayers = findAllByTeacherIdAndStudentId(teacher.getId(), student.getId());
        return classPlayers != null && !classPlayers.isEmpty();
    }

    public boolean changeStatus(int id, int status) {
        Optional<ClassPlayer> temp = repository.findById(id);

        if (temp.isPresent()) {
            try {
                repository.save(this.prepareClassPlayerToChangeStatus(temp.get(), status));
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    private ClassPlayer prepareClassPlayerForCreation(ClassPlayer classPlayer) {
        classPlayer.setDtInc(new Date());
        classPlayer.setActive(true);
        classPlayer.setStatus(2);
        return classPlayer;
    }

    private ClassPlayer prepareClassPlayerToChangeStatus(ClassPlayer classPlayer, int status) {
        classPlayer.setStatus(status);
        return classPlayer;
    }
}
