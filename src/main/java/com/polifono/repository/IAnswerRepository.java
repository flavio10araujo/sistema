package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Answer;

public interface IAnswerRepository extends CrudRepository<Answer, Integer> {

	@Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId")
	public List<Answer> findAnswersByGame(@Param("gameId") int gameId);
	
	@Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId AND map.level.id = :levelId")
	public List<Answer> findAnswersByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);
	
	@Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase, Map map WHERE answer.question = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.id = :mapId")
	public List<Answer> findAnswersByMap(@Param("mapId") int mapId);
	
	@Query("SELECT answer FROM Answer answer, Question question, Content content, Phase phase WHERE answer.question = question.id AND question.content.id = content.id AND content.phase.id = phase.id AND content.contenttype.id = 1 AND phase.id = :phaseId")
	public List<Answer> findAnswersByPhase(@Param("phaseId") int phaseId);
	
	@Query("SELECT answer FROM Answer answer, Question question WHERE answer.question = question.id AND question.id = :questionId")
	public List<Answer> findAnswersByQuestion(@Param("questionId") int questionId);

}