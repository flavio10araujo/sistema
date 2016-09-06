package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Question;

public interface IQuestionRepository extends CrudRepository<Question, Integer> {

	@Query("SELECT question FROM Question question, Content content, Phase phase, Map map WHERE question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId")
	public List<Question> findQuestionsByGame(@Param("gameId") int gameId);
	
	@Query("SELECT question FROM Question question, Content content, Phase phase, Map map WHERE question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId AND map.level.id = :levelId")
	public List<Question> findQuestionsByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);
	
	@Query("SELECT question FROM Question question, Content content, Phase phase, Map map WHERE question.content.id = content.id AND content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.id = :mapId")
	public List<Question> findQuestionsByMap(@Param("mapId") int mapId);
	
	@Query("SELECT question FROM Question question, Content content, Phase phase WHERE question.content.id = content.id AND content.phase.id = phase.id AND content.contenttype.id = 1 AND phase.id = :phaseId")
	public List<Question> findQuestionsByPhase(@Param("phaseId") int phaseId);
	
	@Query("SELECT question FROM Question question WHERE question.content.id = :contentId")
	public List<Question> findQuestionsByContent(@Param("contentId") int contentId);

}