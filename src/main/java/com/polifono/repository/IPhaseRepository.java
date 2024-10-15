package com.polifono.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Phase;

public interface IPhaseRepository extends JpaRepository<Phase, Integer> {

    @Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId")
    List<Phase> findByMap(@Param("mapId") int mapId);

    @Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId")
    List<Phase> findByGame(@Param("gameId") int gameId);

    @Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId AND map.level.id = :levelId")
    List<Phase> findByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);

    @Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId AND phase.order = :phaseOrder")
    Optional<Phase> findByMapAndOrder(@Param("mapId") int mapId, @Param("phaseOrder") int phaseOrder);

    @Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId AND phase.order = :phaseOrder")
    Optional<Phase> findNextPhaseInThisMap(@Param("mapId") int mapId, @Param("phaseOrder") int phaseOrder);

    @Query("SELECT phase FROM PlayerPhase playerPhase, Phase phase, Map map WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND playerPhase.player.id = :playerId AND map.game.id = :gameId ORDER BY phase.order DESC")
    List<Phase> findLastPhaseDoneByPlayerAndGame(@Param("playerId") int playerId, @Param("gameId") int gameId);

    @Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId AND map.level.id = :levelId ORDER BY phase.order DESC")
    List<Phase> findLastPhaseOfTheLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);

    @Query("SELECT phase FROM PlayerPhase playerPhase, Phase phase, Map map, Game game WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND map.game.id = game.id AND playerPhase.player.id = :playerId ORDER BY game.id, phase.id DESC")
    List<Phase> findGamesForProfile(@Param("playerId") int playerId);

    @Query("SELECT phase FROM Content content, Phase phase, Map map, Game game, PlayerPhase playerPhase WHERE content.phase.id = phase.id AND phase.map.id = map.id AND map.game.id = game.id AND phase.id = playerPhase.phase.id AND playerPhase.phasestatus.id = :phasestatus AND playerPhase.player.id = :playerId AND content.content LIKE :q ORDER BY game.name, phase.order ASC")
    List<Phase> findPhasesBySearchAndUser(@Param("q") String q, @Param("playerId") int playerId, @Param("phasestatus") int phasestatus);
}
