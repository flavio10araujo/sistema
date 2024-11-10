package com.polifono.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.model.entity.Communication;

public interface ICommunicationRepository extends JpaRepository<Communication, Integer> {

}
