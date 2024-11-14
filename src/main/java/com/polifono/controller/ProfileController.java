package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
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
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.polifono.service.IPlayervideoService;
import com.polifono.service.ITransactionService;
import com.polifono.service.handler.PlayerHandler;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

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
    private final IPlayervideoService playervideoService;
    private final IContentService contentService;

    @GetMapping("/players/{playerId}")
    public String profilePlayer(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // If the user is accessing his own profile OR if the user is an admin.
        if (currentUser.get().getUser().getId() == playerId || currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
            model.addAttribute("editAvailable", true);

            if (currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
                model.addAttribute("deleteAvailable", true);
            } else {
                model.addAttribute("deleteAvailable", false);
            }
        } else {
            model.addAttribute("editAvailable", false);
            model.addAttribute("deleteAvailable", false);
        }

        List<Phase> phases = phaseService.findGamesForProfile(playerOpt.get().getId());
        if (phases == null) {
            phases = new ArrayList<>();
        }

        List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(playerOpt.get().getId());
        if (playerPhases == null) {
            playerPhases = new ArrayList<>();
        }

        List<Diploma> diplomas = diplomaService.findByPlayer(playerOpt.get().getId());
        if (diplomas == null) {
            diplomas = new ArrayList<>();
        }

        model.addAttribute("player", playerOpt.get());
        model.addAttribute("phases", phases);
        model.addAttribute("playerPhases", playerPhases);
        model.addAttribute("diplomas", diplomas);

        return URL_PROFILE_PROFILE_PLAYER;
    }

    @GetMapping("/players/{playerId}/score")
    public String score(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> player = playerService.findById(playerId);
        if (player.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // If the player logged in is not the player id && is not ADMIN and is not TEACHER.
        if (currentUser.get().getUser().getId() != playerId && !currentUser.get().getUser().getRole().equals(Role.ADMIN) && !currentUser.get().getUser()
                .getRole().equals(Role.TEACHER)) {
            return REDIRECT_HOME;
        }

        // The teacher only can see his own page and of his students.
        if (currentUser.get().getUser().getId() != playerId && currentUser.get().getUser().getRole().equals(Role.TEACHER)) {
            if (!classPlayerService.isMyStudent(currentUser.get().getUser(), player.get())) {
                return REDIRECT_HOME;
            }
        }

        List<PlayerPhase> playerPhases = playerPhaseService.findByPlayer(player.get().getId());
        List<Game> playerPhasesGames = new ArrayList<>();

        if (playerPhases == null) {
            playerPhases = new ArrayList<>();
        } else {
            playerPhasesGames = playerPhaseService.filterPlayerPhasesListByGame(playerPhases);
        }

        model.addAttribute("player", player.get());
        model.addAttribute("playerPhases", playerPhases);
        model.addAttribute("playerPhasesGames", playerPhasesGames);
        model.addAttribute("levels", levelService.findAll());

        // Students can see his own grades, but in a different page.
        if (currentUser.get().getUser().getRole().equals(Role.USER) || currentUser.get().getUser().getRole().equals(Role.ADMIN)) {
            model.addAttribute("editAvailable", true);
            return URL_PROFILE_PROFILE_SCORE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_SCORE;
        }
    }

    @GetMapping("/players/{playerId}/attendance")
    public String attendance(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> player = playerService.findById(playerId);
        if (player.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        // If the player logged in is not the player ID && is not ADMIN and is not TEACHER.
        if (currentUser.get().getUser().getId() != playerId && !currentUser.get().getUser().getRole().equals(Role.ADMIN) && !currentUser.get().getUser()
                .getRole().equals(Role.TEACHER)) {
            return REDIRECT_HOME;
        }

        // The teacher only can see his own page and of his students.
        if (currentUser.get().getUser().getId() != playerId && currentUser.get().getUser().getRole().equals(Role.TEACHER)) {
            if (!classPlayerService.isMyStudent(currentUser.get().getUser(), player.get())) {
                return REDIRECT_HOME;
            }
        }

        List<Login> logins = loginService.findByPlayer(player.get().getId());

        if (logins == null) {
            logins = new ArrayList<>();
        }

        model.addAttribute("player", player.get());
        model.addAttribute("logins", logins);

        // Students can see his own attendances, but in a different page.
        if (currentUser.get().getUser().getRole().equals(Role.USER) || currentUser.get().getUser().getRole().equals(Role.ADMIN)) {
            model.addAttribute("editAvailable", true);
            return URL_PROFILE_PROFILE_ATTENDANCE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_ATTENDANCE;
        }
    }

    @GetMapping("/players/{playerId}/credits")
    public String credits(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> player = playerService.findById(playerId);
        if (player.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        // If the player logged in is not the player ID && is not ADMIN.
        if (currentUser.get().getUser().getId() != playerId && !currentUser.get().getUser().getRole().equals(Role.ADMIN)) {
            return REDIRECT_HOME;
        }

        List<Transaction> transactions = transactionService.findByPlayerAndStatus(player.get(), 3);
        List<Transaction> transactions4 = transactionService.findByPlayerAndStatus(player.get(), 4); // PagSeguro

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        if (transactions4 != null) {
            transactions.addAll(transactions4);
        }

        model.addAttribute("player", player.get());
        model.addAttribute("transactions", transactions);
        model.addAttribute("editAvailable", true);

        return URL_PROFILE_PROFILE_CREDITS;
    }

    @GetMapping("/players/{playerId}/videos")
    public String videos(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playerId);
        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the player logged in is not the playerId && is not ADMIN.
        if (currentUser.get().getUser().getId() != playerId && !currentUser.get().getUser().getRole().equals(Role.ADMIN)) {
            model.addAttribute("editAvailable", false);
        } else {
            model.addAttribute("editAvailable", true);
        }

        model.addAttribute("player", player);

        return URL_PROFILE_PROFILE_VIDEOS;
    }

    @GetMapping("/players/{playerId}/edit")
    public String profilePlayerEdit(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Verify if the playerId belongs to the player logged OR the user logged is an admin.
        if (currentUser.get().getUser().getId() == playerId || currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
            Optional<Player> playerOpt = playerService.findById(playerId);

            if (playerOpt.isEmpty()) {
                return URL_PROFILE_PROFILE_NOT_FOUND;
            }

            model.addAttribute("player", playerOpt.get());
        } else {
            log.warn("Someone tried to edit another player with a different id.");
            return REDIRECT_HOME;
        }

        return URL_PROFILE_PROFILE_PLAYER_EDIT;
    }

    @GetMapping("/players/{playerId}/addVideo")
    public String profilePlayerAddVideo(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Verify if the playerId belongs to the player logged OR the user logged is an admin.
        if (currentUser.get().getUser().getId() == playerId || currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
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
        } else {
            log.debug("Someone tried to add a video to another player with a different id.");
            return REDIRECT_HOME;
        }

        return URL_PROFILE_PROFILE_PLAYER_ADD_VIDEO;
    }

    @PostMapping("/players/update")
    public String update(@ModelAttribute("edit") Player edit, final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // The player only can edit his own profile.
        // The admin can edit all the profiles.
        if (currentUser.get().getUser().getId() != edit.getId() && !currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
            return REDIRECT_HOME;
        }

        String msg = playerManagementService.validateUpdateProfile(edit);

        if (!"".equals(msg)) {
            redirectAttributes.addFlashAttribute("message", "error");
            redirectAttributes.addFlashAttribute("messageContent", msg);
            return "redirect:/profile/players/" + edit.getId() + "/edit";
        }

        Optional<Player> player = playerService.findById(edit.getId());

        // If the player does not exist.
        if (player.isEmpty()) {
            return REDIRECT_HOME;
        }

        try {
            player.get().setName(PlayerUtil.formatNamePlayer(edit.getName().trim() + " " + edit.getLastName().trim()));

            String name = player.get().getName();
            name = name.substring(0, name.indexOf(" "));

            String lastName = player.get().getName();
            lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();

            player.get().setLastName(lastName);
            player.get().setName(name);
            player.get().setPhone(edit.getPhone());
            player.get().setSex(edit.getSex());
            player.get().setDtBirth(edit.getDtBirth());
            player.get().setAddress(edit.getAddress());

            if (edit.getAbout() != null && edit.getAbout().length() > 500) {
                player.get().setAbout(edit.getAbout().substring(0, 499));
            } else {
                player.get().setAbout(edit.getAbout());
            }

            playerService.save(player.get());
            redirectAttributes.addFlashAttribute("edit", "success");

            // If player logged is editing his own profile.
            if (currentUser.get().getUser().getId() == player.get().getId()) {
                // Update the currentAuthenticateUser
                securityService.updateCurrentAuthenticatedUser(player.get());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/profile/players/" + edit.getId();
    }

    @PostMapping("/players/addVideo")
    public String addVideo(final Model model, @ModelAttribute("playervideo") Playervideo playervideo, final RedirectAttributes redirectAttributes) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        // The player only can add videos in his name.
        // The admin can add videos for everybody.
        if (currentUser.get().getUser().getId() != playervideo.getPlayer().getId() && !currentUser.get().getUser().getRole().toString().equals("ADMIN")) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playervideo.getPlayer().getId());

        if (playerOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        playervideo.setPlayer(player);

        String msg = playerManagementService.validateAddVideo(playervideo);

        // If no problems have been detected until now.
        if ("".equals(msg)) {
            // The player cannot add a video in a phase that he hasn't finished yet.
            if (playerPhaseService.findByPlayerPhaseAndStatus(player.getId(), playervideo.getContent().getPhase().getId(), 3) == null) {
                msg = "<br />Só é permitido adicionar vídeos em aulas que o aluno já concluiu.";
            }
            // The player cannot add more than one video in the same phase.
            else if (playervideoService.findByPlayerAndPhase(player, playervideo.getContent().getPhase()) != null) {
                msg = "<br />Não é permitido adicionar mais de um vídeo para a mesma fase.";
            }
        }

        if (!"".equals(msg)) {
            // Msgs.
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", msg);
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

            if (currentUser.get().getUser().getId() == playervideo.getPlayer().getId()) {
                // Update session user.
                securityService.updateCurrentAuthenticatedUser(player);
            }

            playervideo.setContent(contentService.findByPhaseAndOrder(playervideo.getContent().getPhase().getId(), 1).orElse(null));
            playervideoService.save(playervideo);

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/profile/players/" + playervideo.getPlayer().getId() + "/videos";
    }
}
