package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.domain.Question;
import com.polifono.repository.IQuestionRepository;
import com.polifono.service.IQuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements IQuestionService {

    private final IQuestionRepository repository;

    public final Question save(Question question) {
        return repository.save(question);
    }

    public Boolean delete(Integer id) {
        Optional<Question> temp = repository.findById(id);

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

    public final Optional<Question> findById(int questionId) {
        return repository.findById(questionId);
    }

    public final List<Question> findAll() {
        return repository.findAll();
    }

    public final List<Question> findByGame(int gameId) {
        return repository.findByGame(gameId);
    }

    public final List<Question> findByGameAndLevel(int gameId, int levelId) {
        return repository.findByGameAndLevel(gameId, levelId);
    }

    public final List<Question> findByMap(int mapId) {
        return repository.findByMap(mapId);
    }

    public final List<Question> findByPhase(int phaseId) {
        return repository.findByPhase(phaseId);
    }

    public final List<Question> findByContent(int contentId) {
        return repository.findByContent(contentId);
    }
}
