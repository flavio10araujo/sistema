package com.polifono.service;

import java.util.List;

import com.polifono.domain.Diploma;

public interface IDiplomaService {

	public List<Diploma> findByPlayer(int playerId);
}