package com.polifono.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Content;

public interface IContentRepository extends JpaRepository<Content, Integer> {

    @Query("SELECT content FROM Content content WHERE content.contenttype.id = 2")
    List<Content> findAllText();

    @Query("SELECT content FROM Content content WHERE content.contenttype.id = 1")
    List<Content> findAllTest();

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 2 AND map.game.id = :gameId")
    List<Content> findContentsTextByGame(@Param("gameId") int gameId);

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 2 AND map.game.id = :gameId AND map.level.id = :levelId")
    List<Content> findContentsTextByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 2 AND map.id = :mapId")
    List<Content> findContentsTextByMap(@Param("mapId") int mapId);

    @Query("SELECT content FROM Content content, Phase phase WHERE content.phase.id = phase.id AND content.contenttype.id = 2 AND phase.id = :phaseId")
    List<Content> findContentsTextByPhase(@Param("phaseId") int phaseId);

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId")
    List<Content> findContentsTestByGame(@Param("gameId") int gameId);

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.game.id = :gameId AND map.level.id = :levelId")
    List<Content> findContentsTestByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);

    @Query("SELECT content FROM Content content, Phase phase, Map map WHERE content.phase.id = phase.id AND phase.map.id = map.id AND content.contenttype.id = 1 AND map.id = :mapId")
    List<Content> findContentsTestByMap(@Param("mapId") int mapId);

    @Query("SELECT content FROM Content content, Phase phase WHERE content.phase.id = phase.id AND content.contenttype.id = 1 AND phase.id = :phaseId")
    List<Content> findContentsTestByPhase(@Param("phaseId") int phaseId);

    @Query("SELECT content FROM Content content WHERE content.phase.id = :phaseId AND content.order = :contentOrder")
    Optional<Content> findByPhaseAndOrder(@Param("phaseId") int phaseId, @Param("contentOrder") int contentOrder);
}
