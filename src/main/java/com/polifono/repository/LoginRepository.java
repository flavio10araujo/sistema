package com.polifono.repository;

import org.springframework.data.repository.CrudRepository;

import com.polifono.domain.Login;

public interface LoginRepository extends CrudRepository<Login, Integer> {

}