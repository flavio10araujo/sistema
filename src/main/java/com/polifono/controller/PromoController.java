package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_ACOUSTIC_GUITAR;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_ACOUSTIC_GUITAR_MIN;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_MUSIC_THEORY;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_MUSIC_THEORY_MIN;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_RECORDER;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_RECORDER_MIN;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_SAXOPHONE;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_SAXOPHONE_MIN;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_TESTE;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_TRUMPET;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_PHASE_CONTENT_TRUMPET_MIN;
import static com.polifono.common.constant.TemplateConstants.URL_PROMOS_REGISTER;
import static com.polifono.common.constant.TemplateConstants.URL_PROMO_OPEN_SEARCH;
import static com.polifono.common.constant.TemplateConstants.URL_PROMO_SEARCH;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.common.util.ContentUtil;
import com.polifono.common.util.DateUtil;
import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Content;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPromo;
import com.polifono.model.entity.Promo;
import com.polifono.service.IPlayerPromoService;
import com.polifono.service.IPromoService;
import com.polifono.service.impl.ContentService;
import com.polifono.service.impl.MapService;
import com.polifono.service.impl.PhaseService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.game.GameService;
import com.polifono.service.impl.player.PlayerCreditService;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PromoController {

    public static final int FIRST_PHASE_RECORDER = 1;
    public static final int FIRST_PHASE_ACOUSTIC_GUITAR = 92;
    public static final int FIRST_PHASE_SAXOPHONE = 152;
    public static final int FIRST_PHASE_TRUMPET = 212;

    private final SecurityService securityService;
    private final PlayerService playerService;
    private final PlayerCreditService playerCreditService;
    private final GameService gameService;
    private final MapService mapService;
    private final PhaseService phaseService;
    private final ContentService contentService;
    private final IPromoService promoService;
    private final IPlayerPromoService playerPromoService;

    @GetMapping({ "/promo", "/promos" })
    public String promo(final Model model) {
        // If the user is logged in.
        if (securityService.isAuthenticated()) {
            return URL_PROMO_SEARCH;
        } else {
            return URL_PROMO_OPEN_SEARCH;
        }
    }

    @PostMapping("/promo")
    public String promoSearchSubmit(final Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        if (code == null || code.isEmpty()) {
            return URL_PROMO_SEARCH;
        }

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = currentUser.get().getUser();

        // If the player has not confirmed his e-mail yet.
        if (!playerService.isEmailConfirmed(player)
                && player.getIdFacebook() == null) {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent",
                    "Para poder participar das promoções, é necessário primeiramente confirmar o seu e-mail. No momento do seu cadastro, nós lhe enviamos um e-mail com um código de ativação. Acesse seu email e verifique o código enviado.<br />Caso não tenha mais o código, clique <a href='/emailconfirmation'>aqui</a>.");
            return URL_PROMO_SEARCH;
        }

        Date now = DateUtil.getCurrentDateWithHourAndSeconds();
        Promo promo = promoService.findByCodeAndDate(code, now);

        if (promo != null) {
            PlayerPromo playerPromo = playerPromoService.findByPlayerAndPromo(player.getId(), promo.getId());

            // If the player has already used this promotional code.
            if (playerPromo != null) {
                model.addAttribute("message", "error");
                model.addAttribute("messageContent", "Ooops! Você já utilizou esse código uma vez!");
                return URL_PROMO_SEARCH;
            }

            securityService.updateCurrentAuthenticatedUser(playerCreditService.addCreditsToPlayer(player.getId(), promo.getPrize()));

            playerPromo = new PlayerPromo();
            playerPromo.setPlayer(player);
            playerPromo.setPromo(promo);
            playerPromo.setDt(now);
            playerPromoService.save(playerPromo);

            model.addAttribute("message", "success");
            model.addAttribute("promo", promo);
        } else {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", "Ooops! O código informado não existe ou está vencido.");
            return URL_PROMO_SEARCH;
        }

        return URL_PROMO_SEARCH;
    }

    @GetMapping("/promos/register")
    public String register(final Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        return URL_PROMOS_REGISTER;
    }

    @GetMapping("/promos/{gameName}")
    public String promos(
            final Model model,
            @PathVariable("gameName") String gameName
    ) {

        // http://www.polifono.com/promos/musical_theory
        // http://www.polifono.com/promos/recorder
        // http://www.polifono.com/promos/acoustic_guitar
        // http://www.polifono.com/promos/saxophone
        // http://www.polifono.com/promos/trumpet

        // http://www.polifono.com/promos/testes

        int levelOrder = 1;
        int mapOrder = 1;
        int phaseOrder = 1;
        String gameNameParam = gameName;

        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        // workaround - if it's trying to see the musical_theory course, it's showed the recorder course.
        if ("musical_theory".equals(gameName)) {
            gameNameParam = "recorder";
        }

        // teste
        if ("teste".equals(gameName)) {
            gameNameParam = "acoustic_guitar";
        }

        Optional<Game> game = gameService.findByNamelink(gameNameParam);

        if (game.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Map> map = mapService.findByGameLevelAndOrder(game.get().getId(), levelOrder, mapOrder);

        if (map.isEmpty()) {
            return REDIRECT_HOME;
        }

        Optional<Phase> phase = phaseService.findByMapAndOrder(map.get().getId(), phaseOrder);

        if (phase.isEmpty()) {
            return REDIRECT_HOME;
        }

        // Get the first content of this phase.
        Optional<Content> contentOpt = ContentUtil.formatContent(contentService.findByPhaseAndOrder(phase.get().getId(), 1).orElse(null));

        // If the content doesn't exist.
        if (contentOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        model.addAttribute("game", game);
        model.addAttribute("map", map);
        model.addAttribute("phase", phase);
        model.addAttribute("content", contentOpt.get());

        // trumpet
        if ("trumpet".equals(gameName)) {
            return URL_PROMOS_PHASE_CONTENT_TRUMPET;
        }
        // saxophone
        else if ("saxophone".equals(gameName)) {
            return URL_PROMOS_PHASE_CONTENT_SAXOPHONE;
        }
        // acoustic_guitar
        else if ("acoustic_guitar".equals(gameName)) {
            return URL_PROMOS_PHASE_CONTENT_ACOUSTIC_GUITAR;
        }
        // musical_theory
        else if ("musical_theory".equals(gameName)) {
            return URL_PROMOS_PHASE_CONTENT_MUSIC_THEORY;
        }
        // teste
        else if ("teste".equals(gameName)) {
            return URL_PROMOS_PHASE_CONTENT_TESTE;
        }
        // recorder
        else {
            return URL_PROMOS_PHASE_CONTENT_RECORDER;
        }
    }

    @GetMapping("/promos/recorder")
    public String promosRecorder(final Model model) {
        //Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(FIRST_PHASE_RECORDER, 1));
        //model.addAttribute("content", content);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        return URL_PROMOS_PHASE_CONTENT_RECORDER_MIN;
    }

    @GetMapping("/promos/musical_theory")
    public String promosMusicalTheory(final Model model) {
        //Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(FIRST_PHASE_RECORDER, 1));
        //model.addAttribute("content", content);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        return URL_PROMOS_PHASE_CONTENT_MUSIC_THEORY_MIN;
    }

    @GetMapping("/promos/acoustic_guitar")
    public String promosAcousticGuitar(final Model model) {
        //Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(FIRST_PHASE_ACOUSTIC_GUITAR, 1));
        //model.addAttribute("content", content);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        //return URL_PROMOS_PHASECONTENT_ACOUSTIC_GUITAR;
        return URL_PROMOS_PHASE_CONTENT_ACOUSTIC_GUITAR_MIN;
    }

    @GetMapping("/promos/saxophone")
    public String promosSaxophone(final Model model) {
        //Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(FIRST_PHASE_SAXOPHONE, 1));
        //model.addAttribute("content", content);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        return URL_PROMOS_PHASE_CONTENT_SAXOPHONE_MIN;
    }

    @GetMapping("/promos/trumpet")
    public String promosTrumpet(final Model model) {
        //Content content = ContentUtil.formatContent(contentService.findByPhaseAndOrder(FIRST_PHASE_SAXOPHONE, 1));
        //model.addAttribute("content", content);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());

        return URL_PROMOS_PHASE_CONTENT_TRUMPET_MIN;
    }
}
