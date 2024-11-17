package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
@RequiredArgsConstructor
@Component
public class SendEmailService {

    @Value("${app.email.accounts.general.to}")
    private String emailGeneralTo;

    private final EmailSender emailSender;
    private final EmailMessageBuilder emailMessageBuilder;

    /**
     * This method is used to send the email to the user confirm his email.
     */
    public void sendEmailConfirmRegister(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getEmailConfirmed();

        try {
            sendHtmlMail(false, 1, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailConfirmRegister", e);
        }
    }

    /**
     * This method is used to send the email to the user when he doesn't remember his password.
     */
    public void sendEmailPasswordReset(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getPasswordReset();

        try {
            sendHtmlMail(false, 2, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailPasswordReset", e);
        }
    }

    /**
     * Send one email of type 3 (payment registered).
     */
    public void sendEmailPaymentRegistered(Player player, int quantity) {
        String[] args = new String[2];
        args[0] = player.getName();
        args[1] = "" + quantity;

        try {
            if (player.getEmail() != null && !player.getEmail().isEmpty()) {
                sendHtmlMail(false, 3, player.getEmail(), args);
            }
        } catch (Exception e) {
            log.error("sendEmailPaymentRegistered", e);
        }
    }

    /**
     * Send one email of type 4 (invitation to class).
     */
    public void sendEmailInvitationToClass(Player player, ClassPlayer classPlayer) {
        String[] args = new String[3];
        args[0] = classPlayer.getPlayer().getName(); // Student
        args[1] = player.getName(); // Teacher
        args[2] = classPlayer.getClazz().getName();

        try {
            sendHtmlMail(false, 4, classPlayer.getPlayer().getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailInvitationToClass", e);
        }
    }

    /**
     * Send one email of type 5 (contact).
     */
    public void sendEmailContact(String email, String message) {
        String[] args = new String[2];
        args[0] = email;
        args[1] = message;

        try {
            sendHtmlMail(false, 5, emailGeneralTo, args);
        } catch (Exception e) {
            log.error("sendEmailContact", e);
        }
    }

    /**
     * This method is used to send the email to list of players from the group communication of type groupCommunicationId.
     */
    public void sendEmailCommunication(int groupCommunicationId, List<Player> players) {
        int messageType;

        switch (groupCommunicationId) {
        case 4:
            messageType = 104;
            break;
        case 5:
            messageType = 105;
            break;
        default:
            return;
        }

        for (Player player : players) {
            String[] args = new String[5];
            args[0] = player.getName();
            args[1] = (player.getIdFacebook() != null ? String.valueOf(player.getIdFacebook()) : null);
            args[2] = String.valueOf(player.getCredit());
            args[3] = String.valueOf(player.getScore());
            args[4] = "(" + player.getRankLevel() + ") " + player.getRankColor();

            try {
                sendHtmlMail(true, messageType, player.getEmail(), args);
            } catch (Exception e) {
                log.error("sendEmailCommunication", e);
            }
        }
    }

    private void sendHtmlMail(boolean sync, int messageType, String to, String[] args) {
        String from = emailMessageBuilder.getEmailFrom(messageType);
        String subject = emailMessageBuilder.getEmailSubject(messageType, args);
        String message = emailMessageBuilder.getEmailMessage(messageType, args);

        emailSender.sendEmail(sync, from, subject, replaceParamsMessage(message, args), to);
    }

    /**
     * This method get an email and replaces the strings into the email by the args strings.<br>
     * Eg:<br>
     * {0} will be replaced by the first argument into the array<br>
     * {1} will be replaced by the second argument into the array
     *
     * @param message the message
     * @param args    the string to replace into the email string.
     * @return The new string email with the correct messages.
     */
    private String replaceParamsMessage(String message, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                message = message.replace("{" + i + "}", args[i]);
            }
        }

        return message;
    }
}
