package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Content;
import com.polifono.repository.IContentRepository;
import com.polifono.service.IContentService;

@Service
public class ContentServiceImpl implements IContentService {

	private IContentRepository repository;
	
	@Autowired
	public ContentServiceImpl(IContentRepository repository) {
		this.repository = repository;
	}
	
	public final Content save(Content content) {
		return repository.save(content);
	}
	
	public Boolean delete(Integer id) {
		Content temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Content findOne(int contentId) {
		return repository.findOne(contentId);
	}
	
	public final List<Content> findAll() {
		return (List<Content>) repository.findAll();
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @return
	 */
	public final List<Content> findAllText() {
		return (List<Content>) repository.findAllText();
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param gameId
	 * @return
	 */
	public final List<Content> findContentsTextByGame(int gameId) {
		return repository.findContentsTextByGame(gameId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param gameId
	 * @param levelId
	 * @return
	 */
	public final List<Content> findContentsTextByGameAndLevel(int gameId, int levelId) {
		return repository.findContentsTextByGameAndLevel(gameId, levelId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param mapId
	 * @return
	 */
	public final List<Content> findContentsTextByMap(int mapId) {
		return repository.findContentsTextByMap(mapId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param phaseId
	 * @return
	 */
	public final List<Content> findContentsTextByPhase(int phaseId) {
		return repository.findContentsTextByPhase(phaseId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @return
	 */
	public final List<Content> findAllTest() {
		return (List<Content>) repository.findAllTest();
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param gameId
	 * @return
	 */
	public final List<Content> findContentsTestByGame(int gameId) {
		return repository.findContentsTestByGame(gameId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param gameId
	 * @param levelId
	 * @return
	 */
	public final List<Content> findContentsTestByGameAndLevel(int gameId, int levelId) {
		return repository.findContentsTestByGameAndLevel(gameId, levelId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param mapId
	 * @return
	 */
	public final List<Content> findContentsTestByMap(int mapId) {
		return repository.findContentsTestByMap(mapId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param phaseId
	 * @return
	 */
	public final List<Content> findContentsTestByPhase(int phaseId) {
		return repository.findContentsTestByPhase(phaseId);
	}

	public final Content findByPhaseAndOrder(int phaseId, int contentOrder) {
		return repository.findByPhaseAndOrder(phaseId, contentOrder);
	}
}