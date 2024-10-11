package com.polifono.service.impl;

import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerCommunication;
import com.polifono.repository.IPlayerCommunicationRepository;
import com.polifono.service.IPlayerCommunicationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerCommunicationServiceImpl implements IPlayerCommunicationService {

    private final IPlayerCommunicationRepository repository;

    @Override
    public PlayerCommunication save(PlayerCommunication o) {
        return repository.save(o);
    }
}
