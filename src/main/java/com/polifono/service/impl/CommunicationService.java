package com.polifono.service.impl;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Communication;
import com.polifono.repository.ICommunicationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommunicationService {

    private final ICommunicationRepository repository;

    public Communication save(Communication o) {
        return repository.save(o);
    }
}
