package com.polifono.service.player;

import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.polifono.model.entity.Player;

@ExtendWith(MockitoExtension.class)
public class PlayerHandlerTest {

    @InjectMocks
    private PlayerHandler service;

    @Mock
    private MessageSource messagesResource;

    /* validateCreatePlayer - begin */
    @Test
    public void validateCreatePlayer_WhenNoDataIsMissing_ReturnMsgEmpty() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("password123");

        Assertions.assertEquals("", service.validateCreatePlayer(player, Locale.ENGLISH), "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
        Player player = new Player();
        player.setName(null);
        player.setEmail("email@test.com");
        player.setPassword("password123");

        when(messagesResource.getMessage("msg.register.missingName", null, Locale.ENGLISH)).thenReturn("O nome precisa ser informado.");

        Assertions.assertEquals("<br />O nome precisa ser informado.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");

        player.setName("");
        player.setEmail("email@test.com");
        player.setPassword("password123");

        Assertions.assertEquals("<br />O nome precisa ser informado.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenFieldEmailIsMissing_ReturnMsgEmailMissing() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail(null);
        player.setPassword("password123");

        when(messagesResource.getMessage("msg.register.missingEmail", null, Locale.ENGLISH)).thenReturn("O e-mail precisa ser informado.");

        Assertions.assertEquals("<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");

        player.setName("Name Completed");
        player.setEmail("");
        player.setPassword("password123");

        Assertions.assertEquals("<br />O e-mail precisa ser informado.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenFieldEmailIsInvalid_ReturnMsgEmailInvalid() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("invalid_email");
        player.setPassword("password123");

        when(messagesResource.getMessage("msg.register.invalidEmail", null, Locale.ENGLISH)).thenReturn("O e-mail informado não é válido.");

        Assertions.assertEquals("<br />O e-mail informado não é válido.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsMissing_ReturnMsgPasswordMissing() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword(null);

        when(messagesResource.getMessage("msg.register.missingPassword", null, Locale.ENGLISH)).thenReturn("A senha precisa ser informada.");

        Assertions.assertEquals("<br />A senha precisa ser informada.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");

        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("");

        Assertions.assertEquals("<br />A senha precisa ser informada.", service.validateCreatePlayer(player, Locale.ENGLISH),
                "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenFieldPasswordIsInvalid_ReturnMsgPasswordInvalid() {
        Player player = new Player();
        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("12345");

        when(messagesResource.getMessage("msg.register.invalidPassword.size", null, Locale.ENGLISH)).thenReturn(
                "A senha precisa possuir entre 6 e 20 caracteres.");
        when(messagesResource.getMessage("msg.register.invalidPassword.pattern", null, Locale.ENGLISH)).thenReturn(
                "A senha precisa possuir ao menos 1 número e ao menos 1 letra.");

        Assertions.assertEquals("<br />A senha precisa possuir entre 6 e 20 caracteres.",
                service.validateCreatePlayer(player, Locale.ENGLISH), "failure - expected msg returned equals");

        player.setName("Name Completed");
        player.setEmail("email@test.com");
        player.setPassword("123456");

        Assertions.assertEquals("<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.",
                service.validateCreatePlayer(player, Locale.ENGLISH), "failure - expected msg returned equals");
    }

    @Test
    public void validateCreatePlayer_WhenMoreThanOneFielsIsMissing_ReturnMsgFieldsMissing() {
        Player player = new Player();
        player.setName("");
        player.setEmail("");
        player.setPassword("");

        when(messagesResource.getMessage("msg.register.missingName", null, Locale.ENGLISH)).thenReturn("O nome precisa ser informado.");
        when(messagesResource.getMessage("msg.register.missingEmail", null, Locale.ENGLISH)).thenReturn("O e-mail precisa ser informado.");
        when(messagesResource.getMessage("msg.register.missingPassword", null, Locale.ENGLISH)).thenReturn("A senha precisa ser informada.");

        String msg = "<br />O nome precisa ser informado.<br />O e-mail precisa ser informado.<br />A senha precisa ser informada.";

        Assertions.assertEquals(msg, service.validateCreatePlayer(player, Locale.ENGLISH), "failure - expected msg returned equals");
    }
    /* validateCreatePlayer - end */

    /* validateUpdateProfile - begin */
    /*@Test
    public void validateUpdateProfile_WhenNoDataIsMissing_ReturnMsgEmpty() {
        Player player = new Player();
        player.setName("Name Completed");
        Assertions.assertEquals("failure - expected msg returned equals", "", service.validateUpdateProfile(player));
    }*/

    /*@Test
    public void validateUpdateProfile_WhenFieldNameIsMissing_ReturnMsgNameMissing() {
        Player player = new Player();
        player.setName(null);
        Assertions.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));

        player.setName("");
        Assertions.assertEquals("failure - expected msg returned equals", "O nome precisa ser informado.<br />", service.validateUpdateProfile(player));
    }*/
    /* validateUpdateProfile - end */
}
