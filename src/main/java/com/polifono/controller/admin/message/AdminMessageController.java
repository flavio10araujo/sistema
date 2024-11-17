package com.polifono.controller.admin.message;

import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_MESSAGE_GROUP_04;
import static com.polifono.common.constant.TemplateConstants.URL_ADMIN_MESSAGE_GROUP_05;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polifono.model.entity.Communication;
import com.polifono.model.entity.Groupcommunication;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerCommunication;
import com.polifono.service.impl.CommunicationService;
import com.polifono.service.impl.PlayerCommunicationService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/message")
public class AdminMessageController {

    private final PlayerService playerService;
    private final CommunicationService communicationService;
    private final PlayerCommunicationService playerCommunicationService;
    private final SendEmailService sendEmailService;

    @GetMapping("/group04")
    public String group04() {
        return URL_ADMIN_MESSAGE_GROUP_04;
    }

    @PostMapping("/group04")
    public String group04Submit(Model model) {
        Groupcommunication groupcommunication = new Groupcommunication();
        groupcommunication.setId(4);

        List<Player> players = playerService.findCommunicationGroup04();
        sendEmailService.sendEmailCommunication(groupcommunication.getId(), players);

        registerMessages(groupcommunication, players);

        model.addAttribute("players", players);

        return URL_ADMIN_MESSAGE_GROUP_04;
    }

    @GetMapping("/group05")
    public String group05() {
        return URL_ADMIN_MESSAGE_GROUP_05;
    }

    @PostMapping("/group05")
    public String group05Submit(Model model) {

        Groupcommunication groupcommunication = new Groupcommunication();
        groupcommunication.setId(5);

        List<Player> players = playerService.findCommunicationGroup05();

        sendEmailService.sendEmailCommunication(groupcommunication.getId(), players);

        registerMessages(groupcommunication, players);

        model.addAttribute("players", players);

        return URL_ADMIN_MESSAGE_GROUP_05;
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
