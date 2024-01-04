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

import com.polifono.domain.Content;
import com.polifono.domain.Contenttype;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.repository.IContentRepository;
import com.polifono.service.impl.ContentServiceImpl;

/**
 * Unit test methods for the ContentService.
 */
@ExtendWith(MockitoExtension.class)
public class ContentServiceTest {

    @InjectMocks
    private ContentServiceImpl service;

    @Mock
    private IContentRepository repository;

    private final Integer CONTENT_ID_EXISTENT = 2;
    private final Integer CONTENT_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer PHASE_ID_EXISTENT = 1;
    //private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer CONTENTTYPE_TEST = 1;
    private final Integer CONTENTTYPE_TEXT = 2;

    private final Integer GAME_ID_EXISTENT = 1;
    //private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer LEVEL_ID_EXISTENT = 1;
    //private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;

    private final Integer MAP_ID_EXISTENT = 1;
    //private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    /* stubs - begin */
    private Optional<Content> getEntityStubData() {
        Phase phase = new Phase();
        phase.setId(PHASE_ID_EXISTENT);

        Contenttype contenttype = new Contenttype();
        contenttype.setId(CONTENTTYPE_TEXT);

        Content content = new Content();
        content.setId(CONTENT_ID_EXISTENT);
        content.setPhase(phase);
        content.setContenttype(contenttype);
        content.setContent("CONTENT");
        content.setOrder(1);

        return Optional.of(content);
    }

    private List<Content> getEntityListStubData() {
        List<Content> list = new ArrayList<Content>();

        Content entity1 = getEntityStubData().get();
        Content entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }

    private List<Content> getEntityListTypeTextStubData() {
        List<Content> list = new ArrayList<Content>();

        Contenttype contenttype = new Contenttype();
        contenttype.setId(CONTENTTYPE_TEXT);

        Phase phase = new Phase();
        phase.setId(PHASE_ID_EXISTENT);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        Level level = new Level();
        level.setId(LEVEL_ID_EXISTENT);

        map.setGame(game);
        map.setLevel(level);
        phase.setMap(map);

        Content entity1 = getEntityStubData().get();
        entity1.setContenttype(contenttype);
        entity1.setPhase(phase);

        Content entity2 = getEntityStubData().get();
        entity2.setContenttype(contenttype);
        entity2.setPhase(phase);

        list.add(entity1);
        list.add(entity2);

        return list;
    }

