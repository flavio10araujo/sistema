package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Content;
import com.polifono.repository.IContentRepository;
import com.polifono.service.IContentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements IContentService {

    private final IContentRepository repository;

    public Content save(Content content) {
        return repository.save(content);
    }

    public boolean delete(Integer id) {
        Optional<Content> temp = repository.findById(id);

        if (temp.isPresent()) {
            try {
                repository.delete(temp.get());
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public Optional<Content> findById(int contentId) {
        return repository.findById(contentId);
    }

    public List<Content> findAll() {
        return repository.findAll();
    }

    /**
     * Only content with C008_ID = 2 (Text).
     */
    public List<Content> findAllText() {
        return repository.findAllText();
    }

    /**
     * Only content with C008_ID = 2 (Text).
     */
    public List<Content> findContentsTextByGame(int gameId) {
        return repository.findContentsTextByGame(gameId);
    }

    /**
     * Only content with C008_ID = 2 (Text).
     */
    public List<Content> findContentsTextByGameAndLevel(int gameId, int levelId) {
        return repository.findContentsTextByGameAndLevel(gameId, levelId);
    }

    /**
     * Only content with C008_ID = 2 (Text).
     */
    public List<Content> findContentsTextByMap(int mapId) {
        return repository.findContentsTextByMap(mapId);
    }

    /**
     * Only content with C008_ID = 2 (Text).
     */
    public List<Content> findContentsTextByPhase(int phaseId) {
        return repository.findContentsTextByPhase(phaseId);
    }

    /**
     * Only content with C008_ID = 1 (Test).
     */
    public List<Content> findAllTest() {
        return repository.findAllTest();
    }

    /**
     * Only content with C008_ID = 1 (Test).
     */
    public List<Content> findContentsTestByGame(int gameId) {
        return repository.findContentsTestByGame(gameId);
    }

    /**
     * Only content with C008_ID = 1 (Test).
     */
    public List<Content> findContentsTestByGameAndLevel(int gameId, int levelId) {
        return repository.findContentsTestByGameAndLevel(gameId, levelId);
    }

    /**
     * Only content with C008_ID = 1 (Test).
     */
    public List<Content> findContentsTestByMap(int mapId) {
        return repository.findContentsTestByMap(mapId);
    }

    /**
     * Only content with C008_ID = 1 (Test).
     */
    public List<Content> findContentsTestByPhase(int phaseId) {
        return repository.findContentsTestByPhase(phaseId);
    }

    public Optional<Content> findByPhaseAndOrder(int phaseId, int contentOrder) {
        return repository.findByPhaseAndOrder(phaseId, contentOrder);
    }
}
