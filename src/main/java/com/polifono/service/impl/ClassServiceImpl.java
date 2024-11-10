package com.polifono.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Class;
import com.polifono.repository.IClassRepository;
import com.polifono.service.IClassService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClassServiceImpl implements IClassService {

    private final IClassRepository repository;

    public Class save(Class clazz) {
        return repository.save(clazz);
    }

    public boolean delete(Integer id) {
        Optional<Class> temp = repository.findById(id);

        if (temp.isPresent()) {
            try {
                repository.save(prepareClassForChangingStatus(temp.get(), false));
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public Optional<Class> findById(int id) {
        return repository.findById(id);
    }

    public List<Class> findAll() {
        return repository.findAll();
    }

    public Class prepareClassForCreation(Class clazz) {
        clazz.setDtInc(new Date());
        clazz.setActive(true);
        return clazz;
    }

    public Class prepareClassForChangingStatus(Class clazz, boolean status) {
        clazz.setActive(status);
        return clazz;
    }

    public List<Class> findByTeacherAndStatus(int playerId, boolean status) {
        return repository.findByTeacherAndStatus(playerId, status);
    }

    public Class clone(Class clazz) {
        Class newItem = new Class();

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
