package com.polifono.service.impl;

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
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.repository.IPhaseRepository;

/**
 * Unit test methods for the PhaseService.
 */
@ExtendWith(MockitoExtension.class)
public class PhaseServiceTest {

    @InjectMocks
    private PhaseService service;

    @Mock
    private IPhaseRepository repository;

    private final Integer PHASE_ID_EXISTENT = 1;
    private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer LEVEL_ID_EXISTENT = 1;
    private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer MAP_ID_EXISTENT = 1;
    private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer ORDER_EXISTENT = 1;
    private final Integer ORDER_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PLAYER_ID_EXISTENT = 1;

    /* save - begin */
    @Test
    public void save_WhenSavePhase_ReturnPhaseSaved() {
        Integer id = Integer.valueOf(PHASE_ID_EXISTENT);

        Phase phaseSaved = new Phase();
        Map mapPhaseSaved = new Map();
        mapPhaseSaved.setId(MAP_ID_EXISTENT);
        phaseSaved.setMap(mapPhaseSaved);
        phaseSaved.setName("phase test");
        phaseSaved.setOrder(20);
        phaseSaved.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(phaseSaved));

        Optional<Phase> entity = service.findById(id);

        // Changing all possible fields.
        // id - not possible to change.
        Map newMap = new Map();
        newMap.setId(entity.get().getMap().getId() + 1);
        entity.get().setMap(newMap);
        entity.get().setName(entity.get().getName() + " Changed");
        entity.get().setOrder(entity.get().getOrder() + 1);

        when(repository.save(entity.get())).thenReturn(entity.get());

        // Saving the changes.
        Phase changedEntity = service.save(entity.get());

        Assertions.assertNotNull(changedEntity, "failure - expected not null");
        Assertions.assertEquals(id.intValue(), changedEntity.getId(), "failure - expected id attribute match");

        when(repository.findById(changedEntity.getId())).thenReturn(Optional.of(changedEntity));

        // Get the entity in the database with the changes.
        Optional<Phase> updatedEntity = service.findById(changedEntity.getId());

        Assertions.assertEquals(entity.get().getName(), updatedEntity.get().getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.get().getOrder(), updatedEntity.get().getOrder(), "failure - expected order attribute match");
        Assertions.assertEquals(entity.get().getMap().getId(), updatedEntity.get().getMap().getId(), "failure - expected game attribute match");
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenPhaseIsExistent_ReturnTrue() {
        Phase temp = new Phase();

        when(repository.findById(PHASE_ID_EXISTENT)).thenReturn(Optional.of(temp));

        Assertions.assertTrue(service.delete(PHASE_ID_EXISTENT), "failure - expected return true");

        when(repository.findById(PHASE_ID_EXISTENT)).thenReturn(null);

        Optional<Phase> entity = service.findById(PHASE_ID_EXISTENT);
        Assertions.assertNull(entity, "failure - expected null");
    }

    @Test
    public void delete_WhenPhaseIsInexistent_ReturnFalse() {
        when(repository.findById(PHASE_ID_INEXISTENT)).thenReturn(Optional.empty());
        Assertions.assertFalse(service.delete(PHASE_ID_INEXISTENT), "failure - expected return false");
    }
    /* delete - end */

    /* findById - begin */
    @Test
    public void findOne_WhenPhaseIsExistent_ReturnPhase() {
        Integer id = Integer.valueOf(PHASE_ID_EXISTENT);

        Phase phase = new Phase();
        phase.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(phase));

