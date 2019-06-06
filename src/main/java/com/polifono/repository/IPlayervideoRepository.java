package com.polifono.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayervideoRepository extends JpaRepository<Playervideo, Integer> {

	List<Playervideo> findAllByPlayer(Player player, Pageable pageable);
}