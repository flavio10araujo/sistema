package com.polifono.controller.admin.message;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polifono.domain.Communication;
import com.polifono.domain.Groupcommunication;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerCommunication;
import com.polifono.service.ICommunicationService;
import com.polifono.service.IPlayerCommunicationService;
import com.polifono.service.IPlayerService;
import com.polifono.util.EmailSendUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/message")
public class AdminMessageController {

    public static final String URL_ADMIN_MSG_G04 = "admin/message/group04";
    public static final String URL_ADMIN_MSG_G05 = "admin/message/group05";

    private final IPlayerService playerService;
    private final ICommunicationService communicationService;
    private final IPlayerCommunicationService playerCommunicationService;

    @RequestMapping(value = { "/group04" }, method = RequestMethod.GET)
    public String group04(Model model) {
        return URL_ADMIN_MSG_G04;
    }

    @RequestMapping(value = { "/group04" }, method = RequestMethod.POST)
    public String group04Submit(Model model) {
        Groupcommunication groupcommunication = new Groupcommunication();
        groupcommunication.setId(4);

        List<Player> players = playerService.findCommunicationGroup04();
        EmailSendUtil.sendEmailCommunication(groupcommunication.getId(), players);

        registerMessages(groupcommunication, players);

        model.addAttribute("players", players);

        return URL_ADMIN_MSG_G04;
    }

    @RequestMapping(value = { "/group05" }, method = RequestMethod.GET)
    public String group05(Model model) {
        return URL_ADMIN_MSG_G05;
    }

    @RequestMapping(value = { "/group05" }, method = RequestMethod.POST)
    public String group05Submit(Model model) {

        Groupcommunication groupcommunication = new Groupcommunication();
        groupcommunication.setId(5);

        List<Player> players = playerService.findCommunicationGroup05();

        EmailSendUtil.sendEmailCommunication(groupcommunication.getId(), players);

        registerMessages(groupcommunication, players);

        model.addAttribute("players", players);

        return URL_ADMIN_MSG_G05;
    }

    private void registerMessages(Groupcommunication groupcommunication, List<Player> players) {
        Communication communication = new Communication();
        communication.setGroupcommunication(groupcommunication);
        communication.setDtInc(new Date());

        // T019
        communicationService.save(communication);

        for (Player player : players) {

            PlayerCommunication playerCommunication = new PlayerCommunication();
            playerCommunication.setPlayer(player);
            playerCommunication.setCommunication(communication);

            // T020
            playerCommunicationService.save(playerCommunication);
        }
    }
}
