package com.polifono.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.model.entity.Content;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Playervideo;

public interface IPlayerVideoRepository extends JpaRepository<Playervideo, Integer> {

    @Query("SELECT playervideo FROM Playervideo playervideo WHERE playervideo.active = true ORDER BY playervideo.dtInc DESC")
    List<Playervideo> findGeneral(Pageable pageable);

    List<Playervideo> findAllByPlayer(Player player, Pageable pageable);

    List<Playervideo> findAllByContent(Content content, Pageable pageable);

    @Query("SELECT playervideo FROM Playervideo playervideo, Content content, Phase phase WHERE playervideo.content.id = content.id AND content.phase.id = phase.id AND content.order = 1 AND playervideo.player.id = :playerId AND phase.id = :phaseId")
    Optional<Playervideo> findByPlayerAndPhase(@Param("playerId") int playerId, @Param("phaseId") int phaseId);

    @Query("SELECT playervideo FROM Playervideo playervideo WHERE playervideo.active = true ORDER BY RAND()")
    List<Playervideo> findRandomWithRestriction();
}
