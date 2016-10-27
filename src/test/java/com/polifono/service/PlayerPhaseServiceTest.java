package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
 * 
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

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
		service = new PlayerPhaseServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private PlayerPhase getEntityStubData() {
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
    	
    	return entity;
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
    	
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	Assert.assertEquals("failure - expected id player attribute match", PLAYER_ID_EXISTENT.intValue(), entity.getPlayer().getId());
    	Assert.assertEquals("failure - expected id phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	Assert.assertEquals("failure - expected phase status attribute match", PHASESTATUS_ONGOING.intValue(), entity.getPhasestatus().getId());
    	Assert.assertEquals("failure - expected grade attribute match", 0d, entity.getGrade(), 0);
    	Assert.assertNotNull("failure - expected not null", entity.getDtTest());
    	Assert.assertEquals("failure - expected num attempts attribute match", 1, entity.getNumAttempts());
    	Assert.assertEquals("failure - expected score attribute match", 0, entity.getScore());
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
    	
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	Assert.assertEquals("failure - expected id player attribute match", PLAYER_ID_EXISTENT.intValue(), entity.getPlayer().getId());
    	Assert.assertEquals("failure - expected id phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	Assert.assertEquals("failure - expected phase status attribute match", PHASESTATUS_CONCLUDED.intValue(), entity.getPhasestatus().getId());
    	Assert.assertTrue("failure - expected grade attribute match", entity.getGrade() >= 70);
    	Assert.assertNotNull("failure - expected not null", entity.getDtTest());
    	Assert.assertTrue("failure - expected num attempts attribute match", entity.getNumAttempts() >= 1);
    	Assert.assertTrue("failure - expected score attribute match", entity.getScore() >= 10);
    }*/
    /* save - end */
    
    /* findLastPhaseCompleted - begin */
    @Test
    public void findLastPhaseCompleted_WhenPlayerHasAlreadyCompletedAtLeastOnePhaseOfTheGame_ReturnPlayerPhase() {
    	//PlayerPhase entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT);
    	//Assert.assertNotNull("failure - expected not null", entity);
    	//Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
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
    	Assert.assertNull("failure - expected null", entity);
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
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    }
    
    @Test
    public void findByPlayerPhaseAndStatus_WhenSearchByPlayerAndPhaseInexistentAndStatusConcluded_ReturnNull() {
    	PlayerPhase entity = service.findByPlayerPhaseAndStatus(PLAYER_ID_INEXISTENT, PHASE_ID_INEXISTENT, PHASESTATUS_CONCLUDED);
    	Assert.assertNull("failure - expected not null", entity);
    }
    /* findByPlayerPhaseAndStatus - end */
    
    /* findPlayerPhasesByPlayer - begin */
    @Test
    public void findPlayerPhasesByPlayer_WhenSearchByPlayerExistent_ReturnList() {
    	int playerId = PLAYER_ID_EXISTENT;
    	List<PlayerPhase> listReturned = new ArrayList<PlayerPhase>();
    	listReturned.add(new PlayerPhase());
    	when(repository.findPlayerPhasesByPlayer(playerId)).thenReturn(listReturned);
    	
    	List<PlayerPhase> list = service.findPlayerPhasesByPlayer(PLAYER_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    
    @Test
    public void findPlayerPhasesByPlayer_WhenSearchByPlayerInexistent_ReturnNull() {
    	List<PlayerPhase> list = service.findPlayerPhasesByPlayer(PLAYER_ID_INEXISTENT);
    	Assert.assertNull("failure - expected null", list);
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
    	Assert.assertNotNull("failure - expected not null", list);
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
    	
    	Assert.assertTrue(service.isPhaseAlreadyCompletedByPlayer(phase, player));
    }
    
    @Test
    public void isPhaseAlreadyCompletedByPlayer_WhenThePhaseIsNotCompletedByThePlayer_ReturnFalse() {
    	int playerId = PLAYER_ID_EXISTENT, phaseId = PHASE_ID_EXISTENT, phasestatusId = 3;
    	
    	Phase phase = new Phase();
    	phase.setId(playerId);
    	
    	Player player = new Player();
    	player.setId(phaseId);
    	
    	when(repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId)).thenReturn(null);
    	
    	Assert.assertFalse(service.isPhaseAlreadyCompletedByPlayer(phase, player));
    }
    /* isPhaseAlreadyCompletedByPlayer - end */
    
    /* setTestAttempt - begin */
    @Test
    public void setTestAttempt_WhenItIsNotTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithMoreThanOneAttempt() {
    	PlayerPhase entity = getEntityStubData();
    	entity.setNumAttempts(2);
    	
    	when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Player player = new Player();
    	player.setId(PHASE_ID_EXISTENT);
    	
    	Phase phase = new Phase();
    	phase.setId(PLAYER_ID_EXISTENT);
    	
    	PlayerPhase entityReturned = service.setTestAttempt(player, phase);
    	
    	Assert.assertNotNull("failute expected no null", entityReturned);
    	Assert.assertEquals("failure match phase attribute", entity.getPhase().getId(), entityReturned.getPhase().getId());
    	Assert.assertTrue("failure expected numAttempts attribute bigger than 1", (entityReturned.getNumAttempts() > 1));
		
    	verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING);
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void setTestAttempt_WhenItIsTheFirstAttemptToDoThisTest_ReturnPlayerPhaseWithOneAttempt() {
    	PlayerPhase entity = getEntityStubData();
    	entity.setNumAttempts(1);
    	
    	when(repository.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING)).thenReturn(null);
    	when(repository.save(Matchers.any(PlayerPhase.class))).thenReturn(entity);
    	
    	Player player = new Player();
    	player.setId(PHASE_ID_EXISTENT);
    	
    	Phase phase = new Phase();
    	phase.setId(PLAYER_ID_EXISTENT);
    	
    	PlayerPhase entityReturned = service.setTestAttempt(player, phase);
    	
    	Assert.assertNotNull("failute expected not null", entityReturned);
    	Assert.assertEquals("failure match phase attribute", entity.getPhase().getId(), entityReturned.getPhase().getId());
    	Assert.assertEquals("failure match numAttempts attribute", 1, entity.getNumAttempts());
		
    	verify(repository, times(1)).findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_ONGOING);
    	verify(repository, times(1)).save(Matchers.any(PlayerPhase.class));
        verifyNoMoreInteractions(repository);
    }
    /* setTestAttempt - end */
}