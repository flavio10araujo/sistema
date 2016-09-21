package com.polifono.service;

import java.util.List;

import com.polifono.domain.Content;

public interface IContentService {

	public Content save(Content content);
	
	public Boolean delete(Integer id);
	
	public Content findOne(int contentId);
	
	public List<Content> findAll();
	
	public List<Content> findAllText();
	
	public List<Content> findContentsTextByGame(int gameId);
	
	public List<Content> findContentsTextByGameAndLevel(int gameId, int levelId);
	
	public List<Content> findContentsTextByMap(int mapId);
	
	public List<Content> findContentsTextByPhase(int phaseId);
	
	public List<Content> findAllTest();
	
	public List<Content> findContentsTestByGame(int gameId);
	
	public List<Content> findContentsTestByGameAndLevel(int gameId, int levelId);
	
	public List<Content> findContentsTestByMap(int mapId);
	
	public List<Content> findContentsTestByPhase(int phaseId);

	public Content findByPhaseAndOrder(int phaseId, int contentOrder);

}