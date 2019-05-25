package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Communication;
import com.polifono.repository.ICommunicationRepository;
import com.polifono.service.ICommunicationService;

@Service
public class CommunicationServiceImpl implements ICommunicationService {

	private ICommunicationRepository repository;
	
	@Autowired
	public CommunicationServiceImpl(ICommunicationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Communication save(Communication o) {
		return repository.save(o);
	}

}