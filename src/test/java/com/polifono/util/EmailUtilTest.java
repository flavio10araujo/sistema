package com.polifono.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EmailUtilTest {

    @Test
    public void givenValidEmail_WhenValidateEmail_ThenReturnTrue() {
        assertTrue(EmailUtil.validateEmail("test@example.com"));
        assertTrue(EmailUtil.validateEmail("user.name@example.com"));
        assertTrue(EmailUtil.validateEmail("user_name@example.co.uk"));
    }

    @Test
    public void givenInvalidEmail_WhenValidateEmail_ThenReturnFalse() {
        assertFalse(EmailUtil.validateEmail(null));
        assertFalse(EmailUtil.validateEmail(""));
        assertFalse(EmailUtil.validateEmail(" "));
        assertFalse(EmailUtil.validatePassword("a1"));
        assertFalse(EmailUtil.validateEmail("plainaddress"));
        assertFalse(EmailUtil.validateEmail("plainaddress@"));
        assertFalse(EmailUtil.validateEmail("plainaddress@domain"));
        assertFalse(EmailUtil.validateEmail("plainaddress@domain."));
        assertFalse(EmailUtil.validateEmail("plainaddress@domain.c"));
        assertFalse(EmailUtil.validateEmail("plainaddress@domain..com"));
    }

    @Test
    public void givenValidPassword_WhenValidatePassword_ThenReturnTrue() {
        assertTrue(EmailUtil.validatePassword("Pabcd1"));
        assertTrue(EmailUtil.validatePassword("1_#*a&?"));
    }

    @Test
    public void givenInvalidPassword_WhenValidatePassword_ThenReturnFalse() {
        assertFalse(EmailUtil.validatePassword(null));
        assertFalse(EmailUtil.validatePassword(""));
        assertFalse(EmailUtil.validatePassword(" "));
        assertFalse(EmailUtil.validatePassword("password")); // No number.
        assertFalse(EmailUtil.validatePassword("12345678")); // No letter.
        assertFalse(EmailUtil.validatePassword("pass word1")); // Contains space.
        assertFalse(EmailUtil.validatePassword("a1234")); // Smaller than 6 characters.
        assertFalse(EmailUtil.validatePassword("a1234567890b123456780c")); // Bigger than 20 characters.
    }

    @Test
    public void givenValidLogin_WhenValidateLogin_ThenReturnTrue() {
        assertTrue(EmailUtil.validateLogin("flavio10"));
        assertTrue(EmailUtil.validateLogin("user_name"));
        assertTrue(EmailUtil.validateLogin("user-name"));
        assertTrue(EmailUtil.validateLogin("user123"));
    }

    @Test
    public void givenInvalidLogin_WhenValidateLogin_ThenReturnFalse() {
        assertFalse(EmailUtil.validateLogin(null));
        assertFalse(EmailUtil.validateLogin(""));
        assertFalse(EmailUtil.validateLogin(" "));
        assertFalse(EmailUtil.validateLogin("user name")); // Contains space
        assertFalse(EmailUtil.validateLogin("user@name")); // Contains special character
        assertFalse(EmailUtil.validateLogin("user!name")); // Contains special character
        assertFalse(EmailUtil.validateLogin("user")); // Less than 6 characters
        assertFalse(EmailUtil.validateLogin("user12345678901234567890")); // More than 20 characters
    }

    @Test
    public void givenEmailWithCorrectDomain_WhenAvoidWrongDomain_ThenReturnSameEmail() {
        assertEquals("test1@gmail.com", EmailUtil.avoidWrongDomain("test1@gmail.com"));
        assertEquals("test1@hotmail.com", EmailUtil.avoidWrongDomain("test1@hotmail.com"));
        assertEquals("test1@yahoo.com", EmailUtil.avoidWrongDomain("test1@yahoo.com"));
    }

    @Test
    public void givenNullEmail_WhenAvoidWrongDomain_ThenReturnNull() {
        assertNull(EmailUtil.avoidWrongDomain(null));
    }

    @Test
    public void givenEmptyEmail_WhenAvoidWrongDomain_ThenReturnEmpty() {
        assertEquals("", EmailUtil.avoidWrongDomain(""));
    }

    @Test
    public void givenEmailWithoutAtSymbol_WhenAvoidWrongDomain_ThenReturnSameEmail() {
        assertEquals("test1gmail.com", EmailUtil.avoidWrongDomain("test1gmail.com"));
    }

    @Test
    public void givenEmailWithWrongDomain_WhenAvoidWrongDomain_ThenReturnCorrectedEmail() {
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmail.com.br"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmail.co"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmail.comm"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gamail.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gamil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gemeil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gemil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gimal.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmai.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmaiil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmaio.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmakl.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmeil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmil.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gmial.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@gnail.com"));
        assertEquals("email@gmail.com", EmailUtil.avoidWrongDomain("email@g-mail.com"));

        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmail.co"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmail.comm"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hitmail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@homail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hot.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotamil.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotimail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmaeil.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmayl.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmsil.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hptmail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@htmail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@rotmail.com"));
        assertEquals("email@hotmail.com", EmailUtil.avoidWrongDomain("email@hotmil.com"));

        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hitmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@homail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hot.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotamil.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotimail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotmaeil.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotmayl.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotmsil.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hptmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@htmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@rotmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailUtil.avoidWrongDomain("email@hotmil.com.br"));

        assertEquals("email@yahoo.com.br", EmailUtil.avoidWrongDomain("email@yaho.com.br"));
        assertEquals("email@yahoo.com.br", EmailUtil.avoidWrongDomain("email@yhoo.com.br"));
        assertEquals("email@yahoo.com.br", EmailUtil.avoidWrongDomain("email@uahoo.com.br"));

        assertEquals("email@yahoo.com", EmailUtil.avoidWrongDomain("email@yaho.com"));
        assertEquals("email@yahoo.com", EmailUtil.avoidWrongDomain("email@yhoo.com"));
        assertEquals("email@yahoo.com", EmailUtil.avoidWrongDomain("email@uahoo.com"));

        assertEquals("email@bol.com.br", EmailUtil.avoidWrongDomain("email@boll.com.br"));
        assertEquals("email@bol.com.br", EmailUtil.avoidWrongDomain("email@bl.com.br"));

        assertEquals("email@outlook.com", EmailUtil.avoidWrongDomain("email@outllok.com"));
        assertEquals("email@outlook.com", EmailUtil.avoidWrongDomain("email@outllok.com"));
        assertEquals("email@outlook.com", EmailUtil.avoidWrongDomain("email@outluoock.com"));
    }
}
