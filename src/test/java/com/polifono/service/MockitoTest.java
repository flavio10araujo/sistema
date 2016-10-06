package com.polifono.service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.polifono.Application;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.repository.IMapRepository;
import com.polifono.service.impl.MapServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
//@Transactional
public class MockitoTest /*extends AbstractTest*/ {

	//@Autowired
	private IMapService service;
	
	@Mock
	private IMapRepository repository;
	
	@Mock
	private IPhaseService phaseService;
	
	@Mock
	private List<String> mockList;
	
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
		service = new MapServiceImpl(repository, phaseService);
    }

	/*@Test
	public void test() {
		String expected = "Hello, World!";
		when(mockList.get(0)).thenReturn(expected);
		String actual = mockList.get(0);
		Assert.assertEquals(expected, actual);
	}*/
	
	@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapDifferentOfTheFirstMapOfTheFirstLevelAndThePlayerHasNeverFinishedAPhaseOfThisGame_returnFalse() {
		Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(2);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
		
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(null);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
	
	@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapInAPreviousLevelThanTheLastPhaseDonesLevel_returnTrue() {
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(2);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setOrder(3);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }
}