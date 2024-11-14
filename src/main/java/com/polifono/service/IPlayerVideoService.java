package com.polifono.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.polifono.model.entity.Content;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Playervideo;

public interface IPlayerVideoService {

    Playervideo save(Playervideo o);

    List<Playervideo> findAll();

    List<Playervideo> findAll(Pageable pageable);

    List<Playervideo> findAllByContent(Content content, Pageable pageable);

    List<Playervideo> findAllByPlayer(Player player, Pageable pageable);

    Playervideo findByPlayerAndPhase(Player player, Phase phase);

    List<Playervideo> findRandomWithRestriction(String restriction);
}
