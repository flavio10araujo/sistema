package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.polifono.domain.Content;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;
import com.polifono.repository.IPlayervideoRepository;
import com.polifono.service.IPlayervideoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayervideoServiceImpl implements IPlayervideoService {

    private final IPlayervideoRepository repository;

    @Override
    public Playervideo save(Playervideo playervideo) {
        playervideo.setDtInc(new Date());
        playervideo.setActive(true);
        return repository.save(playervideo);
    }

    @Override
    public List<Playervideo> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Playervideo> findAll(Pageable pageable) {
        return repository.findGeneral(pageable);
    }

    @Override
    public List<Playervideo> findAllByContent(Content content, Pageable pageable) {
        return repository.findAllByContent(content, pageable);
    }

    @Override
    public List<Playervideo> findAllByPlayer(Player player, Pageable pageable) {
        return repository.findAllByPlayer(player, pageable);
    }

    @Override
    public Playervideo findByPlayerAndPhase(Player player, Phase phase) {
        return repository.findByPlayerAndPhase(player.getId(), phase.getId());
    }

    @Override
    public List<Playervideo> findRandomWithRestriction(String restriction) {
        return repository.findRandomWithRestriction();
    }
}
