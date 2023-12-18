package com.polifono.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.polifono.domain.Communication;
import com.polifono.domain.Groupcommunication;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerCommunication;
import com.polifono.service.ICommunicationService;
import com.polifono.service.IPlayerCommunicationService;
import com.polifono.service.IPlayerService;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private ICommunicationService communicationService;

    @Autowired
    private IPlayerCommunicationService playerCommunicationService;

    /**
     * initialDelay = How many miliseconds this method will be called after the start of the application.
     * fixedDelay = The frequency that this method will be called.
     * TimeUnit.MINUTES.sleep(N) = The fixedDelay will start counting only after this time is finished.
     */
    @Scheduled(initialDelay = 300000, fixedDelay = 4200000)
    public void sendEmailGroup04AndGroup05() {
        logger.info("Job sendEmailGroup04AndGroup05() :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

    }

    private void registerMessages(Groupcommunication groupcommunication, List<Player> players) {
        Communication communication = new Communication();
        communication.setGroupcommunication(groupcommunication);
        communication.setDtInc(new Date());

        if (players != null && players.size() > 0) {
            // T019
            communicationService.save(communication);
        }

        for (Player player : players) {

            PlayerCommunication playerCommunication = new PlayerCommunication();
            playerCommunication.setPlayer(player);
            playerCommunication.setCommunication(communication);

            // T020
            playerCommunicationService.save(playerCommunication);
        }
    }
}
