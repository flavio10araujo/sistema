package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.constant.TemplateConstants.URL_GAMES_LEVEL;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;
import com.polifono.service.helper.GameHelperService;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    private static final String GAME_NAME_LINK_INEXISTENT = "game_inexistent";
    private static final String GAME_NAME_LINK_EXISTENT = "game_existent";

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameHelperService gameHelperService;

    /* listGames - BEGIN */
    @Test
    public void whenListGames_thenReturnGamesIndexPage() {
        Model model = mock(Model.class);
        doNothing().when(gameHelperService).addRankingToModel(model);

        String result = gameController.listGames(model);

        verify(gameHelperService).addRankingToModel(model);
        Assertions.assertEquals(URL_GAMES_INDEX, result);
    }
    /* listGames - END */

    /* listLevelsOfTheGame - BEGIN */
    @Test
    public void givenGameDoesNotExist_WhenListLevelsOfTheGame_thenRedirectToHomePage() {
        when(gameHelperService.getGameByNamelinkOrRedirectHome(GAME_NAME_LINK_INEXISTENT)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> gameController.listLevelsOfTheGame(mock(Model.class), GAME_NAME_LINK_INEXISTENT)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Expected status is NOT_FOUND");
    }

    @Test
    public void givenGameExist_whenListLevelsOfTheGame_thenOpenGameLevelPage() {
        Model model = mock(Model.class);
        Game game = getGame();

        when(gameHelperService.getGameByNamelinkOrRedirectHome(GAME_NAME_LINK_EXISTENT)).thenReturn(game);
        doNothing().when(gameHelperService).addGameAndLevelsToModel(model, game);

        String result = gameController.listLevelsOfTheGame(model, GAME_NAME_LINK_EXISTENT);

        verify(gameHelperService).addGameAndLevelsToModel(model, game);
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
