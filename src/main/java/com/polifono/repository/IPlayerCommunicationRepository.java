package com.polifono.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.model.entity.PlayerCommunication;

public interface IPlayerCommunicationRepository extends JpaRepository<PlayerCommunication, Integer> {

}
