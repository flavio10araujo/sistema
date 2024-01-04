package com.polifono.service;

import java.util.List;
import java.util.Optional;

public interface IClassService {

    public com.polifono.domain.Class save(com.polifono.domain.Class o);

    public Boolean delete(Integer id);

    public Optional<com.polifono.domain.Class> findById(int id);

    public List<com.polifono.domain.Class> findAll();

    public List<com.polifono.domain.Class> findByTeacherAndStatus(int playerId, boolean status);

    public com.polifono.domain.Class clone(com.polifono.domain.Class clazz);

    public com.polifono.domain.Class prepareClassForCreation(com.polifono.domain.Class clazz);

    public com.polifono.domain.Class prepareClassForChangingStatus(com.polifono.domain.Class clazz, boolean status);

}
