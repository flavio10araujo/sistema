package com.polifono.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polifono.domain.Diploma;
import com.polifono.repository.IDiplomaRepository;
import com.polifono.service.IDiplomaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DiplomaServiceImpl implements IDiplomaService {

    private final IDiplomaRepository repository;

    public final Diploma save(Diploma diploma) {
        return repository.save(diploma);
    }

    @Override
    public List<Diploma> findByPlayer(int playerId) {
        List<Diploma> list = repository.findByPlayer(playerId);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;
    }

    @Override
    public Diploma findByCode(String code) {
        return repository.findByCode(code);
    }
}
