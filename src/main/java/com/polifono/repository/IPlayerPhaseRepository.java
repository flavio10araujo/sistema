package com.polifono.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.PlayerPhase;

public interface IPlayerPhaseRepository extends JpaRepository<PlayerPhase, UUID> {

    @Query("""
            SELECT playerPhase
            FROM PlayerPhase playerPhase, Phase phase, Map map
            WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND playerPhase.player.id = :playerId AND playerPhase.phasestatus.id = 3 AND map.game.id = :gameId
            ORDER BY phase.order DESC
            """)
    List<PlayerPhase> findLastPhaseCompleted(@Param("playerId") int playerId, @Param("gameId") int gameId);

    @Query("""
            SELECT playerPhase
            FROM PlayerPhase playerPhase, Phase phase, Map map, Game game
            WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND map.game.id = game.id AND playerPhase.player.id = :playerId
            ORDER BY game.id, phase.id ASC
            """)
    List<PlayerPhase> findByPlayer(@Param("playerId") int playerId);

    @Query("""
            SELECT playerPhase
            FROM PlayerPhase playerPhase
            WHERE playerPhase.player.id = :playerId AND playerPhase.phase.id = :phaseId AND playerPhase.phasestatus.id = :phasestatusId
            """)
    Optional<PlayerPhase> findByPlayerPhaseAndStatus(@Param("playerId") int playerId, @Param("phaseId") int phaseId, @Param("phasestatusId") int phasestatusId);

    @Query("""
            SELECT playerPhase
            FROM PlayerPhase playerPhase, Phase phase, Map map
            WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND playerPhase.player.id = :playerId
            AND playerPhase.phasestatus.id = 3 AND map.game.id = :gameId
            AND (phase.order >= :phaseBegin AND phase.order <= :phaseEnd)
            """)
    List<PlayerPhase> findForReportGeneral(@Param("playerId") int playerId, @Param("gameId") int gameId, @Param("phaseBegin") int phaseBegin,
            @Param("phaseEnd") int phaseEnd);

    @Query(value = """
            SELECT t1.c001_id, t1.C001_NAME, t1.c001_LAST_NAME, SUM(t7.C007_SCORE)
            FROM t007_player_phase t7, t001_player t1
            WHERE t1.c001_id = t7.c001_id AND t7.c006_id = 3 AND DATE(t7.c007_dt_test) BETWEEN :dateBegin AND :dateEnd AND t1.c001_id not in (1, 2)
            GROUP BY t1.c001_id
            ORDER BY 4 DESC
            LIMIT 10
            """, nativeQuery = true)
    Object[][] getRanking(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);
}
