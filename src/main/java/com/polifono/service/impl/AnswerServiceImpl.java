package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Answer;
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
    public final List<Answer> findAllByGameId(int gameId) {
        return repository.findAllByGameId(gameId);
    }

    @Override
    public final List<Answer> findAllByGameIdAndLevelId(int gameId, int levelId) {
        return repository.findAllByGameIdAndLevelId(gameId, levelId);
    }

    @Override
    public final List<Answer> findAllByMapId(int mapId) {
        return repository.findAllByMapId(mapId);
    }

    @Override
    public final List<Answer> findAllByPhaseId(int phaseId) {
        return repository.findAllByPhaseId(phaseId);
    }

    @Override
    public final List<Answer> findAllByQuestionId(int questionId) {
        return repository.findAllByQuestionId(questionId);
    }
}
