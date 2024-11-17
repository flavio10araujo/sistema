package com.polifono.service;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.PlayerCommunication;
import com.polifono.repository.IPlayerCommunicationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerCommunicationService {

    private final IPlayerCommunicationRepository repository;

    public PlayerCommunication save(PlayerCommunication o) {
        return repository.save(o);
    }
}
