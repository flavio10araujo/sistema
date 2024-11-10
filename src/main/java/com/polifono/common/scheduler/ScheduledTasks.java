package com.polifono.common.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.polifono.model.entity.Communication;
import com.polifono.model.entity.Groupcommunication;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerCommunication;
import com.polifono.service.ICommunicationService;
import com.polifono.service.IPlayerCommunicationService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerCommunicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledTasks {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final PlayerCommunicationService playerService;
    private final ICommunicationService communicationService;
    private final IPlayerCommunicationService playerCommunicationService;
    private final SendEmailService sendEmailService;

    /**
     * initialDelay = How many milliseconds this method will be called after the start of the application.
     * fixedDelay = The frequency that this method will be called.
     * TimeUnit.MINUTES.sleep(N) = The fixedDelay will start counting only after this time is finished.
     */
    @Scheduled(initialDelay = 300000, fixedDelay = 4200000)
    public void sendEmailGroup04AndGroup05() {
        log.info("Job sendEmailGroup04AndGroup05() :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

        // GROUP 04
        Groupcommunication groupCommunicationG4 = new Groupcommunication();
        groupCommunicationG4.setId(4);
        List<Player> playersG4 = playerService.findCommunicationGroup04();
        sendEmailService.sendEmailCommunication(groupCommunicationG4.getId(), playersG4);
        registerMessages(groupCommunicationG4, playersG4);

        // GROUP 05
        Groupcommunication groupCommunicationG5 = new Groupcommunication();
        groupCommunicationG5.setId(5);
        List<Player> playersG5 = playerService.findCommunicationGroup05();
        sendEmailService.sendEmailCommunication(groupCommunicationG5.getId(), playersG5);
        registerMessages(groupCommunicationG5, playersG5);

        try {
            // Time estimated for running this job (send all the emails in this case).
            TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException ex) {
            log.error("sendEmailGroup04AndGroup05() - Ran into an error", ex);
            throw new IllegalStateException(ex);
        }
    }

    private void registerMessages(Groupcommunication groupcommunication, List<Player> players) {
        Communication communication = new Communication();
        communication.setGroupcommunication(groupcommunication);
        communication.setDtInc(new Date());

        if (players != null && !players.isEmpty()) {
            communicationService.save(communication);

            for (Player player : players) {
                PlayerCommunication playerCommunication = new PlayerCommunication();
                playerCommunication.setPlayer(player);
                playerCommunication.setCommunication(communication);
                playerCommunicationService.save(playerCommunication);
            }
        }
    }
}