        Optional<Phase> entity = service.findById(id);
        Assertions.assertNotNull(entity, "failure - expected not null");
        Assertions.assertEquals(id.intValue(), entity.get().getId(), "failure - expected id attribute match");
    }

    @Test
    public void findOne_WhenPhaseIsInexistent_ReturnNull() {
        Integer id = PHASE_ID_INEXISTENT;

        when(repository.findById(id)).thenReturn(null);

        Optional<Phase> entity = service.findById(id);
        Assertions.assertNull(entity, "failure - expected null");
    }
    /* findById - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllPhases_ReturnList() {
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(new Phase());
        when(repository.findAll()).thenReturn(listReturned);

        List<Phase> list = service.findAll();
        Assertions.assertNotNull(list, "failure - expected not null");
        Assertions.assertNotEquals(0, list.size(), "failure - not expected list size 0");
    }
    /* findAll - end */

    /* findByGame - begin */
    @Test
    public void findPhasesByGame_WhenSearchByGameExistent_ReturnList() {
        int gameId = GAME_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(new Phase());
        when(repository.findByGame(gameId)).thenReturn(listReturned);

        List<Phase> list = service.findByGame(GAME_ID_EXISTENT);
        Assertions.assertNotNull(list, "failure - not expected null");
        Assertions.assertNotEquals(0, list.size(), "failure - list size not expected 0");
    }

    @Test
    public void findPhasesByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
        int gameId = GAME_ID_INEXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        when(repository.findByGame(gameId)).thenReturn(listReturned);

        List<Phase> list = service.findByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");
    }
    /* findByGame - end */

    /* findByGameAndLevel - begin */
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
        int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(new Phase());
        when(repository.findByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        List<Phase> list = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertNotNull(list, "failure - not expected null");
        Assertions.assertNotEquals(0, list.size(), "failure - list size not expected 0");
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
        int gameId = GAME_ID_INEXISTENT, levelId = LEVEL_ID_INEXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        when(repository.findByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        List<Phase> list = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
        int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_INEXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        when(repository.findByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        List<Phase> list = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");
    }

    @Test
    public void findByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
        int gameId = GAME_ID_INEXISTENT, levelId = LEVEL_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        when(repository.findByGameAndLevel(gameId, levelId)).thenReturn(listReturned);

        List<Phase> list = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");
    }
    /* findByGameAndLevel - end */

    /* findByMap - begin */
    @Test
    public void findByMap_WhenSearchByMapExistent_ReturnList() {
        int mapId = MAP_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(new Phase());
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        List<Phase> list = service.findByMap(MAP_ID_EXISTENT);
        Assertions.assertNotNull(list, "failure - not expected null");
        Assertions.assertNotEquals(0, list.size(), "failure - list size not expected 0");
    }

    @Test
    public void findByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
        int mapId = MAP_ID_INEXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        List<Phase> list = service.findByMap(MAP_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");
    }
    /* findByMap - end */

    /* findByMapAndOrder - begin */
    @Test
    public void findByMapAndOrder_WhenSearchByMapAndOrderExistents_ReturnList() {
        int mapId = MAP_ID_EXISTENT, phaseOrder = ORDER_EXISTENT;
        Phase entityReturned = new Phase();
        when(repository.findByMapAndOrder(mapId, phaseOrder)).thenReturn(Optional.of(entityReturned));

        Optional<Phase> entity = service.findByMapAndOrder(MAP_ID_EXISTENT, ORDER_EXISTENT);
        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
    }

    @Test
    public void findByMapAndOrder_WhenSearchByMapAndOrderInexistents_ReturnEmptyList() {
        int mapId = MAP_ID_INEXISTENT, phaseOrder = ORDER_INEXISTENT;
        when(repository.findByMapAndOrder(mapId, phaseOrder)).thenReturn(Optional.empty());

        Optional<Phase> entity = service.findByMapAndOrder(MAP_ID_INEXISTENT, ORDER_INEXISTENT);
        Assertions.assertFalse(entity.isPresent(), "failure - expected empty");
    }
    /* findByMapAndOrder - end */

    /* findNextPhaseInThisMap - begin */
    @Test
    public void findNextPhaseInThisMap_WhenNextPhaseInThisMapIsExistent_ReturnNextPhase() {
        int mapId = MAP_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        item.setOrder(10);
        Map map = new Map();
        map.setId(3);
        item.setMap(map);
        listReturned.add(item);
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        List<Phase> list = service.findByMap(MAP_ID_EXISTENT);
        Phase firstPhase = list.get(0);

        int phaseOrder = (firstPhase.getOrder() + 1);
        Phase entityReturned = new Phase();
        entityReturned.setOrder(phaseOrder);

        when(repository.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1)).thenReturn(Optional.of(entityReturned));

        Optional<Phase> entity = service.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1);

        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertEquals(firstPhase.getOrder() + 1, entity.get().getOrder());
    }

    @Test
    public void findNextPhaseInThisMap_WhenNextPhaseInThisMapIsInexistent_ReturnNull() {
        int mapId = MAP_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        item.setOrder(10);
        Map map = new Map();
        map.setId(3);
        item.setMap(map);
        listReturned.add(item);
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        List<Phase> list = service.findByMap(MAP_ID_EXISTENT);
        Phase firstPhase = list.get(list.size() - 1);

        when(repository.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1)).thenReturn(Optional.empty());

        Optional<Phase> entity = service.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1);

        Assertions.assertFalse(entity.isPresent(), "failure - expected null");
    }
    /* findNextPhaseInThisMap - end */

    /* findLastPhaseDoneByPlayerAndGame - begin */
    @Test
    public void findLastPhaseDoneByPlayerAndGame_WhenPlayerHasAlreadyFinishedAtLeastOnePhase_ReturnLastPhaseDone() {
        int playerId = PLAYER_ID_EXISTENT, gameId = GAME_ID_EXISTENT;

        List<Phase> listReturned = new ArrayList<>();
        Phase phase = new Phase();
        phase.setId(123);
        listReturned.add(phase);
        when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);

        Optional<Phase> entity = service.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT);
        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertNotEquals(0, entity.get().getId(), "failure - expected id attribute bigger than 0");
    }
    /* findLastPhaseDoneByPlayerAndGame - end */

    /* findLastPhaseOfTheLevel - begin */
    @Test
    public void findLastPhaseOfTheLevel_WhenEveryThingIsOK_ReturnLastPhaseOfTheLevel() {
        int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_EXISTENT;
        Phase itemReturned = new Phase();
        itemReturned.setId(123);
        itemReturned.setOrder(10);
        Map mapItemReturned = new Map();
        mapItemReturned.setId(2);
        itemReturned.setMap(mapItemReturned);
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(itemReturned);
        when(repository.findLastPhaseOfTheLevel(gameId, levelId)).thenReturn(listReturned);

        Optional<Phase> entity = service.findLastPhaseOfTheLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertTrue(entity.isPresent(), "failure - expected not null");
        Assertions.assertNotEquals(0, entity.get().getId(), "failure - expected id attribute bigger than 0");

        // The only way to be sure that entity is the last phase of the level is if when we use findNextPhaseInThisMap the return is null.
        int mapId = entity.get().getMap().getId(), phaseOrder = (entity.get().getOrder() + 1);
        when(repository.findNextPhaseInThisMap(mapId, phaseOrder)).thenReturn(Optional.empty());

        Optional<Phase> entityNull = service.findNextPhaseInThisMap(entity.get().getMap().getId(), entity.get().getOrder() + 1);
        Assertions.assertFalse(entityNull.isPresent(), "failure - expected null");
    }
    /* findLastPhaseOfTheLevel - end */

    /* findGamesForProfile - begin */
    @Test
    public void givenPlayerExists_whenFindGamesForProfile_thenReturnList() {
        int playerId = PLAYER_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        Game game = new Game();
        game.setId(4);
        Map map = new Map();
        map.setGame(game);
        item.setMap(map);
        listReturned.add(item);
        when(repository.findGamesForProfile(playerId)).thenReturn(listReturned);

        List<Phase> list = service.findGamesForProfile(PLAYER_ID_EXISTENT);
        Assertions.assertNotNull(list, "failure - expected not null");
    }
    /* findGamesForProfile - end */

    /* findPhasesCheckedByMap - begin */
    @Test
    public void givenThereAreNotPhasesInTheMap_whenFindPhasesCheckedByMap_thenReturnNull() {
        int mapId = MAP_ID_EXISTENT;
        when(repository.findByMap(mapId)).thenReturn(null);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();

        List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);
        Assertions.assertNull(list);
    }

    @Test
    public void givenPlayerHasNeverCompletedAnyPhaseOfThisGame_whenFindPhasesCheckedByMap_thenReturnListOfPhasesWithTheFirstPhaseOpened() {
        int mapId = MAP_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        listReturned.add(new Phase());
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = null;

        List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);
        Phase firstPhase = list.get(0);
        Assertions.assertTrue(firstPhase.isOpened());
    }

    @Test
    public void givenPlayerHasAlreadyCompletedAtLeastOnePhaseOfTheGame_whenFindPhasesCheckedByMap_thenReturnListOfPhaseWithAllPhasesOpenedUntilTheNextPhase() {
        int mapId = MAP_ID_EXISTENT;
        List<Phase> listReturned = new ArrayList<>();
        Phase p1 = new Phase();
        p1.setOrder(1);
        Phase p2 = new Phase();
        p2.setOrder(2);
        Phase p3 = new Phase();
        p3.setOrder(3);
        Phase p4 = new Phase();
        p4.setOrder(4);
        listReturned.add(p1);
        listReturned.add(p2);
        listReturned.add(p3);
        listReturned.add(p4);
        when(repository.findByMap(mapId)).thenReturn(listReturned);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);

        PlayerPhase lastPhaseCompleted = new PlayerPhase();
        Phase phaseCompleted = new Phase();
        phaseCompleted.setOrder(2);
        lastPhaseCompleted.setPhase(phaseCompleted);

        List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);

        Assertions.assertTrue(list.get(0).isOpened());
        Assertions.assertTrue(list.get(1).isOpened());
        Assertions.assertTrue(list.get(2).isOpened());
        Assertions.assertFalse(list.get(3).isOpened());
    }
    /* findPhasesCheckedByMap - end */

    /* canPlayerAccessPhase - begin */
    @Test
    public void givenAccessingFirstPhase_whenCanPlayerAccessPhase_thenReturnTrue() {
        Phase phase = new Phase();
        phase.setOrder(1);

        Player user = new Player();

        Assertions.assertTrue(service.canPlayerAccessPhase(phase, user.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAPhaseButHeHadNeverFinishedAPhaseOfThisGame_whenCanPlayerAccessPhase_thenReturnFalse() {
        int playerId = 1, gameId = 1;

        Phase phase = new Phase();
        phase.setOrder(20);
        Game game = new Game();
        game.setId(gameId);
        Map map = new Map();
        map.setGame(game);
        phase.setMap(map);

        Player user = new Player();
        user.setId(playerId);

        when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(new ArrayList<>());

        Assertions.assertFalse(service.canPlayerAccessPhase(phase, user.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAPhaseThatHeHadAlreadyDone_whenCanPlayerAccessPhase_thenReturnTrue() {
        int playerId = 1, gameId = 1;

        Phase phase = new Phase();
        phase.setOrder(20);
        Game game = new Game();
        game.setId(gameId);
        Map map = new Map();
        map.setGame(game);
        phase.setMap(map);

        Player user = new Player();
        user.setId(playerId);

        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        item.setOrder(25); // Last phase done.
        listReturned.add(item);

        when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);

        Assertions.assertTrue(service.canPlayerAccessPhase(phase, user.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessTheNextPhaseInTheRightSequence_whenCanPlayerAccessPhase_thenReturnTrue() {
        int playerId = 1, gameId = 1;

        Phase phase = new Phase();
        phase.setOrder(20);
        Game game = new Game();
        game.setId(gameId);
        Map map = new Map();
        map.setGame(game);
        phase.setMap(map);

        Player user = new Player();
        user.setId(playerId);

        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        item.setOrder(19); // Last phase done.
        listReturned.add(item);

        when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);

        Assertions.assertTrue(service.canPlayerAccessPhase(phase, user.getId()));
    }

    @Test
    public void givenPlayerIsTryingToAccessAFuturePhaseButItIsNotTheNextPhase_whenCanPlayerAccessPhase_thenReturnFalse() {
        int playerId = 1, gameId = 1;

        Phase phase = new Phase();
        phase.setOrder(20);
        Game game = new Game();
        game.setId(gameId);
        Map map = new Map();
        map.setGame(game);
        phase.setMap(map);

        Player user = new Player();
        user.setId(playerId);

        List<Phase> listReturned = new ArrayList<>();
        Phase item = new Phase();
        item.setOrder(15); // Last phase done.
        listReturned.add(item);

        when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);

        Assertions.assertFalse(service.canPlayerAccessPhase(phase, user.getId()));
    }
    /* canPlayerAccessPhase - end */
}
