package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_GAMES;
import static com.polifono.common.TemplateConstants.URL_GAMES_INDEX;
import static com.polifono.common.TemplateConstants.URL_GAMES_LEVEL;
import static com.polifono.common.TemplateConstants.URL_GAMES_MAP;
import static com.polifono.common.TemplateConstants.URL_GAMES_PHASE_CONTENT;
import static com.polifono.common.TemplateConstants.URL_GAMES_PHASE_TEST;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Content;
import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Question;
import com.polifono.service.IGameService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.helper.GameHelperService;
import com.polifono.service.impl.SecurityService;
import com.polifono.validation.ValidGameNameLink;
import com.polifono.validation.ValidLevelOrder;
import com.polifono.validation.ValidMapOrder;
import com.polifono.validation.ValidPhaseOrder;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final SecurityService securityService;
    private final IGameService gameService;
    private final GameHelperService gameHelperService;
    private final IPhaseService phaseService;
    private final IPlayerPhaseService playerPhaseService;

    @GetMapping("/games")
    public String listGames(final Model model) {
        gameHelperService.addRankingToModel(model);
        return URL_GAMES_INDEX;
    }

    @Validated
    @GetMapping("/games/{gameNameLink}")
    public String listLevelsOfTheGame(
            final Model model,
            @PathVariable("gameNameLink") @ValidGameNameLink String gameNameLink
    ) {
        Game game = gameHelperService.getGameByNamelinkOrRedirectHome(gameNameLink);

        gameHelperService.addGameAndLevelsToModel(model, game);
        return URL_GAMES_LEVEL;
    }

    @Validated
    @GetMapping("/games/{gameNameLink}/{levelOrder}/{mapOrder}")
    public String listPhasesOfTheMap(
            final Model model,
            @PathVariable("gameNameLink") @ValidGameNameLink String gameNameLink,
            @PathVariable("levelOrder") @ValidLevelOrder int levelOrder,
            @PathVariable("mapOrder") @ValidMapOrder int mapOrder
    ) {
        Game game = gameHelperService.getGameByNamelinkOrRedirectHome(gameNameLink);
        Map map = gameHelperService.getMapByGameLevelAndOrderOrRedirectHome(game, levelOrder, mapOrder);

        gameHelperService.addGameMapAndPhasesToModel(model, game, map);
        return URL_GAMES_MAP;
    }

    /**
     * List all the phases of the current map of the player logged in.
     * If the player didn't play any phase of this game, the first phase of the first map of the game will be shown.
     * If the player has already played a phase of this game, it will be shown the right map with the next phase unlocked.
     */
    @Validated
    @GetMapping("/games/{gameNameLink}/{levelOrder}/{mapOrder}/{phaseOrder}")
    public String initPhase(
            final Model model,
            @PathVariable("gameNameLink") @ValidGameNameLink String gameNameLink,
            @PathVariable("levelOrder") @ValidLevelOrder int levelOrder,
            @PathVariable("mapOrder") @ValidMapOrder int mapOrder,
            @PathVariable("phaseOrder") @ValidPhaseOrder int phaseOrder,
            Locale locale
    ) {
        Game game = gameHelperService.getGameByNamelinkOrRedirectHome(gameNameLink);
        Map map = gameHelperService.getMapByGameLevelAndOrderOrRedirectHome(game, levelOrder, mapOrder);
        Phase phase = gameHelperService.getPhaseByMapAndOrderOrRedirectHome(map, phaseOrder);
        Content content = gameHelperService.getContentByPhaseOrRedirectHome(phase);

        gameHelperService.isPlayerAllowedToAccessPhaseOrRedirectHome(phase);

        if (phaseService.shouldDisplayBuyCreditsPage(game, phase)) {
            return gameHelperService.handleDisplayBuyCreditsPage(model, locale);
        }

        gameHelperService.addGameMapPhaseAndContentToModel(model, game, map, phase, content);
        return URL_GAMES_PHASE_CONTENT;
    }

    @GetMapping("/games/{gameNameLink}/{levelOrder}/{mapOrder}/{phaseOrder}/test")
    @Validated
    public String initTest(
            HttpSession session,
            final Model model,
            @PathVariable("gameNameLink") @ValidGameNameLink String gameNameLink,
            @PathVariable("levelOrder") @ValidLevelOrder int levelOrder,
            @PathVariable("mapOrder") @ValidMapOrder int mapOrder,
            @PathVariable("phaseOrder") @ValidPhaseOrder int phaseOrder,
            Locale locale
    ) {
        Game game = gameHelperService.getGameByNamelinkOrRedirectHome(gameNameLink);
        Map map = gameHelperService.getMapByGameLevelAndOrderOrRedirectHome(game, levelOrder, mapOrder);
        Phase phase = gameHelperService.getPhaseByMapAndOrderOrRedirectHome(map, phaseOrder);
        Content content = gameHelperService.getContentByPhaseOrRedirectHome(phase);

        gameHelperService.isPlayerAllowedToAccessPhaseOrRedirectHome(phase);

        if (playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, securityService.getUserId())) {
            return REDIRECT_GAMES + "/" + gameNameLink;
        }

        if (phaseService.shouldDisplayBuyCreditsPage(game, phase)) {
            return gameHelperService.handleDisplayBuyCreditsPage(model, locale);
        }

        playerPhaseService.registerTestAttempt(phase);

        List<Question> questions = gameHelperService.getQuestionsByContentOrRedirectHome(content);

        gameHelperService.addQuestionsIdToSession(session, questions);
        gameHelperService.addGameMapPhaseAndQuestionsToModel(model, game, map, phase, questions);
        return URL_GAMES_PHASE_TEST;
    }

    @PostMapping("/games/result")
    public String showResultTest(
            HttpSession session,
            final Model model,
            @RequestParam java.util.Map<String, String> playerAnswers // playerAnswers is all the parameters passed, not only the player's answers.
    ) {
        List<Integer> questionsId = gameHelperService.getQuestionsIdFromSessionOrRedirectHome(session);
        int grade = gameService.calculateGrade(questionsId, playerAnswers);
        Phase currentPhase = gameService.getPhaseOfTheTest(questionsId);

        if (!phaseService.hasPlayerPassedPhase(grade)) {
            return gameHelperService.handleFailedPhase(model, currentPhase, grade);
        }

        return gameHelperService.handlePassedPhase(model, currentPhase, grade);
    }
}
