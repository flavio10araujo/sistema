package com.polifono.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.Class;
import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;

@ExtendWith(MockitoExtension.class)
public class SendEmailServiceTest {

    public static final String FROM = "email@example.com";
    public static final String SUBJECT = "Subject";
    public static final String EMAIL_GENERAL_TO = "email_general@email.com";

    @Mock
    private EmailSender emailSender;

    @Mock
    private EmailMessageBuilder emailMessageBuilder;

    @InjectMocks
    private SendEmailService sendEmailService;

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
        player.setName("John Silva");
        player.setEmail("john.silva@email.com");
        player.setEmailConfirmed("activation_code");
        player.setPasswordReset("password_reset_code");
    }

    @Test
    public void givenPlayer_WhenSendEmailConfirmRegister_ThenSendEmail() {
        String message = "Name {0} Email {1} Code {2}";
        String[] args = { player.getName(), player.getEmail(), player.getEmailConfirmed() };

        when(emailMessageBuilder.getEmailFrom(1)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(1, args)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(1, args)).thenReturn(message);

        sendEmailService.sendEmailConfirmRegister(player);

        verify(emailSender).sendEmail(false, FROM, SUBJECT, "Name John Silva Email john.silva@email.com Code activation_code", player.getEmail());
    }

    @Test
    public void givenPlayer_WhenSendEmailPasswordReset_ThenSendEmail() {
        String message = "Name {0} Email {1} Code {2}";
        String[] args = { player.getName(), player.getEmail(), player.getPasswordReset() };

        when(emailMessageBuilder.getEmailFrom(2)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(2, args)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(2, args)).thenReturn(message);

        sendEmailService.sendEmailPasswordReset(player);

        verify(emailSender).sendEmail(false, FROM, SUBJECT, "Name John Silva Email john.silva@email.com Code password_reset_code", player.getEmail());
    }

    @Test
    public void givenPlayer_WhenSendEmailPaymentRegistered_ThenSendEmail() {
        String message = "Name {0} Quantity {1}";
        String[] args = { player.getName(), "50" };

        when(emailMessageBuilder.getEmailFrom(3)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(3, args)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(3, args)).thenReturn(message);

        sendEmailService.sendEmailPaymentRegistered(player, 50);

        verify(emailSender).sendEmail(false, FROM, SUBJECT, "Name John Silva Quantity 50", player.getEmail());
    }

    @Test
    public void givenPlayerAndClassPlayer_WhenSendEmailInvitationToClass_ThenSendEmail() {
        Player teacher = new Player();
        teacher.setName("Mary Jane");

        ClassPlayer classPlayer = new ClassPlayer();
        classPlayer.setPlayer(player);
        Class clazz = new Class();
        clazz.setName("Guitar Pro");
        classPlayer.setClazz(clazz);

        String message = "Student {0}, Teacher {1}, Class {2}";
        String[] args = { player.getName(), teacher.getName(), clazz.getName() };

        when(emailMessageBuilder.getEmailFrom(4)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(4, args)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(4, args)).thenReturn(message);

        sendEmailService.sendEmailInvitationToClass(teacher, classPlayer);

        verify(emailSender).sendEmail(false, FROM, SUBJECT, "Student John Silva, Teacher Mary Jane, Class Guitar Pro", classPlayer.getPlayer().getEmail());
    }

    @Test
    public void givenEmailAndMessage_WhenSendEmailContact_ThenSendEmail() {
        String senderEmail = "user@email.com";
        String senderMessage = "Hello World";

        String message = "Sender email: {0}, Message: {1}";
        String[] args = { senderEmail, senderMessage };

        when(emailMessageBuilder.getEmailFrom(5)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(5, args)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(5, args)).thenReturn(message);

        sendEmailService.setEmailGeneralTo(EMAIL_GENERAL_TO);
        sendEmailService.sendEmailContact(senderEmail, senderMessage);

        verify(emailSender).sendEmail(false, FROM, SUBJECT, "Sender email: user@email.com, Message: Hello World", EMAIL_GENERAL_TO);
    }

    @Test
    public void givenGroupCommunicationId4AndPlayers_WhenSendEmailCommunication_ThenSendEmail() {
        int groupCommunicationId = 4;

        Player player1 = new Player();
        player1.setName("Alessandro");
        player1.setCredit(50);
        player1.setScore(1500);

        Player player2 = new Player();
        player2.setName("Orlando");
        player2.setIdFacebook(12345L);
        player2.setCredit(100);
        player2.setScore(7000);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        String message = "Name {0}, Credit {2}, Score {3}, Rank {4}";
        String[] args1 = { player1.getName(), null, "50", "1500", "(2) Faixa Amarela" };
        String[] args2 = { player2.getName(), String.valueOf(player2.getIdFacebook()), "100", "7000", "(5) Faixa Roxa" };

        when(emailMessageBuilder.getEmailFrom(104)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(104, args1)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(104, args1)).thenReturn(message);

        when(emailMessageBuilder.getEmailFrom(104)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(104, args2)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(104, args2)).thenReturn(message);

        sendEmailService.sendEmailCommunication(groupCommunicationId, players);

        verify(emailSender).sendEmail(true, FROM, SUBJECT, "Name Alessandro, Credit 50, Score 1500, Rank (2) Faixa Amarela", player1.getEmail());
        verify(emailSender).sendEmail(true, FROM, SUBJECT, "Name Orlando, Credit 100, Score 7000, Rank (5) Faixa Roxa", player2.getEmail());
    }

    @Test
    public void givenGroupCommunicationId5AndPlayers_WhenSendEmailCommunication_ThenSendEmail() {
        int groupCommunicationId = 5;

        Player player1 = new Player();
        player1.setName("Leandro");
        player1.setCredit(50);
        player1.setScore(1500);

        Player player2 = new Player();
        player2.setName("Beatriz");
        player2.setIdFacebook(12345L);
        player2.setCredit(100);
        player2.setScore(7000);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        String message = "Name {0}, Credit {2}, Score {3}, Rank {4}";
        String[] args1 = { player1.getName(), null, "50", "1500", "(2) Faixa Amarela" };
        String[] args2 = { player2.getName(), String.valueOf(player2.getIdFacebook()), "100", "7000", "(5) Faixa Roxa" };

        when(emailMessageBuilder.getEmailFrom(105)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(105, args1)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(105, args1)).thenReturn(message);

        when(emailMessageBuilder.getEmailFrom(105)).thenReturn(FROM);
        when(emailMessageBuilder.getEmailSubject(105, args2)).thenReturn(SUBJECT);
        when(emailMessageBuilder.getEmailMessage(105, args2)).thenReturn(message);

        sendEmailService.sendEmailCommunication(groupCommunicationId, players);

        verify(emailSender).sendEmail(true, FROM, SUBJECT, "Name Leandro, Credit 50, Score 1500, Rank (2) Faixa Amarela", player1.getEmail());
        verify(emailSender).sendEmail(true, FROM, SUBJECT, "Name Beatriz, Credit 100, Score 7000, Rank (5) Faixa Roxa", player2.getEmail());
    }
}
