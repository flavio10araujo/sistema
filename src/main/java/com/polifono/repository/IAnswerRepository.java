package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Answer;

public interface IAnswerRepository extends JpaRepository<Answer, Integer> {

    @Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question.id = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId")
    List<Answer> findByGame(@Param("gameId") int gameId);

    @Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question.id = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId AND map.level.id = :levelId")
    List<Answer> findByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);

    @Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question.id = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.id = :mapId")
    List<Answer> findByMap(@Param("mapId") int mapId);

    @Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase WHERE answer.question.id = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND content.contenttype.id = 1 AND phase.id = :phaseId")
    List<Answer> findByPhase(@Param("phaseId") int phaseId);

    @Query("SELECT answer FROM Answer answer, Question question WHERE answer.question.id = question.id AND question.id = :questionId")
    List<Answer> findByQuestion(@Param("questionId") int questionId);
}
