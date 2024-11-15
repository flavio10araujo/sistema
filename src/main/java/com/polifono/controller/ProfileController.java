package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.REDIRECT_PROFILE_PLAYERS;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_ATTENDANCE;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_ATTENDANCE_OWNER;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_CREDITS;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_NOT_FOUND;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_PLAYER;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_PLAYER_ADD_VIDEO;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_PLAYER_EDIT;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_SCORE;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_SCORE_OWNER;
import static com.polifono.common.constant.TemplateConstants.URL_PROFILE_PROFILE_VIDEOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.Min;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.common.util.PlayerUtil;
import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Diploma;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Login;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.model.entity.Playervideo;
import com.polifono.model.entity.Transaction;
import com.polifono.model.enums.Role;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.ILoginService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerVideoService;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.player.PlayerHandler;
import com.polifono.service.impl.player.PlayerService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final PlayerService playerService;
    private final PlayerHandler playerManagementService;
    private final IPhaseService phaseService;
    private final IPlayerPhaseService playerPhaseService;
    private final IClassPlayerService classPlayerService;
    private final ILoginService loginService;
    private final IDiplomaService diplomaService;
    private final IGameService gameService;
    private final ILevelService levelService;
    private final ITransactionService transactionService;
    private final IPlayerVideoService playerVideoService;
    private final IContentService contentService;
    private final PlayerHandler playerHandler;

    @Validated
    @GetMapping("/players/{playerId}")
    public String profilePlayer(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();

        // If the user is accessing his own profile OR if the user is an admin.
        if (currentUser.getUser().getId() == playerId || Role.ADMIN.equals(currentUser.getUser().getRole())) {
            model.addAttribute("editAvailable", true);

            if (Role.ADMIN.equals(currentUser.getUser().getRole())) {
                model.addAttribute("deleteAvailable", true);
            } else {
                model.addAttribute("deleteAvailable", false);
            }
        } else {
            model.addAttribute("editAvailable", false);
            model.addAttribute("deleteAvailable", false);
        }

        Player player = playerOpt.get();

        List<Phase> phases = phaseService.findGamesForProfile(player.getId());
        if (phases == null) {
            phases = new ArrayList<>();
        }

        List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(player.getId());
        if (playerPhases == null) {
            playerPhases = new ArrayList<>();
        }

        List<Diploma> diplomas = diplomaService.findByPlayer(player.getId());
        if (diplomas == null) {
            diplomas = new ArrayList<>();
        }

        model.addAttribute("player", player);
        model.addAttribute("phases", phases);
        model.addAttribute("playerPhases", playerPhases);
        model.addAttribute("diplomas", diplomas);

        return URL_PROFILE_PROFILE_PLAYER;
    }

    @Validated
    @GetMapping("/players/{playerId}/score")
    public String score(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != playerId &&
                !Role.ADMIN.equals(currentUser.getUser().getRole()) &&
                !Role.TEACHER.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        // The teacher only can see his own page and of his students.
        if (currentUser.getUser().getId() != playerId &&
                Role.TEACHER.equals(currentUser.getUser().getRole())) {
            if (!classPlayerService.isMyStudent(currentUser.getUser(), player)) {
                return REDIRECT_HOME;
            }
        }

        List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(player.getId());
        List<Game> playerPhasesGames = new ArrayList<>();

        if (playerPhases == null) {
            playerPhases = new ArrayList<>();
        } else {
            playerPhasesGames = playerPhaseService.filterPlayerPhasesListByGame(playerPhases);
        }

        model.addAttribute("player", player);
        model.addAttribute("playerPhases", playerPhases);
        model.addAttribute("playerPhasesGames", playerPhasesGames);
        model.addAttribute("levels", levelService.findAll());

        if (Role.USER.equals(currentUser.getUser().getRole()) ||
                Role.ADMIN.equals(currentUser.getUser().getRole())) {
            model.addAttribute("editAvailable", true);
            return URL_PROFILE_PROFILE_SCORE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_SCORE;
        }
    }

    @Validated
    @GetMapping("/players/{playerId}/attendance")
    public String attendance(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != playerId &&
                !Role.ADMIN.equals(currentUser.getUser().getRole()) &&
                !Role.TEACHER.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        // The teacher only can see his own page and of his students.
        if (currentUser.getUser().getId() != playerId &&
                Role.TEACHER.equals(currentUser.getUser().getRole())) {
            if (!classPlayerService.isMyStudent(currentUser.getUser(), player)) {
                return REDIRECT_HOME;
            }
        }

        List<Login> logins = loginService.findByPlayer(player.getId());
        if (logins == null) {
            logins = new ArrayList<>();
        }

        model.addAttribute("player", player);
        model.addAttribute("logins", logins);

        if (Role.USER.equals(currentUser.getUser().getRole()) ||
                Role.ADMIN.equals(currentUser.getUser().getRole())) {
            model.addAttribute("editAvailable", true);
            return URL_PROFILE_PROFILE_ATTENDANCE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_ATTENDANCE;
        }
    }

    @Validated
    @GetMapping("/players/{playerId}/credits")
    public String credits(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        CurrentUser currentUser = currentUserOpt.get();

        // If the player logged in is not the player ID && is not ADMIN.
        if (currentUser.getUser().getId() != playerId && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        List<Transaction> transactions = transactionService.findByPlayerAndStatus(player, 3);
        List<Transaction> transactions4 = transactionService.findByPlayerAndStatus(player, 4); // PagSeguro

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        if (transactions4 != null) {
            transactions.addAll(transactions4);
        }

        model.addAttribute("player", player);
        model.addAttribute("transactions", transactions);
        model.addAttribute("editAvailable", true);

        return URL_PROFILE_PROFILE_CREDITS;
    }

    @Validated
    @GetMapping("/players/{playerId}/videos")
    public String videos(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != playerId && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            model.addAttribute("editAvailable", false);
        } else {
            model.addAttribute("editAvailable", true);
        }

        model.addAttribute("player", playerOpt.get());

        return URL_PROFILE_PROFILE_VIDEOS;
    }

    @Validated
    @GetMapping("/players/{playerId}/edit")
    public String profilePlayerEdit(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != playerId && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        model.addAttribute("player", playerOpt.get());

        return URL_PROFILE_PROFILE_PLAYER_EDIT;
    }

    @Validated
    @GetMapping("/players/{playerId}/addVideo")
    public String profilePlayerAddVideo(final Model model,
            @PathVariable("playerId") @Min(1) int playerId) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != playerId && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        model.addAttribute("player", player);
        // Filter.
        model.addAttribute("games", gameService.findByActive(true));
        model.addAttribute("levels", levelService.findByActive(true));
        // Form
        Playervideo playervideo = new Playervideo();
        playervideo.setPlayer(player);
        model.addAttribute("playervideo", playervideo);

        return URL_PROFILE_PROFILE_PLAYER_ADD_VIDEO;
    }

    @Validated
    @PostMapping("/players/update")
    public String update(@ModelAttribute("edit") @NonNull Player edit,
            final RedirectAttributes redirectAttributes,
            Locale locale) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();

        if (currentUser.getUser().getId() != edit.getId() && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        String msg = playerManagementService.validateUpdateProfile(edit, locale);

        if (!msg.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "error");
            redirectAttributes.addFlashAttribute("messageContent", msg);
            return REDIRECT_PROFILE_PLAYERS + "/" + edit.getId() + "/edit";
        }

        Optional<Player> playerOpt = playerService.findById(edit.getId());
        if (playerOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        try {
            player.setName(PlayerUtil.formatNamePlayer(edit.getName().trim() + " " + edit.getLastName().trim()));
            playerHandler.formatPlayerNames(player);
            player.setPhone(edit.getPhone());
            player.setSex(edit.getSex());
            player.setDtBirth(edit.getDtBirth());
            player.setAddress(edit.getAddress());

            if (edit.getAbout() != null && edit.getAbout().length() > 500) {
                player.setAbout(edit.getAbout().substring(0, 499));
            } else {
                player.setAbout(edit.getAbout());
            }

            playerService.save(player);
            redirectAttributes.addFlashAttribute("edit", "success");

            if (currentUser.getUser().getId() == player.getId()) {
                securityService.updateCurrentAuthenticatedUser(player);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return REDIRECT_PROFILE_PLAYERS + "/" + edit.getId();
    }

    @Validated
    @PostMapping("/players/addVideo")
    public String addVideo(final Model model,
            @ModelAttribute("playervideo") @NonNull Playervideo playervideo,
            final RedirectAttributes redirectAttributes,
            Locale locale) {

        Optional<CurrentUser> currentUserOpt = securityService.getCurrentAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        CurrentUser currentUser = currentUserOpt.get();
        if (currentUser.getUser().getId() != playervideo.getPlayer().getId() && !Role.ADMIN.equals(currentUser.getUser().getRole())) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playervideo.getPlayer().getId());
        if (playerOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        playervideo.setPlayer(player);

        String validationError = playerManagementService.validateAddVideo(playervideo, locale);

        // If no problems have been detected until now.
        if (validationError.isEmpty()) {
            // The player cannot add a video in a phase that he hasn't finished yet.
            if (playerPhaseService.findByPlayerPhaseAndStatus(player.getId(), playervideo.getContent().getPhase().getId(), 3).isEmpty()) {
                validationError = "<br />" + messagesResource.getMessage("msg.playerProfile.addVideo.notAllowed.unfinishedClass", null, locale);
            }
            // The player cannot add more than one video in the same phase.
            else if (playerVideoService.findByPlayerAndPhase(player, playervideo.getContent().getPhase()).isEmpty()) {
                validationError = "<br />" + messagesResource.getMessage("msg.playerProfile.addVideo.notAllowed.videoAlreadyExists", null, locale);
            }
        }

        if (!validationError.isEmpty()) {
            // Msgs.
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", validationError);
            // Filter.
            model.addAttribute("games", gameService.findByActive(true));
            model.addAttribute("levels", levelService.findByActive(true));
            // Form.
            model.addAttribute("playervideo", playervideo);

            return URL_PROFILE_PROFILE_PLAYER_ADD_VIDEO;
        }

        try {
            player.setScore(player.getScore() + 25);
            player.setCoin(player.getCoin() + 25);

            if (currentUser.getUser().getId() == playervideo.getPlayer().getId()) {
                securityService.updateCurrentAuthenticatedUser(player);
            }

            playervideo.setContent(contentService.findByPhaseAndOrder(playervideo.getContent().getPhase().getId(), 1).orElse(null));
            playerVideoService.save(playervideo);

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return REDIRECT_PROFILE_PLAYERS + "/" + playervideo.getPlayer().getId() + "/videos";
    }
}
