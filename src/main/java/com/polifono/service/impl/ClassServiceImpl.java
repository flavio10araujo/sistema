package com.polifono.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.repository.IClassRepository;
import com.polifono.service.IClassService;

@Service
public class ClassServiceImpl implements IClassService {

    private IClassRepository repository;

    @Autowired
    public ClassServiceImpl(IClassRepository repository) {
        this.repository = repository;
    }

    public final com.polifono.domain.Class save(com.polifono.domain.Class clazz) {
        return repository.save(clazz);
    }

    public Boolean delete(Integer id) {
        Optional<com.polifono.domain.Class> temp = repository.findById(id);

        if (!temp.isPresent()) {
            try {
                repository.save(prepareClassForChangingStatus(temp.get(), false));
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public Optional<com.polifono.domain.Class> findById(int id) {
        return repository.findById(id);
    }

    public final List<com.polifono.domain.Class> findAll() {
        return (List<com.polifono.domain.Class>) repository.findAll();
    }

    public final com.polifono.domain.Class prepareClassForCreation(com.polifono.domain.Class clazz) {
        clazz.setDtInc(new Date());
        clazz.setActive(true);
        return clazz;
    }

    public final com.polifono.domain.Class prepareClassForChangingStatus(com.polifono.domain.Class clazz, boolean status) {
        clazz.setActive(status);
        return clazz;
    }

    public final List<com.polifono.domain.Class> findByTeacherAndStatus(int playerId, boolean status) {
        return repository.findByTeacherAndStatus(playerId, status);
    }

    public final com.polifono.domain.Class clone(com.polifono.domain.Class clazz) {
        com.polifono.domain.Class newItem = new com.polifono.domain.Class();

        newItem.setActive(clazz.isActive());
        newItem.setDescription(clazz.getDescription());
        newItem.setGrade(clazz.getGrade());
        newItem.setName(clazz.getName());
        newItem.setPlayer(clazz.getPlayer());
        newItem.setSchool(clazz.getSchool());
        newItem.setSemester(clazz.getSemester());
        newItem.setYear(clazz.getYear());

        return newItem;
    }
}
