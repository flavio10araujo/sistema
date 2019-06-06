package com.polifono.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayervideoService {

	public Playervideo save(Playervideo o);
	
	//public Boolean delete(Integer id);
	
	//public Playervideo findOne(int id);
	
	public List<Playervideo> findAll();
	
	
	public List<Playervideo> findAllByPlayer(Player player, Pageable pageable);
}