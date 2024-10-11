package com.polifono.service;

import java.util.List;
import java.util.Optional;

public interface IClassService {

    com.polifono.domain.Class save(com.polifono.domain.Class o);

    boolean delete(Integer id);

    Optional<com.polifono.domain.Class> findById(int id);

    List<com.polifono.domain.Class> findAll();

    List<com.polifono.domain.Class> findByTeacherAndStatus(int playerId, boolean status);

    com.polifono.domain.Class clone(com.polifono.domain.Class clazz);

    com.polifono.domain.Class prepareClassForCreation(com.polifono.domain.Class clazz);

    com.polifono.domain.Class prepareClassForChangingStatus(com.polifono.domain.Class clazz, boolean status);
}
