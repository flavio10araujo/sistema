package com.polifono.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Answer;
import com.polifono.repository.IAnswerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final IAnswerRepository repository;

    public final Answer save(Answer answer) {
        return repository.save(answer);
    }

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

    public final Optional<Answer> findById(int answerId) {
        return repository.findById(answerId);
    }

    public final List<Answer> findAll() {
        return repository.findAll();
    }

    public final List<Answer> findAllByGameId(int gameId) {
        return repository.findAllByGameId(gameId);
    }

    public final List<Answer> findAllByGameIdAndLevelId(int gameId, int levelId) {
        return repository.findAllByGameIdAndLevelId(gameId, levelId);
    }

    public final List<Answer> findAllByMapId(int mapId) {
        return repository.findAllByMapId(mapId);
    }

    public final List<Answer> findAllByPhaseId(int phaseId) {
        return repository.findAllByPhaseId(phaseId);
    }

    public final List<Answer> findAllByQuestionId(int questionId) {
        return repository.findAllByQuestionId(questionId);
    }
}
