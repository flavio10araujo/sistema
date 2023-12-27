package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;
import com.polifono.service.impl.PlayerPhaseServiceImpl;

/**
 * Unit test methods for the PlayerPhaseService.
 */
@Transactional
public class PlayerPhaseServiceTest extends AbstractTest {

    private IPlayerPhaseService service;

    @Mock
    private IPlayerPhaseRepository repository;

    private final Integer PLAYER_ID_EXISTENT = 1;
    private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PHASE_ID_EXISTENT = 1;
    private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PHASESTATUS_ONGOING = 2;
    private final Integer PHASESTATUS_CONCLUDED = 3;

    @BeforeEach
    public void setUp() {
        // Do something before each test method.
        MockitoAnnotations.initMocks(this);
        service = new PlayerPhaseServiceImpl(repository);
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test method.
    }

    /* stubs - begin */
    private Optional<PlayerPhase> getEntityStubData() {
        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phasestatus phasestatus = new Phasestatus();
        phasestatus.setId(PHASESTATUS_ONGOING);

        PlayerPhase entity = new PlayerPhase();
        entity.setNumAttempts(1);
        entity.setPhase(phase);
        entity.setPlayer(player);
        entity.setPhasestatus(phasestatus);

        return Optional.of(entity);
    }
    /* stubs - end */

    /* save - begin */
    @Test
    public void save_WhenSavePlayerPhase_ReturnPlayerPhaseSaved() {
        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PHASE_ID_EXISTENT);

        Phasestatus phasestatus = new Phasestatus();
        phasestatus.setId(PHASESTATUS_ONGOING);

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
        Assertions.assertEquals(PHASESTATUS_ONGOING.intValue(), entity.getPhasestatus().getId(), "failure - expected phase status attribute match");
        Assertions.assertEquals(0d, entity.getGrade(), 0, "failure - expected grade attribute match");
        Assertions.assertNotNull(entity.getDtTest(), "failure - expected not null");
        Assertions.assertEquals(1, entity.getNumAttempts(), "failure - expected num attempts attribute match");
        Assertions.assertEquals(0, entity.getScore(), "failure - expected score attribute match");
    }

    /*@Test
    public void save_whenTestCompleted_saveTheGradeOfTheTest() {
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);

    	Phase phase = new Phase();
    	phase.setId(PHASE_ID_EXISTENT);

    	Phasestatus phasestatus = new Phasestatus();
    	phasestatus.setId(PHASESTATUS_CONCLUDED);

    	PlayerPhase playerPhase = new PlayerPhase();
    	playerPhase.setPlayer(player);
    	playerPhase.setPhase(phase);
    	playerPhase.setPhasestatus(phasestatus);
    	playerPhase.setGrade(70);
    	playerPhase.setDtTest(new Date());
    	playerPhase.setNumAttempts(1);
    	playerPhase.setScore(50);
    	playerPhase.setId(1);

    	when(repository.save(playerPhase)).thenReturn(playerPhase);

    	PlayerPhase entity = service.save(playerPhase);

    	Assertions.assertNotNull("failure - expected not null", entity);
    	Assertions.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	Assertions.assertEquals("failure - expected id player attribute match", PLAYER_ID_EXISTENT.intValue(), entity.getPlayer().getId());
    	Assertions.assertEquals("failure - expected id phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	Assertions.assertEquals("failure - expected phase status attribute match", PHASESTATUS_CONCLUDED.intValue(), entity.getPhasestatus().getId());
    	Assertions.assertTrue("failure - expected grade attribute match", entity.getGrade() >= 70);
    	Assertions.assertNotNull("failure - expected not null", entity.getDtTest());
    	Assertions.assertTrue("failure - expected num attempts attribute match", entity.getNumAttempts() >= 1);
    	Assertions.assertTrue("failure - expected score attribute match", entity.getScore() >= 10);
    }*/
    /* save - end */

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
        PlayerPhase entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_INEXISTENT);
        Assertions.assertNull(entity, "failure - expected null");
    }
    /* findLastPhaseCompleted - end */

    /* findByPlayerPhaseAndStatus - begin */
    @Test
    public void findByPlayerPhaseAndStatus_WhenSearchByPlayerPhaseAndStatusExistents_ReturnPlayerPhase() {
        int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = PHASESTATUS_CONCLUDED;
        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setId(1);
        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(playerPhase);

        PlayerPhase entity = service.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_CONCLUDED);
        Assertions.assertNotNull(entity, "failure - expected not null");
        Assertions.assertNotEquals(0, entity.getId(), "failure - expected id attribute bigger than 0");
    }

    @Test
    public void findByPlayerPhaseAndStatus_WhenSearchByPlayerAndPhaseInexistentAndStatusConcluded_ReturnNull() {
        PlayerPhase entity = service.findByPlayerPhaseAndStatus(PLAYER_ID_INEXISTENT, PHASE_ID_INEXISTENT, PHASESTATUS_CONCLUDED);
        Assertions.assertNull(entity, "failure - expected not null");
    }
    /* findByPlayerPhaseAndStatus - end */

    /* findPlayerPhasesByPlayer - begin */
    @Test
    public void findPlayerPhasesByPlayer_WhenSearchByPlayerExistent_ReturnList() {
        int playerId = PLAYER_ID_EXISTENT;
        List<PlayerPhase> listReturned = new ArrayList<PlayerPhase>();
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
    /* findPlayerPhasesByPlayer - end */

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

        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(new PlayerPhase());

        Assertions.assertTrue(service.isPhaseAlreadyCompletedByPlayer(phase, player));
    }

    @Test
    public void isPhaseAlreadyCompletedByPlayer_WhenThePhaseIsNotCompletedByThePlayer_ReturnFalse() {
        int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = 3;

        Phase phase = new Phase();
        phase.setId(playerId);

        Player player = new Player();
        player.setId(phaseId);

        when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(null);

        Assertions.assertFalse(service.isPhaseAlreadyCompletedByPlayer(phase, player));
    }
    /* isPhaseAlreadyCompletedByPlayer - end */

    /* setTestAttempt - begin */
    @Test
    public void setTestAttempt_WhenItIsNotTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithMoreThanOneAttempt() {
        PlayerPhase entity = getEntityStubData().get();
        entity.setNumAttempts(2);

        when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        PlayerPhase entityReturned = service.setTestAttempt(player, phase);

        Assertions.assertNotNull(entityReturned, "failute expected no null");
        Assertions.assertEquals(entity.getPhase().getId(), entityReturned.getPhase().getId(), "failure match phase attribute");
        Assertions.assertTrue((entityReturned.getNumAttempts() > 1), "failure expected numAttempts attribute bigger than 1");

        verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING);
        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void setTestAttempt_WhenItIsTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithOneAttempt() {
        PlayerPhase entity = getEntityStubData().get();
        entity.setNumAttempts(1);

        when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING)).thenReturn(null);
        when(repository.save(ArgumentMatchers.any(PlayerPhase.class))).thenReturn(entity);

        Player player = new Player();
        player.setId(PHASE_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(PLAYER_ID_EXISTENT);

        PlayerPhase entityReturned = service.setTestAttempt(player, phase);

        Assertions.assertNotNull(entityReturned, "failure expected not null");
        Assertions.assertEquals(entity.getPhase().getId(), entityReturned.getPhase().getId(), "failure match phase attribute");
        Assertions.assertEquals(1, entity.getNumAttempts(), "failure match numAttempts attribute");

        verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING);
        verify(repository, times(1)).save(ArgumentMatchers.any(PlayerPhase.class));
        verifyNoMoreInteractions(repository);
    }
    /* setTestAttempt - end */
}
