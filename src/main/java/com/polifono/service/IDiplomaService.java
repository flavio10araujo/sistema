package com.polifono.service;

import java.util.List;

import com.polifono.domain.Diploma;

public interface IDiplomaService {

	Diploma save(Diploma o);

	Diploma findByCode(String code);

	List<Diploma> findByPlayer(int playerId);
}
