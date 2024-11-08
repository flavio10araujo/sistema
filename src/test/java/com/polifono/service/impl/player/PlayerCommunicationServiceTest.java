package com.polifono.service.impl.player;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.repository.IPlayerRepository;

@ExtendWith(MockitoExtension.class)
public class PlayerCommunicationServiceTest {

    @InjectMocks
    private PlayerCommunicationService service;

    @Mock
    private IPlayerRepository repository;
}