    private List<Content> getEntityListTypeTestStubData() {
        List<Content> list = new ArrayList<Content>();

        Contenttype contenttype = new Contenttype();
        contenttype.setId(CONTENTTYPE_TEST);

        Phase phase = new Phase();
        phase.setId(PHASE_ID_EXISTENT);

        Map map = new Map();
        map.setId(MAP_ID_EXISTENT);

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        Level level = new Level();
        level.setId(LEVEL_ID_EXISTENT);

        map.setGame(game);
        map.setLevel(level);
        phase.setMap(map);

        Content entity1 = getEntityStubData().get();
        entity1.setContenttype(contenttype);
        entity1.setPhase(phase);

        Content entity2 = getEntityStubData().get();
        entity2.setContenttype(contenttype);
        entity2.setPhase(phase);

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

    /* save - begin */
    @Test
    public void save_WhenSaveContent_ReturnContentSaved() {
        Optional<Content> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        Content entitySaved = service.save(entity.get());

        Assertions.assertNotNull(entitySaved, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entitySaved.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().getPhase().getId(), entitySaved.getPhase().getId(), "failure - expected phase attribute match");
        Assertions.assertEquals(entity.get().getContenttype().getId(), entitySaved.getContenttype().getId(), "failure - expected content type attribute match");
        Assertions.assertEquals(entity.get().getContent(), entitySaved.getContent(), "failure - expected content attribute match");
        Assertions.assertEquals(entity.get().getOrder(), entitySaved.getOrder(), "failure - expected order attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenContentIsExistent_ReturnTrue() {
        Optional<Content> entity = getEntityStubData();

        when(repository.findById(CONTENT_ID_EXISTENT)).thenReturn(entity);
        doNothing().when(repository).delete(entity.get());

        Assertions.assertTrue(service.delete(CONTENT_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(CONTENT_ID_EXISTENT);
        verify(repository, times(1)).delete(entity.get());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenContentIsInexistent_ReturnFalse() {
        when(repository.findById(CONTENT_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(CONTENT_ID_INEXISTENT), "failure - expected return false");

        verify(repository, times(1)).findById(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenContentIsExistent_ReturnContent() {
        Optional<Content> entity = getEntityStubData();

        when(repository.findById(CONTENT_ID_EXISTENT)).thenReturn(entity);

        Optional<Content> entityReturned = service.findById(CONTENT_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(CONTENT_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(CONTENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenContentIsInexistent_ReturnNull() {
        when(repository.findById(CONTENT_ID_INEXISTENT)).thenReturn(null);

        Optional<Content> entity = service.findById(CONTENT_ID_INEXISTENT);
        Assertions.assertNull(entity, "failure - expected null");

        verify(repository, times(1)).findById(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllContent_ReturnList() {
        List<Content> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Content> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findAllText - begin */
    @Test
    public void findAllText_WhenListAllContentOfTypeText_ReturnList() {
        List<Content> list = getEntityListTypeTextStubData();

        when(repository.findAllText()).thenReturn(list);

        List<Content> listReturned = service.findAllText();

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
        }

        verify(repository, times(1)).findAllText();
        verifyNoMoreInteractions(repository);
    }
    /* findAllText - end */

    /* findContentsTextByGame - begin */
    @Test
    public void findContentsTextByGame_WhenSearchByGameExistent_ReturnList() {
        List<Content> list = getEntityListTypeTextStubData();

        when(repository.findContentsTextByGame(GAME_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTextByGame(GAME_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId(), "expected game attribute match");
        }

        verify(repository, times(1)).findContentsTextByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByGame - end */

    /* findContentsTextByGameAndLevel - begin */
    @Test
    public void findContentsTextByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
        List<Content> list = getEntityListTypeTextStubData();

        when(repository.findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId(), "expected game attribute match");
            Assertions.assertEquals(LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId(), "expected level attribute match");
        }

        verify(repository, times(1)).findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByGameAndLevel - end */

    /* findContentsTextByMap - begin */
    @Test
    public void findContentsTextByMap_WhenSearchByMapExistent_ReturnList() {
        List<Content> list = getEntityListTypeTextStubData();

        when(repository.findContentsTextByMap(MAP_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTextByMap(MAP_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId(), "expected map attribute match");
        }

        verify(repository, times(1)).findContentsTextByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByMap - end */

    /* findContentsTextByPhase - begin */
    @Test
    public void findContentsTextByPhase_WhenSearchByPhaseExistent_ReturnList() {
        List<Content> list = getEntityListTypeTextStubData();

        when(repository.findContentsTextByPhase(PHASE_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTextByPhase(PHASE_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId(), "expected phase attribute match");
        }

        verify(repository, times(1)).findContentsTextByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByPhase - end */

    /* findAllTest - begin */
    @Test
    public void findAllTest_WhenListAllContentOfTypeTest_ReturnList() {
        List<Content> list = getEntityListTypeTestStubData();

        when(repository.findAllTest()).thenReturn(list);

        List<Content> listReturned = service.findAllTest();

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
        }

        verify(repository, times(1)).findAllTest();
        verifyNoMoreInteractions(repository);
    }
    /* findAllTest - end */

    /* findContentsTestByGame - begin */
    @Test
    public void findContentsTestByGame_WhenSearchByGameExistent_ReturnList() {
        List<Content> list = getEntityListTypeTestStubData();

        when(repository.findContentsTestByGame(GAME_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTestByGame(GAME_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId(), "expected game attribute match");
        }

        verify(repository, times(1)).findContentsTestByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByGame - end */

    /* findContentsTestByGameAndLevel - begin */
    @Test
    public void findContentsTestByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
        List<Content> list = getEntityListTypeTestStubData();

        when(repository.findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId(), "expected game attribute match");
            Assertions.assertEquals(LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId(), "expected level attribute match");
        }

        verify(repository, times(1)).findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByGameAndLevel - end */

    /* findContentsTestByMap - begin */
    @Test
    public void findContentsTestByMap_WhenSearchByMapExistent_ReturnList() {
        List<Content> list = getEntityListTypeTestStubData();

        when(repository.findContentsTestByMap(MAP_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTestByMap(MAP_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId(), "expected map attribute match");
        }

        verify(repository, times(1)).findContentsTestByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByMap - end */

    /* findContentsTestByPhase - begin */
    @Test
    public void findContentsTestByPhase_WhenSearchByPhaseExistent_ReturnList() {
        List<Content> list = getEntityListTypeTestStubData();

        when(repository.findContentsTestByPhase(PHASE_ID_EXISTENT)).thenReturn(list);

        List<Content> listReturned = service.findContentsTestByPhase(PHASE_ID_EXISTENT);

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        for (Content entity : listReturned) {
            Assertions.assertEquals(CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId(), "expected content type attribute match");
            Assertions.assertEquals(PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId(), "expected phase attribute match");
        }

        verify(repository, times(1)).findContentsTestByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByPhase - end */

    /* findByPhaseAndOrder - begin */
    @Test
    public void findByPhaseAndOrder_WhenSearchByPhaseAndOrderExistents_ReturnList() {
        Optional<Content> entity = getEntityStubData();

        when(repository.findByPhaseAndOrder(PHASE_ID_EXISTENT, 1)).thenReturn(entity.get());

        Content entityReturned = service.findByPhaseAndOrder(PHASE_ID_EXISTENT, 1);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PHASE_ID_EXISTENT.intValue(), entityReturned.getPhase().getId(), "expected phase type attribute match");
        Assertions.assertEquals(1, entityReturned.getOrder(), "expected order attribute match");

        verify(repository, times(1)).findByPhaseAndOrder(PHASE_ID_EXISTENT, 1);
        verifyNoMoreInteractions(repository);
    }
    /* findByPhaseAndOrder - end */
}
