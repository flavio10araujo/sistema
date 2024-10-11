package com.polifono.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.polifono.domain.Content;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayervideoService {

    Playervideo save(Playervideo o);

    List<Playervideo> findAll();

    List<Playervideo> findAll(Pageable pageable);

    List<Playervideo> findAllByContent(Content content, Pageable pageable);

    List<Playervideo> findAllByPlayer(Player player, Pageable pageable);

    Playervideo findByPlayerAndPhase(Player player, Phase phase);

    List<Playervideo> findRandomWithRestriction(String restriction);
}
