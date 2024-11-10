package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.model.entity.Class;

public interface IClassService {

    Class save(Class o);

    boolean delete(Integer id);

    Optional<Class> findById(int id);

    List<Class> findAll();

    List<Class> findByTeacherAndStatus(int playerId, boolean status);

    Class clone(Class clazz);

    Class prepareClassForCreation(Class clazz);

    Class prepareClassForChangingStatus(Class clazz, boolean status);
}
