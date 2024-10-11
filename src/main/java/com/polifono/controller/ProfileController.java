package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_ATTENDANCE;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_ATTENDANCE_OWNER;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_CREDITS;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_NOT_FOUND;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_PLAYER;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_PLAYER_ADD_VIDEO;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_PLAYER_EDIT;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_SCORE;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_SCORE_OWNER;
import static com.polifono.common.TemplateConstants.URL_PROFILE_PROFILE_VIDEOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polifono.domain.Diploma;
import com.polifono.domain.Game;
import com.polifono.domain.Login;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Playervideo;
import com.polifono.domain.Transaction;
import com.polifono.domain.enums.Role;
import com.polifono.service.IClassPlayerService;
import com.polifono.service.IContentService;
import com.polifono.service.IDiplomaService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.ILoginService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IPlayervideoService;
import com.polifono.service.ITransactionService;
import com.polifono.util.PlayerUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    private final IPlayerService playerService;
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

    @RequestMapping(value = { "/player/{playerId}" }, method = RequestMethod.GET)
    public final String profilePlayer(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the user is logged AND (he is accessing his own profile OR he is an admin).
        if (
                this.currentAuthenticatedUser() != null
                        &&
                        (
                                Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() == playerId
                                        ||
                                        Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().toString().equals("ADMIN")
                        )
        ) {

            model.addAttribute("editAvailable", true);

            if (Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().toString().equals("ADMIN")) {
                model.addAttribute("deleteAvailable", true);
            } else {
                model.addAttribute("deleteAvailable", false);
            }
        } else {
            model.addAttribute("editAvailable", false);
            model.addAttribute("deleteAvailable", false);
        }

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

    @RequestMapping(value = { "/player/{playerId}/score" }, method = RequestMethod.GET)
    public final String score(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the player logged in is not the player Id && is not ADMIN and is not TEACHER.
        if (
                Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() != playerId &&
                        !Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN) &&
                        !Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.TEACHER)
        ) {
            return REDIRECT_HOME;
        }

        // The teacher only can see his own page and of his students.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() != playerId &&
                Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.TEACHER)) {

            if (!classPlayerService.isMyStudent(Objects.requireNonNull(currentAuthenticatedUser()).getUser(), player)) {
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

        // Students can see his own grades, but in a different page.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.USER)
                || Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN)) {

            model.addAttribute("editAvailable", true);

            return URL_PROFILE_PROFILE_SCORE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_SCORE;
        }
    }

    @RequestMapping(value = { "/player/{playerId}/attendance" }, method = RequestMethod.GET)
    public final String attendance(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the player logged in is not the player ID && is not ADMIN and is not TEACHER.
        if (
                Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() != playerId &&
                        !Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN) &&
                        !Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().equals(Role.TEACHER)
        ) {
            return REDIRECT_HOME;
        }

        // The teacher only can see his own page and of his students.
        if (Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() != playerId &&
                Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().equals(Role.TEACHER)) {

            if (!classPlayerService.isMyStudent(Objects.requireNonNull(currentAuthenticatedUser()).getUser(), player)) {
                return REDIRECT_HOME;
            }
        }

        List<Login> logins = loginService.findByPlayer(player.getId());

        if (logins == null) {
            logins = new ArrayList<>();
        }

        model.addAttribute("player", player);
        model.addAttribute("logins", logins);

        // Students can see his own attendances, but in a different page.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.USER)
                || Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN)) {

            model.addAttribute("editAvailable", true);

            return URL_PROFILE_PROFILE_ATTENDANCE_OWNER;
        } else {
            return URL_PROFILE_PROFILE_ATTENDANCE;
        }
    }

    @RequestMapping(value = { "/player/{playerId}/credits" }, method = RequestMethod.GET)
    public final String credits(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the player logged in is not the player ID && is not ADMIN.
        if (
                Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() != playerId &&
                        !Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN)
        ) {
            return REDIRECT_HOME;
        }

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

    @RequestMapping(value = { "/player/{playerId}/videos" }, method = RequestMethod.GET)
    public final String videos(final Model model, @PathVariable("playerId") Integer playerId) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty()) {
            return URL_PROFILE_PROFILE_NOT_FOUND;
        }

        Player player = playerOpt.get();

        // If the player logged in is not the playerId && is not ADMIN.
        if (
                Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() != playerId &&
                        !Objects.requireNonNull(currentAuthenticatedUser()).getUser().getRole().equals(Role.ADMIN)
        ) {
            model.addAttribute("editAvailable", false);
        } else {
            model.addAttribute("editAvailable", true);
        }

        model.addAttribute("player", player);

        return URL_PROFILE_PROFILE_VIDEOS;
    }

    @RequestMapping(value = { "/player/edit/{playerId}" }, method = RequestMethod.GET)
    public final String profilePlayerEdit(final Model model, @PathVariable("playerId") Integer playerId) {

        // Verify if the playerId belongs to the player logged OR the user logged is an admin.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() == playerId || Objects.requireNonNull(this.currentAuthenticatedUser())
                .getUser().getRole().toString().equals("ADMIN")) {
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

    @RequestMapping(value = "/player/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("edit") Player edit, final RedirectAttributes redirectAttributes) {

        // The player only can edit his own profile.
        // The admin can edit all the profiles.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() != edit.getId()
                && !Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().toString().equals("ADMIN")) {
            return REDIRECT_HOME;
        }

        String msg = playerService.validateUpdateProfile(edit);

        if (!"".equals(msg)) {
            redirectAttributes.addFlashAttribute("message", "error");
            redirectAttributes.addFlashAttribute("messageContent", msg);
            return "redirect:/profile/player/edit/" + edit.getId();
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
            if (Objects.requireNonNull(currentAuthenticatedUser()).getUser().getId() == player.get().getId()) {
                // Update the currentAuthenticateUser
                this.updateCurrentAuthenticateUser(player.get());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("edit", "unsuccess");
        }

        return "redirect:/profile/player/" + edit.getId();
    }

    @RequestMapping(value = { "/player/addVideo/{playerId}" }, method = RequestMethod.GET)
    public final String profilePlayerAddVideo(final Model model, @PathVariable("playerId") Integer playerId) {

        // Verify if the playerId belongs to the player logged OR the user logged is an admin.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() == playerId || Objects.requireNonNull(this.currentAuthenticatedUser())
                .getUser().getRole().toString().equals("ADMIN")) {
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

    @RequestMapping(value = "/player/addVideo", method = RequestMethod.POST)
    public String addVideo(final Model model, @ModelAttribute("playervideo") Playervideo playervideo, final RedirectAttributes redirectAttributes) {
        // The player only can add videos in his name.
        // The admin can add videos for everybody.
        if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() != playervideo.getPlayer().getId()
                && !Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getRole().toString().equals("ADMIN")) {
            return REDIRECT_HOME;
        }

        Optional<Player> playerOpt = playerService.findById(playervideo.getPlayer().getId());

        if (playerOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = playerOpt.get();

        playervideo.setPlayer(player);

        String msg = playerService.validateAddVideo(playervideo);

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

            if (Objects.requireNonNull(this.currentAuthenticatedUser()).getUser().getId() == playervideo.getPlayer().getId()) {
                // Update session user.
                this.updateCurrentAuthenticateUser(player);
            }

            playervideo.setContent(contentService.findByPhaseAndOrder(playervideo.getContent().getPhase().getId(), 1));
            playervideoService.save(playervideo);

            redirectAttributes.addFlashAttribute("save", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("save", "unsuccess");
        }

        return "redirect:/profile/player/" + playervideo.getPlayer().getId() + "/videos";
    }
}
