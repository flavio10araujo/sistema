package com.polifono.serviceIT;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.service.IPlayerPhaseService;

/**
 * Unit test methods for the PlayerPhaseService.
 * 
 */
@Transactional
public class PlayerPhaseServiceTest extends AbstractTest {

	@Autowired
    private IPlayerPhaseService service;
	
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
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
    //@Test
    public void save_whenTestNotCompleted_saveOneAttempt() {
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

    //@Test
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
    }
    /* save - end */
    
    /* findLastPhaseCompleted - begin */
    //@Test
    public void findLastPhaseCompleted_whenPlayerHasAlreadyCompletedAtLeastOnePhaseOfTheGame_returnItem() {
    	PlayerPhase entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	//buscar a próxima fase (order + 1)
    		//se for null, o usuário já completou a última fase do game
    	//verificar na T007 se essa próxima fase está registrada
    		//se não estiver, ok o método buscou a última fase completada
    		//se estiver, verificar se o status dela é em andamento
    			//se sim, ok
    			//se não, o método não buscou a última fase concluída
    }
    
    //@Test
    public void findLastPhaseCompleted_whenPlayerHasntCompletedAnyPhaseOfTheGame_returnNull() {
    	PlayerPhase entity = service.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_INEXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findLastPhaseCompleted - end */
    
    /* findByPlayerPhaseAndStatus - begin */
    //@Test
    public void findByPlayerPhaseAndStatus_whenPlayerPhaseExists_returnItem() {
    	PlayerPhase entity = service.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, PHASE_ID_EXISTENT, PHASESTATUS_CONCLUDED);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    }
    
    //@Test
    public void findByPlayerPhaseAndStatus_whenPlayerPhaseInexists_returnNull() {
    	PlayerPhase entity = service.findByPlayerPhaseAndStatus(PLAYER_ID_INEXISTENT, PHASE_ID_INEXISTENT, PHASESTATUS_CONCLUDED);
    	Assert.assertNull("failure - expected not null", entity);
    }
    /* findByPlayerPhaseAndStatus - end */
    
    /* findPlayerPhasesByPlayer - begin */
    //@Test
    public void findPlayerPhasesByPlayer_whenPlayerPhasesExist_returnList() {
    	List<PlayerPhase> list = service.findPlayerPhasesByPlayer(PLAYER_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    
    //@Test
    public void findPlayerPhasesByPlayer_whenPlayerPhasesInexist_returnNull() {
    	List<PlayerPhase> list = service.findPlayerPhasesByPlayer(PLAYER_ID_INEXISTENT);
    	Assert.assertNull("failure - expected null", list);
    }
    /* findPlayerPhasesByPlayer - end */
    
    /* findForReportGeneral - begin */
    //@Test
    public void findForReportGeneral_whenPlayerInexistent_returnEmptyList() {
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
}