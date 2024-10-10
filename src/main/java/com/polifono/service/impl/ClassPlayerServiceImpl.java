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
    public final ClassPlayer save(ClassPlayer classPlayer) {
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
    public final List<ClassPlayer> findAll() {
        return (List<ClassPlayer>) repository.findAll();
    }

    /**
     * Get all the students of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    @Override
    public final List<ClassPlayer> findByTeacher(int playerId) {
        return repository.findByTeacher(playerId);
    }

    /**
     * Get all the students of a specific class (clazzId) of this teacher (playerId).
     * Get only student in the status 1 (pending) and 2 (confirmed).
     */
    @Override
    public final List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId) {
        return repository.findByTeacherAndClass(playerId, clazzId);
    }

    @Override
    public final List<ClassPlayer> findByClassAndStatus(int clazzId, int status) {
        return repository.findByClassAndStatus(clazzId, status);
    }

    @Override
    public final List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId) {
        return repository.findByClassAndPlayer(clazzId, playerId);
    }

    @Override
    public final List<ClassPlayer> findByPlayerAndStatus(int playerId, int status) {
        return repository.findByPlayerAndStatus(playerId, status);
    }

    /**
     * Method used to see if studentId is student of teacherId in any class.
     * The studentId must be in status 2 (confirmed).
     */
    @Override
    public final List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId) {
        return repository.findByTeacherAndStudent(teacherId, studentId);
    }

    @Override
    public final ClassPlayer create(ClassPlayer classPlayer) {
        return repository.save(this.prepareClassPlayerForCreation(classPlayer));
    }

    /**
     * Method used to see if student is student of teacher in any class.
     * Return true if student is student of teacher.
     */
    @Override
    public boolean isMyStudent(Player teacher, Player student) {
        List<ClassPlayer> classPlayers = this.findByTeacherAndStudent(teacher.getId(), student.getId());
        return classPlayers != null && !classPlayers.isEmpty();
    }

    public final ClassPlayer prepareClassPlayerForCreation(ClassPlayer classPlayer) {
        classPlayer.setDtInc(new Date());
        classPlayer.setActive(true);
        classPlayer.setStatus(2);
        return classPlayer;
    }

    @Override
    public final boolean changeStatus(int id, int status) {
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

    public final ClassPlayer prepareClassPlayerToChangeStatus(ClassPlayer classPlayer, int status) {
        classPlayer.setStatus(status);
        return classPlayer;
    }
}
