package com.polifono.service.impl.playervideo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.polifono.model.entity.Content;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Playervideo;
import com.polifono.repository.IPlayerVideoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerVideoService {

    private final IPlayerVideoRepository repository;

    public Playervideo save(Playervideo playervideo) {
        playervideo.setDtInc(new Date());
        playervideo.setActive(true);
        return repository.save(playervideo);
    }

    public List<Playervideo> findAll() {
        return repository.findAll();
    }

    public List<Playervideo> findAll(Pageable pageable) {
        return repository.findGeneral(pageable);
    }

    public List<Playervideo> findAllByContent(Content content, Pageable pageable) {
        return repository.findAllByContent(content, pageable);
    }

    public List<Playervideo> findAllByPlayer(Player player, Pageable pageable) {
        return repository.findAllByPlayer(player, pageable);
    }

    public Optional<Playervideo> findByPlayerAndPhase(Player player, Phase phase) {
        return repository.findByPlayerAndPhase(player.getId(), phase.getId());
    }

    public List<Playervideo> findRandomWithRestriction(String restriction) {
        return repository.findRandomWithRestriction();
    }
}
