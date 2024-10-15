package com.polifono.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.repository.IClassPlayerRepository;
import com.polifono.service.IClassPlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClassPlayerServiceImpl implements IClassPlayerService {

    private final IClassPlayerRepository repository;

    @Override
    public ClassPlayer save(ClassPlayer classPlayer) {
        return repository.save(classPlayer);
    }

    @Override
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

    @Override
    public Optional<ClassPlayer> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<ClassPlayer> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ClassPlayer> findAllByClassIdAndStatus(int clazzId, int status) {
        return repository.findAllByClassIdAndStatus(clazzId, status);
    }

    /**
     * Get all the students of a specific class (clazzId) of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    @Override
    public List<ClassPlayer> findAllByClassIdAndTeacherId(int clazzId, int teacherId) {
        return repository.findAllByClassIdAndTeacherId(clazzId, teacherId);
    }

    @Override
    public List<ClassPlayer> findAllByClassIdAndStudentId(int clazzId, int studentId) {
        return repository.findAllByClassIdAndStudentId(clazzId, studentId);
    }

    /**
     * Get all the students of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    @Override
    public List<ClassPlayer> findAllByTeacherId(int teacherId) {
        return repository.findAllByTeacherId(teacherId);
    }

    /**
     * Method used to see if studentId is student of teacherId in any class.
     * The studentId must be in status 2 (confirmed).
     */
    @Override
    public List<ClassPlayer> findAllByTeacherIdAndStudentId(int teacherId, int studentId) {
        return repository.findAllByTeacherIdAndStudentId(teacherId, studentId);
    }

    @Override
    public List<ClassPlayer> findAllByStudentIdAndStatus(int studentId, int status) {
        return repository.findAllByStudentIdAndStatus(studentId, status);
    }

    @Override
    public ClassPlayer create(ClassPlayer classPlayer) {
        return repository.save(prepareClassPlayerForCreation(classPlayer));
    }

    /**
     * Method used to see if student is student of teacher in any class.
     * Return true if student is student of teacher.
     */
    @Override
    public boolean isMyStudent(Player teacher, Player student) {
        List<ClassPlayer> classPlayers = findAllByTeacherIdAndStudentId(teacher.getId(), student.getId());
        return classPlayers != null && !classPlayers.isEmpty();
    }

    @Override
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
