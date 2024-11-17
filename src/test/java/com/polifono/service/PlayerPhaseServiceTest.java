package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.common.util.DateUtil;
import com.polifono.model.dto.RankingDTO;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Phasestatus;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.model.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;

/**
 * Unit test methods for the PlayerPhaseService.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerPhaseServiceTest {

    @InjectMocks
    private PlayerPhaseService service;

    @Mock
    private IPlayerPhaseRepository repository;

    @Mock
    private PhaseService phaseService;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PHASE_ID_EXISTENT = 1;
    private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PHASE_STATUS_ONGOING = 2;
    private final Integer PHASE_STATUS_CONCLUDED = 3;

    /* save - begin */
    @Test
    public void save_WhenSavePlayerPhase_ReturnPlayerPhaseSaved() {
        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PHASE_ID_EXISTENT);

        Phasestatus phasestatus = new Phasestatus();
        phasestatus.setId(PHASE_STATUS_ONGOING);

        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setPlayer(player);
        playerPhase.setPhase(phase);
        playerPhase.setPhasestatus(phasestatus);
        playerPhase.setGrade(0);
        playerPhase.setDtTest(new Date());
        playerPhase.setNumAttempts(1);
        playerPhase.setScore(0);
        playerPhase.setId(1);

        when(repository.save(playerPhase)).thenReturn(playerPhase);

        PlayerPhase entity = service.save(playerPhase);

        Assertions.assertNotNull(entity, "failure - expected not null");
        Assertions.assertNotEquals(0, entity.getId(), "failure - expected id attribute bigger than 0");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entity.getPlayer().getId(), "failure - expected id player attribute match");
        Assertions.assertEquals(PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId(), "failure - expected id phase attribute match");
        Assertions.assertEquals(PHASE_STATUS_ONGOING.intValue(), entity.getPhasestatus().getId(), "failure - expected phase status attribute match");
        Assertions.assertEquals(0d, entity.getGrade(), 0, "failure - expected grade attribute match");
        Assertions.assertNotNull(entity.getDtTest(), "failure - expected not null");
        Assertions.assertEquals(1, entity.getNumAttempts(), "failure - expected num attempts attribute match");
        Assertions.assertEquals(0, entity.getScore(), "failure - expected score attribute match");
    }
    /* save - end */

    /* findByPlayer - begin */
    @Test
    public void findPlayerPhasesByPlayer_WhenSearchByPlayerExistent_ReturnList() {
        int playerId = PLAYER_ID_EXISTENT;
        List<PlayerPhase> listReturned = new ArrayList<>();
        listReturned.add(new PlayerPhase());
        when(repository.findByPlayer(playerId)).thenReturn(listReturned);

        List<PlayerPhase> list = service.findByPlayer(PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(list, "failure - expected not null");
        Assertions.assertNotEquals(0, list.size(), "failure - not expected list size 0");
    }

    @Test
    public void findPlayerPhasesByPlayer_WhenSearchByPlayerInexistent_ReturnNull() {
        List<PlayerPhase> list = service.findByPlayer(PLAYER_ID_INEXISTENT);
        Assertions.assertNull(list, "failure - expected null");
    }
    /* findByPlayer - end */

    /* findByPlayerPhaseAndStatus - begin */
    @Test
    public void findByPlayerPhaseAndStatus_WhenSearchByPlayerPhaseAndStatusExistents_ReturnPlayerPhase() {
        int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = PHASE_STATUS_CONCLUDED;
        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setId(1);
        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(Optional.of(playerPhase));

        Optional<PlayerPhase> entityOpt = service.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASE_STATUS_CONCLUDED);
        PlayerPhase entity = entityOpt.orElse(null);
        Assertions.assertNotNull(entity, "failure - expected not null");
        Assertions.assertNotEquals(0, entity.getId(), "failure - expected id attribute bigger than 0");
    }

    @Test
    public void findByPlayerPhaseAndStatus_WhenSearchByPlayerAndPhaseInexistentAndStatusConcluded_ReturnNull() {
        Optional<PlayerPhase> entityOpt = service.findByPlayerPhaseAndStatus(PLAYER_ID_INEXISTENT, PHASE_ID_INEXISTENT, PHASE_STATUS_CONCLUDED);
        PlayerPhase entity = entityOpt.orElse(null);
        Assertions.assertNull(entity, "failure - expected not null");
    }
    /* findByPlayerPhaseAndStatus - end */

    /* findLastPhaseCompleted - begin */
    @Test
    public void findLastPhaseCompleted_WhenPlayerHasAlreadyCompletedAtLeastOnePhaseOfTheGame_ReturnPlayerPhase() {
        //PlayerPhase entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT);
        //Assertions.assertNotNull("failure - expected not null", entity);
        //Assertions.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());

        //buscar a próxima fase (order + 1)
        //se for null, o usuário já completou a última fase do game
        //verificar na T007 se essa próxima fase está registrada
        //se não estiver, ok o método buscou a última fase completada
        //se estiver, verificar se o status dela é em andamento
        //se sim, ok
        //se não, o método não buscou a última fase concluída
    }

    @Test
    public void findLastPhaseCompleted_WhenPlayerHasntCompletedAnyPhaseOfTheGame_ReturnNull() {
        Optional<PlayerPhase> entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_INEXISTENT);
        Assertions.assertFalse(entity.isPresent(), "failure - expected null");
    }
    /* findLastPhaseCompleted - end */

    /* findForReportGeneral - begin */
    @Test
    public void findForReportGeneral_WhenSearchByPlayerInexistent_ReturnEmptyList() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        ReportGeneralForm reportGeneralForm = new ReportGeneralForm();
        reportGeneralForm.setGame(game);
        reportGeneralForm.setPhaseBegin(1);
        reportGeneralForm.setPhaseEnd(5);

        List<PlayerPhase> list = service.findForReportGeneral(reportGeneralForm, PLAYER_ID_INEXISTENT);
        Assertions.assertNotNull(list, "failure - expected not null");
    }
    /* findForReportGeneral - end */

    /* isPhaseAlreadyCompletedByPlayer - begin */
    @Test
    public void isPhaseAlreadyCompletedByPlayer_WhenThePhaseIsAlreadyCompletedByThePlayer_ReturnTrue() {
        int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = 3;

        Phase phase = new Phase();
        phase.setId(playerId);

        Player player = new Player();
        player.setId(phaseId);

        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(Optional.of(new PlayerPhase()));

        Assertions.assertTrue(service.isPhaseAlreadyCompletedByPlayer(phase, player.getId()));
    }

    @Test
    public void isPhaseAlreadyCompletedByPlayer_WhenThePhaseIsNotCompletedByThePlayer_ReturnFalse() {
        int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = 3;

        Phase phase = new Phase();
        phase.setId(playerId);

        Player player = new Player();
        player.setId(phaseId);

        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.isPhaseAlreadyCompletedByPlayer(phase, player.getId()));
    }
    /* isPhaseAlreadyCompletedByPlayer - end */

    /* setTestAttempt - begin */
    @Test
    public void setTestAttempt_WhenItIsNotTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithMoreThanOneAttempt() {
        PlayerPhase entity = getEntityStubData();
        entity.setNumAttempts(2);

        when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASE_STATUS_ONGOING)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        PlayerPhase entityReturned = service.setTestAttempt(player, phase);

        Assertions.assertNotNull(entityReturned, "failute expected no null");
        Assertions.assertEquals(entity.getPhase().getId(), entityReturned.getPhase().getId(), "failure match phase attribute");
        Assertions.assertTrue((entityReturned.getNumAttempts() > 1), "failure expected numAttempts attribute bigger than 1");

        verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASE_STATUS_ONGOING);
        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void setTestAttempt_WhenItIsTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithOneAttempt() {
        PlayerPhase entity = getEntityStubData();
        entity.setNumAttempts(1);

        when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASE_STATUS_ONGOING)).thenReturn(Optional.empty());
        when(repository.save(ArgumentMatchers.any(PlayerPhase.class))).thenReturn(entity);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        PlayerPhase entityReturned = service.setTestAttempt(player, phase);

        Assertions.assertNotNull(entityReturned, "failure expected not null");
        Assertions.assertEquals(entity.getPhase().getId(), entityReturned.getPhase().getId(), "failure match phase attribute");
        Assertions.assertEquals(1, entity.getNumAttempts(), "failure match numAttempts attribute");

        verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASE_STATUS_ONGOING);
        verify(repository, times(1)).save(ArgumentMatchers.any(PlayerPhase.class));
        verifyNoMoreInteractions(repository);
    }
    /* setTestAttempt - end */

    /* filterPlayerPhasesListByGame - begin */
    @Test
    public void givenListIsEmpty_whenFilterPlayerPhasesListByGame_thenReturnEmptyList() {
        List<PlayerPhase> list = new ArrayList<>();
        List<Game> result = service.filterPlayerPhasesListByGame(list);

        Assertions.assertNotNull(result, "failure - expected not null");
        Assertions.assertTrue(result.isEmpty(), "failure - expected empty list");
    }

    @Test
    public void givenListContainsPhasesFromSameGame_whenFilterPlayerPhasesListByGame_thenReturnSingleGame() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        Map map = new Map();
        map.setGame(game);

        Phase phase1 = new Phase();
        phase1.setMap(map);

        Phase phase2 = new Phase();
        phase2.setMap(map);

        PlayerPhase playerPhase1 = new PlayerPhase();
        playerPhase1.setPhase(phase1);

        PlayerPhase playerPhase2 = new PlayerPhase();
        playerPhase2.setPhase(phase2);

        List<PlayerPhase> list = new ArrayList<>();
        list.add(playerPhase1);
        list.add(playerPhase2);

        List<Game> result = service.filterPlayerPhasesListByGame(list);

        Assertions.assertNotNull(result, "failure - expected not null");
        Assertions.assertEquals(1, result.size(), "failure - expected list size 1");
        Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), result.get(0).getId(), "failure - expected game id match");
    }

    @Test
    public void givenListContainsPhasesFromDifferentGames_whenFilterPlayerPhasesListByGame_thenReturnMultipleGames() {
        Game game1 = new Game();
        game1.setId(GAME_ID_EXISTENT);

        Game game2 = new Game();
        game2.setId(GAME_ID_EXISTENT + 1);

        Map map1 = new Map();
        map1.setGame(game1);

        Map map2 = new Map();
        map2.setGame(game2);

        Phase phase1 = new Phase();
        phase1.setMap(map1);

        Phase phase2 = new Phase();
        phase2.setMap(map2);

        PlayerPhase playerPhase1 = new PlayerPhase();
        playerPhase1.setPhase(phase1);

        PlayerPhase playerPhase2 = new PlayerPhase();
        playerPhase2.setPhase(phase2);

        List<PlayerPhase> list = new ArrayList<>();
        list.add(playerPhase1);
        list.add(playerPhase2);

        List<Game> result = service.filterPlayerPhasesListByGame(list);

        Assertions.assertNotNull(result, "failure - expected not null");
        Assertions.assertEquals(2, result.size(), "failure - expected list size 2");
        Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), result.get(0).getId(), "failure - expected first game id match");
        Assertions.assertEquals(GAME_ID_EXISTENT + 1, result.get(1).getId(), "failure - expected second game id match");
    }
    /* filterPlayerPhasesListByGame - end */

    /* getRankingMonthly - begin */
    @Test
    public void givenRepositoryReturnsEmptyList_whenGetRankingMonthly_thenReturnEmptyRanking() throws ParseException {
        when(repository.getRanking(DateUtil.getFirstDayOfTheCurrentMonth(), DateUtil.getLastDayOfTheCurrentMonth())).thenReturn(new Object[0][0]);

        List<RankingDTO> ranking = service.getRankingMonthly();

        Assertions.assertNotNull(ranking, "failure - expected not null");
        Assertions.assertTrue(ranking.isEmpty(), "failure - expected empty list");
    }

    @Test
    public void givenRepositoryReturnsListOfPlayers_whenGetRankingMonthly_thenReturnRanking() throws ParseException {
        Object[][] objects = {
                { 1, "John", "Doe", 100 },
                { 2, "Jane", "Smith", 90 }
        };
        when(repository.getRanking(DateUtil.getFirstDayOfTheCurrentMonth(), DateUtil.getLastDayOfTheCurrentMonth())).thenReturn(objects);

        List<RankingDTO> ranking = service.getRankingMonthly();

        Assertions.assertNotNull(ranking, "failure - expected not null");
        Assertions.assertEquals(2, ranking.size(), "failure - expected list size 2");

        RankingDTO first = ranking.get(0);
        Assertions.assertEquals(1, first.player().getId(), "failure - expected player id match");
        Assertions.assertEquals("John", first.player().getName(), "failure - expected player name match");
        Assertions.assertEquals("Doe", first.player().getLastName(), "failure - expected player last name match");
        Assertions.assertEquals(100, first.score(), "failure - expected score match");

        RankingDTO second = ranking.get(1);
        Assertions.assertEquals(2, second.player().getId(), "failure - expected player id match");
        Assertions.assertEquals("Jane", second.player().getName(), "failure - expected player name match");
        Assertions.assertEquals("Smith", second.player().getLastName(), "failure - expected player last name match");
        Assertions.assertEquals(90, second.score(), "failure - expected score match");
    }

    @Test
    public void givenParseExceptionThrown_whenGetRankingMonthly_thenReturnEmptyRanking() {
        try (MockedStatic<DateUtil> mockedDateUtil = Mockito.mockStatic(DateUtil.class)) {
            mockedDateUtil.when(DateUtil::getFirstDayOfTheCurrentMonth).thenThrow(ParseException.class);
            mockedDateUtil.when(DateUtil::getLastDayOfTheCurrentMonth).thenThrow(ParseException.class);

            List<RankingDTO> ranking = service.getRankingMonthly();

            Assertions.assertNotNull(ranking, "failure - expected not null");
            Assertions.assertTrue(ranking.isEmpty(), "failure - expected empty list");
        }
    }
    /* getRankingMonthly - end */

    /* getPermittedLevelForPlayer - begin */
    @Test
    public void givenPlayerHasNotCompletedAnyPhase_whenGetPermittedLevelForPlayer_thenReturnLevel1() {
        int playerId = PLAYER_ID_EXISTENT;
        int gameId = GAME_ID_EXISTENT;

        when(repository.findLastPhaseCompleted(playerId, gameId)).thenReturn(new ArrayList<>());

        int level = service.getPermittedLevelForPlayer(playerId, gameId);

        Assertions.assertEquals(1, level, "failure - expected level 1");
    }

    @Test
    public void givenPlayerHasCompletedPhasesButLastPhaseOfLevelNotFound_whenGetPermittedLevelForPlayer_thenReturnLevel1() {
        int playerId = PLAYER_ID_EXISTENT;
        int gameId = GAME_ID_EXISTENT;

        PlayerPhase playerPhase = new PlayerPhase();
        Phase phase = new Phase();
        Map map = new Map();
        Level level = new Level();
        level.setId(1);
        level.setOrder(1);
        map.setLevel(level);
        phase.setMap(map);
        playerPhase.setPhase(phase);

        when(repository.findLastPhaseCompleted(playerId, gameId)).thenReturn(List.of(playerPhase));
        when(phaseService.findLastPhaseOfTheLevel(gameId, level.getId())).thenReturn(Optional.empty());

        int permittedLevel = service.getPermittedLevelForPlayer(playerId, gameId);

        Assertions.assertEquals(1, permittedLevel, "failure - expected level 1");
    }

    @Test
    public void givenPlayerHasCompletedPhasesAndLastPhaseOfLevelFoundButNotSameAsLastCompletedPhase_whenGetPermittedLevelForPlayer_thenReturnCurrentLevelOrder() {
        int playerId = PLAYER_ID_EXISTENT;
        int gameId = GAME_ID_EXISTENT;

        PlayerPhase playerPhase = new PlayerPhase();
        Phase phase = new Phase();
        phase.setId(1);
        Map map = new Map();
        Level level = new Level();
        level.setId(1);
        level.setOrder(1);
        map.setLevel(level);
        phase.setMap(map);
        playerPhase.setPhase(phase);

        Phase lastPhaseOfLevel = new Phase();
        lastPhaseOfLevel.setId(2);

        when(repository.findLastPhaseCompleted(playerId, gameId)).thenReturn(List.of(playerPhase));
        when(phaseService.findLastPhaseOfTheLevel(gameId, level.getId())).thenReturn(Optional.of(lastPhaseOfLevel));

        int permittedLevel = service.getPermittedLevelForPlayer(playerId, gameId);

        Assertions.assertEquals(level.getOrder(), permittedLevel, "failure - expected current level order");
    }

    @Test
    public void givenPlayerHasCompletedPhasesAndLastPhaseOfLevelFoundAndSameAsLastCompletedPhase_whenGetPermittedLevelForPlayer_thenReturnNextLevelOrder() {
        int playerId = PLAYER_ID_EXISTENT;
        int gameId = GAME_ID_EXISTENT;

        PlayerPhase playerPhase = new PlayerPhase();
        Phase phase = new Phase();
        phase.setId(1);
        Map map = new Map();
        Level level = new Level();
        level.setId(1);
        level.setOrder(1);
        map.setLevel(level);
        phase.setMap(map);
        playerPhase.setPhase(phase);

        Phase lastPhaseOfLevel = new Phase();
        lastPhaseOfLevel.setId(1);

        when(repository.findLastPhaseCompleted(playerId, gameId)).thenReturn(List.of(playerPhase));
        when(phaseService.findLastPhaseOfTheLevel(gameId, level.getId())).thenReturn(Optional.of(lastPhaseOfLevel));

        int permittedLevel = service.getPermittedLevelForPlayer(playerId, gameId);

        Assertions.assertEquals(level.getOrder() + 1, permittedLevel, "failure - expected next level order");
    }
    /* getPermittedLevelForPlayer - end */

    /* stubs - begin */
    private PlayerPhase getEntityStubData() {
        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phasestatus phasestatus = new Phasestatus();
        phasestatus.setId(PHASE_STATUS_ONGOING);

        PlayerPhase entity = new PlayerPhase();
        entity.setNumAttempts(1);
        entity.setPhase(phase);
        entity.setPlayer(player);
        entity.setPhasestatus(phasestatus);

        return entity;
    }
    /* stubs - end */
}
