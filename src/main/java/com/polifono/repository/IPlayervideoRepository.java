package com.polifono.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayervideoRepository extends JpaRepository<Playervideo, Integer> {

	List<Playervideo> findAllByPlayer(Player player, Pageable pageable);
	
	@Query("SELECT playervideo FROM Playervideo playervideo, Content content, Phase phase WHERE playervideo.content.id = content.id AND content.phase.id = phase.id AND content.order = 1 AND playervideo.player.id = :playerId AND phase.id = :phaseId")
	public Playervideo findByPlayerAndPhase(@Param("playerId") int playerId, @Param("phaseId") int phaseId);
}