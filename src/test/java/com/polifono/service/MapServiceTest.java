package com.polifono.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.repository.IMapRepository;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {

    @InjectMocks
    private MapService service;

    @Mock
    private IMapRepository repository;

    @Mock
    private PhaseService phaseService;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer MAP_ID_EXISTENT = 1;
    private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer LEVEL_ID_EXISTENT = 3;
    private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;

    /* save - begin */
    @Test
    public void save_WhenSaveMap_ReturnMapSaved() {
        Map entity = getEntityStubData();

        when(repository.save(entity)).thenReturn(entity);

        // Saving the changes.
        Map entityReturned = service.save(entity);

        Assertions.assertEquals(entity.getName(), entityReturned.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.getOrder(), entityReturned.getOrder(), "failure - expected order attribute match");
        Assertions.assertEquals(entity.getGame().getId(), entityReturned.getGame().getId(), "failure - expected game attribute match");
        Assertions.assertEquals(entity.getLevel().getId(), entityReturned.getLevel().getId(), "failure - expected level attribute match");

        verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenMapIsExistent_ReturnTrue() {
        Map entity = getEntityStubData();

        when(repository.findById(MAP_ID_EXISTENT)).thenReturn(Optional.of(entity));
        doNothing().when(repository).delete(entity);

        Assertions.assertTrue(service.delete(MAP_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(MAP_ID_EXISTENT);
        verify(repository, times(1)).delete(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenMapIsInexistent_ReturnFalse() {
        when(repository.findById(MAP_ID_EXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(MAP_ID_EXISTENT), "failure - expected return false");

        verify(repository, times(1)).findById(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenMapIsExistent_ReturnMap() {
        Map entity = getEntityStubData();

        when(repository.findById(MAP_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Optional<Map> entityReturned = service.findById(MAP_ID_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(MAP_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenMapIsInexistent_ReturnNull() {
        when(repository.findById(MAP_ID_INEXISTENT)).thenReturn(null);

        Optional<Map> entity = service.findById(MAP_ID_INEXISTENT);
        Assertions.assertNull(entity, "failure - expected null");

        verify(repository, times(1)).findById(MAP_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllMaps_ReturnList() {
        List<Map> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Map> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findMapsByGame - begin */
    @Test
    public void findMapsByGame_WhenSearchByGameExistent_ReturnList() {
        List<Map> list = getEntityListStubData();

        when(repository.findMapsByGame(GAME_ID_EXISTENT)).thenReturn(list);

        List<Map> listReturned = service.findMapsByGame(GAME_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findMapsByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findMapsByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
        when(repository.findMapsByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Map> listReturned = service.findMapsByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findMapsByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findMapsByGame - end */

    /* findMapsByGameAndLevel - begin */
    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
        List<Map> list = getEntityListStubData();

        when(repository.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);

        List<Map> listReturned = service.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
        when(repository.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Map> listReturned = service.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
        when(repository.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Map> listReturned = service.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findMapsByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
        when(repository.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<>());

        List<Map> listReturned = service.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findMapsByGameAndLevel - end */

    /* findByGameAndLevel - begin */
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnMap() {
        int gameId = 1, levelId = 1;
        List<Map> listReturned = new ArrayList<>();
        listReturned.add(new Map());

        when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        Optional<Map> entity = service.findByGameAndLevel(gameId, levelId);
        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnOptionalEmpty() {
        int gameId = 1, levelId = 1;
        List<Map> listReturned = new ArrayList<>();

        when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        Optional<Map> entity = service.findByGameAndLevel(gameId, levelId);
        Assertions.assertFalse(entity.isPresent(), "failure - expected Optional to be empty");
    }
    /* findByGameAndLevel - end */

    /* findByGameLevelAndOrder - begin */
    @Test
    public void findByGameLevelAndOrder_WhenSearchByGameLevelAndOrderExistents_ReturnMap() {
        int gameId = 1, levelId = 1, mapOrder = 1;

        List<Map> listReturned = new ArrayList<>();
        listReturned.add(new Map());
        when(repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder)).thenReturn(listReturned);

        Optional<Map> entity = service.findByGameLevelAndOrder(gameId, levelId, mapOrder);
        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
    }

    @Test
    public void findByGameLevelAndOrder_WhenSearchByGameLevelAndOrderInexistents_ReturnOptionalEmpty() {
        int gameId = 999, levelId = 999, mapOrder = 999;

        List<Map> listReturned = new ArrayList<>();

        when(repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder)).thenReturn(listReturned);

        Optional<Map> entity = service.findByGameLevelAndOrder(gameId, levelId, mapOrder);
        Assertions.assertFalse(entity.isPresent(), "failure - expected Optional to be empty");
    }
    /* findByGameLevelAndOrder - end */

    /* findNextMapSameLevel - begin */
    @Test
    public void findNextMapSameLevel_WhenNextMapExists_ReturnNextMap() {
        Map map = new Map();
        Game game = new Game();
        game.setId(1);
        map.setGame(game);
        Level level = new Level();
        level.setId(1);
        map.setLevel(level);

        when(repository.findById(MAP_ID_EXISTENT)).thenReturn(Optional.of(map));

        Optional<Map> mapCurrent = service.findById(MAP_ID_EXISTENT);

        Map nextMapSameLevel = new Map();
        nextMapSameLevel.setGame(mapCurrent.get().getGame());
        nextMapSameLevel.setLevel(mapCurrent.get().getLevel());
        nextMapSameLevel.setName("Next Map Same Level");
        nextMapSameLevel.setOrder(mapCurrent.get().getOrder() + 1);
        nextMapSameLevel.setId(50);

        when(repository.save(nextMapSameLevel)).thenReturn(nextMapSameLevel);
        nextMapSameLevel = service.save(nextMapSameLevel);

        when(repository.findNextMapSameLevel(mapCurrent.get().getGame().getId(), mapCurrent.get().getLevel().getId(), mapCurrent.get().getOrder())).thenReturn(
                Optional.ofNullable(nextMapSameLevel));

        Optional<Map> entity = service.findNextMapSameLevel(mapCurrent.get());

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(nextMapSameLevel.getId(), entity.get().getId());
    }
    /* findNextMapSameLevel - end */

    /* canPlayerAccessMap - begin */
    @Test
    public void givenPlayerIsTryingToAccessTheFirstMapOfTheFirstLevel_whenCanPlayerAccessMap_thenReturnTrue() {
        Level level = new Level();
        level.setOrder(1);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setLevel(level);

        Player player = new Player();

        Assertions.assertTrue(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAMapDifferentOfTheFirstMapOfTheFirstLevelAndThePlayerHasNeverFinishedAPhaseOfThisGame_whenCanPlayerAccessMap_thenReturnFalse() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(2);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.empty());

        Assertions.assertFalse(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAMapInAPreviousLevelThanTheLastPhaseDoneLevel_whenCanPlayerAccessMap_thenReturnTrue() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(2);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setOrder(3);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setLevel(levelLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Assertions.assertTrue(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAPreviousMapAtTheSameLevelOfTheLastPhaseDone_whenCanPlayerAccessMap_thenReturnTrue() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(3);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setOrder(3);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(2);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Assertions.assertTrue(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessANextMapButTheLastPhaseDoneIsNotTheLastPhaseOfTheLevel_whenCanPlayerAccessMap_thenReturnFalse() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(3);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(2);
        levelLastPhaseDone.setOrder(2);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(59);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertFalse(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelAndItIsTheNext_whenCanPlayerAccessMap_thenReturnTrue() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(3);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(2);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(3);
        levelLastPhaseDone.setOrder(3);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(60);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertTrue(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelButItIsNotTheNext_whenCanPlayerAccessMap_thenReturnFalse() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(3);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(3);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(3);
        levelLastPhaseDone.setOrder(3);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(60);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertFalse(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsTheFirstOfTheNextLevel_whenCanPlayerAccessMap_thenReturnTrue() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(4);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(3);
        levelLastPhaseDone.setOrder(3);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(60);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertTrue(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapINotTheFirstOfTheNextLevel_whenCanPlayerAccessMap_thenReturnFalse() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(4);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(2);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(3);
        levelLastPhaseDone.setOrder(3);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(60);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertFalse(service.canPlayerAccessMap(map, player.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessMapButTheLevelOfThisMapIsNotTheNextLevel_whenCanPlayerAccessMap_thenReturnFalse() {
        Game game = new Game();
        game.setId(1);

        Level level = new Level();
        level.setOrder(5);

        Map map = new Map(); // Map that the player is trying to access.
        map.setOrder(1);
        map.setGame(game);
        map.setLevel(level);

        Player player = new Player();
        player.setId(1);

        Level levelLastPhaseDone = new Level();
        levelLastPhaseDone.setId(3);
        levelLastPhaseDone.setOrder(3);

        Game gameLastPhaseDone = new Game();
        gameLastPhaseDone.setId(1);

        Map mapLastPhaseDone = new Map();
        mapLastPhaseDone.setOrder(1);
        mapLastPhaseDone.setLevel(levelLastPhaseDone);
        mapLastPhaseDone.setGame(gameLastPhaseDone);

        Phase lastPhaseDone = new Phase();
        lastPhaseDone.setId(60);
        lastPhaseDone.setMap(mapLastPhaseDone);

        when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(Optional.of(lastPhaseDone));

        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(60);

        when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(
                Optional.of(lastPhaseOfTheLevel));

        Assertions.assertFalse(service.canPlayerAccessMap(map, player.getId()));
    }
    /* canPlayerAccessMap - end */

    /* findCurrentMap - begin */
    @Test
    public void givenPlayerHasNeverCompletedAnyPhaseOfTheGame_whenFindCurrentMap_thenReturnFirstMapOfTheFirstLevelOfTheGame() {
        int gameId = 1, levelId = 1;

        List<Map> listReturned = new ArrayList<Map>();
        Map map = new Map();
        map.setOrder(1);
        Game gameMapReturned = new Game();
        gameMapReturned.setId(GAME_ID_EXISTENT);
        map.setGame(gameMapReturned);
        Level levelMapReturned = new Level();
        levelMapReturned.setOrder(1);
        map.setLevel(levelMapReturned);
        listReturned.add(map);

        when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = null;
        Optional<Map> entity = service.findCurrentMap(game, lastPhaseCompleted);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(1, entity.get().getOrder(), "failure - expected map order attribute match");
        Assertions.assertEquals(1, entity.get().getLevel().getOrder(), "failure - expected level order attribute match");
        Assertions.assertEquals(game.getId(), entity.get().getGame().getId(), "failure - expected game id attribute match");
    }

    @Test
    public void givenNextPhaseIsInTheSameMapThatTheLastPhaseCompleted_whenFindCurrentMap_thenReturnSameMapOfTheLastPhaseCompleted() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();
        Map map = new Map();
        map.setId(3); // Map ID of the last phase completed.
        Phase phase = new Phase();
        phase.setOrder(10); // Last phase completed.
        phase.setMap(map);
        lastPhaseCompleted.setPhase(phase);

        Phase nextPhase = new Phase(); // Mocking the next phase.
        nextPhase.setOrder(11);

        when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(
                Optional.of(nextPhase));

        Optional<Map> entity = service.findCurrentMap(game, lastPhaseCompleted);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(lastPhaseCompleted.getPhase().getMap().getId(), entity.get().getId(), "failure - expected map id attribute match");
    }

    @Test
    public void givenNextPhaseIsInTheNextMapInTheSameLevel_whenFindCurrentMap_thenReturnMapWithNextOrderAndInTheSameLevel() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();
        Level level = new Level();
        level.setOrder(1);
        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);
        map.setOrder(1);
        map.setLevel(level);
        map.setGame(game);
        Phase phase = new Phase();
        phase.setMap(map);
        lastPhaseCompleted.setPhase(phase);

        Map nextMapSameLevel = new Map();
        nextMapSameLevel.setLevel(level);
        nextMapSameLevel.setOrder(2);

        Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

        when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(
                Optional.empty());
        when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(
                Optional.of(nextMapSameLevel));

        Optional<Map> entity = service.findCurrentMap(game, lastPhaseCompleted);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder() + 1, entity.get().getOrder());
        Assertions.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder(), entity.get().getLevel().getOrder());
    }

    @Test
    public void givenNextPhaseIsInTheNextLevel_whenFindCurrentMap_thenReturnFirstMapOfNextLevelWithFlagLevelCompletedChecked() {
        int gameId = 1, levelId = 1;

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();
        Level level = new Level();
        level.setOrder(1);
        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);
        map.setOrder(1);
        map.setLevel(level);
        map.setGame(game);
        Phase phase = new Phase();
        phase.setMap(map);
        lastPhaseCompleted.setPhase(phase);

        Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

        when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(
                Optional.empty());
        when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(Optional.empty());

        List<Map> listReturned = new ArrayList<>();
        Map mapReturned = new Map();
        mapReturned.setOrder(1);
        mapReturned.setLevelCompleted(true);
        Level levelReturned = new Level();
        levelReturned.setOrder(2);
        mapReturned.setLevel(levelReturned);

        listReturned.add(mapReturned);

        when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        Optional<Map> entity = service.findCurrentMap(game, lastPhaseCompleted);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(1, entity.get().getOrder());
        Assertions.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder() + 1, entity.get().getLevel().getOrder());
        Assertions.assertTrue(entity.get().isLevelCompleted());
    }

    @Test
    public void givenItDoesntExistNextMap_whenFindCurrentMap_thenReturnSameMapOfTheLastPhaseCompletedWithFlagGameCompletedChecked() {
        int gameId = 1, levelId = 1;

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();
        Level level = new Level();
        level.setOrder(1);
        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);
        map.setOrder(1);
        map.setLevel(level);
        map.setGame(game);
        Phase phase = new Phase();
        phase.setMap(map);
        lastPhaseCompleted.setPhase(phase);

        Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

        when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(
                Optional.empty());
        when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(Optional.empty());

        List<Map> listReturned = new ArrayList<>();

        when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        Optional<Map> entity = service.findCurrentMap(game, lastPhaseCompleted);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder(), entity.get().getOrder());
        Assertions.assertTrue(entity.get().isGameCompleted());
    }
    /* findCurrentMap - end */

    /* stubs - begin */
    private Map getEntityStubData() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        Level level = new Level();
        level.setId(LEVEL_ID_EXISTENT);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);
        map.setName("Map Test");
        map.setOrder(3);
        map.setGame(game);
        map.setLevel(level);

        return map;
    }

    private List<Map> getEntityListStubData() {
        List<Map> list = new ArrayList<>();

        Map entity1 = getEntityStubData();
        Map entity2 = getEntityStubData();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
