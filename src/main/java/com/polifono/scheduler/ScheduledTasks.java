package com.polifono.scheduler;

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
import com.polifono.util.EmailSendUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    	
    	// GROUP 04
    	Groupcommunication groupcommunicationG4 = new Groupcommunication();
		groupcommunicationG4.setId(4);
		List<Player> playersG4 = playerService.findCommunicationGroup04();
		EmailSendUtil.sendEmailCommunication(groupcommunicationG4.getId(), playersG4);
		registerMessages(groupcommunicationG4, playersG4);
		
		// GROUP 05
		Groupcommunication groupcommunicationG5 = new Groupcommunication();
		groupcommunicationG5.setId(5);
		List<Player> playersG5 = playerService.findCommunicationGroup05();
		EmailSendUtil.sendEmailCommunication(groupcommunicationG5.getId(), playersG5);
		registerMessages(groupcommunicationG5, playersG5);
    	
        try {
        	// Time estimated for running this job (send all the emails in this case).
        	TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException ex) {
            logger.error("sendEmailGroup04AndGroup05() - Ran into an error {}", ex);
            throw new IllegalStateException(ex);
        }
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