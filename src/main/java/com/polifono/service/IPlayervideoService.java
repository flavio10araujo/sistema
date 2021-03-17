package com.polifono.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.polifono.domain.Content;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayervideoService {

	public Playervideo save(Playervideo o);
	
	//public Boolean delete(Integer id);
	
	//public Playervideo findOne(int id);
	
	public List<Playervideo> findAll();
	
	public List<Playervideo> findAll(Pageable pageable);
	
	public List<Playervideo> findAllByContent(Content content, Pageable pageable);
	
	public List<Playervideo> findAllByPlayer(Player player, Pageable pageable);
	
	public Playervideo findByPlayerAndPhase(Player player, Phase phase);
	
	public List<Playervideo> findRandomWithRestriction(String restriction);
}