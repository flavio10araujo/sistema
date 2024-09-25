package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Answer;
import com.polifono.repository.IAnswerRepository;
import com.polifono.service.IAnswerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements IAnswerService {

    private final IAnswerRepository repository;

    @Override
    public final Answer save(Answer answer) {
        return repository.save(answer);
    }

    @Override
    public final Boolean delete(final Integer id) {
        Optional<Answer> temp = repository.findById(id);

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

    @Override
    public final Optional<Answer> findById(int answerId) {
        return repository.findById(answerId);
    }

    @Override
    public final List<Answer> findAll() {
        return repository.findAll();
    }

    @Override
    public final List<Answer> findByGame(int gameId) {
        return repository.findByGame(gameId);
    }

    @Override
    public final List<Answer> findByGameAndLevel(int gameId, int levelId) {
        return repository.findByGameAndLevel(gameId, levelId);
    }

    @Override
    public final List<Answer> findByMap(int mapId) {
        return repository.findByMap(mapId);
    }

    @Override
    public final List<Answer> findByPhase(int phaseId) {
        return repository.findByPhase(phaseId);
    }

    @Override
    public final List<Answer> findByQuestion(int questionId) {
        return repository.findByQuestion(questionId);
    }
}
