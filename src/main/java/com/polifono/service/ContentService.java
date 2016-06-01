package com.polifono.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Content;
import com.polifono.domain.Phase;
import com.polifono.repository.ContentRepository;

@Service
public class ContentService {

	private final ContentRepository contentRepository;
	
	@Autowired
	public ContentService(final ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}
	
	public final Content save(Content content) {
		return contentRepository.save(content);
	}
	
	public Boolean delete(Integer id) {
		Content temp = contentRepository.findOne(id);
		
		if (temp != null) {
			try {
				contentRepository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Content find(int contentId) {
		return contentRepository.findOne(contentId);
	}
	
	public final List<Content> findAll() {
		return (List<Content>) contentRepository.findAll();
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @return
	 */
	public final List<Content> findAllText() {
		return (List<Content>) contentRepository.findAllText();
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param gameId
	 * @return
	 */
	public final List<Content> findContentsTextByGame(int gameId) {
		return contentRepository.findContentsTextByGame(gameId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param gameId
	 * @param levelId
	 * @return
	 */
	public final List<Content> findContentsTextByGameAndLevel(int gameId, int levelId) {
		return contentRepository.findContentsTextByGameAndLevel(gameId, levelId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param mapId
	 * @return
	 */
	public final List<Content> findContentsTextByMap(int mapId) {
		return contentRepository.findContentsTextByMap(mapId);
	}
	
	/**
	 * Only content with C008_ID = 2 (Text).
	 * 
	 * @param phaseId
	 * @return
	 */
	public final List<Content> findContentsTextByPhase(int phaseId) {
		return contentRepository.findContentsTextByPhase(phaseId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @return
	 */
	public final List<Content> findAllTest() {
		return (List<Content>) contentRepository.findAllTest();
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param gameId
	 * @return
	 */
	public final List<Content> findContentsTestByGame(int gameId) {
		return contentRepository.findContentsTestByGame(gameId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param gameId
	 * @param levelId
	 * @return
	 */
	public final List<Content> findContentsTestByGameAndLevel(int gameId, int levelId) {
		return contentRepository.findContentsTestByGameAndLevel(gameId, levelId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param mapId
	 * @return
	 */
	public final List<Content> findContentsTestByMap(int mapId) {
		return contentRepository.findContentsTestByMap(mapId);
	}
	
	/**
	 * Only content with C008_ID = 1 (Test).
	 * 
	 * @param phaseId
	 * @return
	 */
	public final List<Content> findContentsTestByPhase(int phaseId) {
		return contentRepository.findContentsTestByPhase(phaseId);
	}

	public final Content findContentByPhaseAndOrder(Phase phase, int contentOrder) {
		Content content = contentRepository.findContentByPhaseAndOrder(phase.getId(), contentOrder);
		return content;
	}
}