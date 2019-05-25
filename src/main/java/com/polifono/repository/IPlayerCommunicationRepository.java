package com.polifono.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.domain.PlayerCommunication;

public interface IPlayerCommunicationRepository extends JpaRepository<PlayerCommunication, Integer> {

}