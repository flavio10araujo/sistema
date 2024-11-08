package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.TemplateConstants.URL_GAMES_LEVEL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Player;
import com.polifono.domain.enums.Role;
import com.polifono.dto.RankingDTO;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IQuestionService;
import com.polifono.service.helper.GameHelperService;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SecurityService;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    private static final String GAME_NAME_LINK_INEXISTENT = "game_inexistent";
    private static final String GAME_NAME_LINK_EXISTENT = "game_existent";

    @InjectMocks
    private GameController gameController;

    @Mock
    private ConfigsCreditsProperties configsCreditsProperties;

    @Mock
    private MessageSource messagesResource;

    @Mock
    private SecurityService securityService;

    @Mock
    private IPlayerService playerService;

    @Mock
    private IGameService gameService;

    @Mock
    private ILevelService levelService;

    @Mock
    private IMapService mapService;

    @Mock
    private IPhaseService phaseService;

    @Mock
    private IContentService contentService;

    @Mock
    private IQuestionService questionService;

    @Mock
    private IPlayerPhaseService playerPhaseService;

    @Mock
    private IDiplomaService diplomaService;

    @Mock
    private GenerateRandomStringService generateRandomStringService;

    @Mock
    private GameHelperService gameHelperService;

    /* listGames - BEGIN */
    @Test
    public void whenListGames_thenReturnGamesIndexPage() {
        Model model = mock(Model.class);
        List<RankingDTO> rankingMonthly = new ArrayList<>();

        when(playerPhaseService.getRankingMonthly()).thenReturn(rankingMonthly);

        String result = gameController.listGames(model);

        verify(model).addAttribute("ranking_monthly", rankingMonthly);
        Assertions.assertEquals(URL_GAMES_INDEX, result);
    }
    /* listGames - END */

    /* listLevelsOfTheGame - BEGIN */
    @Test
    public void givenGameDoesNotExist_WhenListLevelsOfTheGame_thenRedirectToHomePage() {
        when(gameService.findByNamelink(GAME_NAME_LINK_INEXISTENT)).thenReturn(Optional.empty());

        String result = gameController.listLevelsOfTheGame(mock(Model.class), GAME_NAME_LINK_INEXISTENT);

        Assertions.assertEquals(REDIRECT_HOME, result);
    }

    @Test
    public void givenGameExist_whenListLevelsOfTheGame_thenOpenGameLevelPage() {
        Model model = mock(Model.class);
        Game game = getGame();
        Player user = getPlayer();
        List<Level> levels = getLevels();

        when(gameService.findByNamelink(GAME_NAME_LINK_EXISTENT)).thenReturn(Optional.of(game));
        when(securityService.getUserId()).thenReturn(1);
        when(playerPhaseService.getPermittedLevelForPlayer(user.getId(), game.getId())).thenReturn(1);
        when(levelService.flagLevelsToOpenedOrNot(game.getId(), 1)).thenReturn(levels);

        String result = gameController.listLevelsOfTheGame(model, GAME_NAME_LINK_EXISTENT);

        verify(model).addAttribute("game", game);
        verify(model).addAttribute("levels", levels);

        Assertions.assertEquals(URL_GAMES_LEVEL, result);
    }

    /* listLevelsOfTheGame - END */

    private Game getGame() {
        Game game = new Game();
        game.setId(1);
        game.setName("Game Name");
        game.setNamelink(GAME_NAME_LINK_EXISTENT);
        game.setActive(true);
        return game;
    }

    private Player getPlayer() {
        Player player = new Player();
        player.setId(1);
        player.setEmail("email@email.com");
        player.setPassword("password");
        player.setRole(Role.USER);
        return player;
    }

    private List<Level> getLevels() {
        List<Level> levels = new ArrayList<>();
        Level level1 = new Level();
        level1.setId(1);
        Level level2 = new Level();
        level2.setId(2);
        levels.add(level1);
        levels.add(level2);
        return levels;
    }
}
