package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerCommunication;
import com.polifono.repository.IPlayerCommunicationRepository;
import com.polifono.service.IPlayerCommunicationService;

@Service
public class PlayerCommunicationServiceImpl implements IPlayerCommunicationService {

	private IPlayerCommunicationRepository repository;
	
	@Autowired
	public PlayerCommunicationServiceImpl(IPlayerCommunicationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public PlayerCommunication save(PlayerCommunication o) {
		return repository.save(o);
	}

}