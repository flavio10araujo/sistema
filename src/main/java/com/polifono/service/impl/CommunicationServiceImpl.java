package com.polifono.service.impl;

import org.springframework.stereotype.Service;

import com.polifono.domain.Communication;
import com.polifono.repository.ICommunicationRepository;
import com.polifono.service.ICommunicationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommunicationServiceImpl implements ICommunicationService {

	private final ICommunicationRepository repository;

	@Override
	public Communication save(Communication o) {
		return repository.save(o);
	}
}
