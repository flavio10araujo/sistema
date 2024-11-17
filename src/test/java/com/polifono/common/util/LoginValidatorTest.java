package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LoginValidatorTest {

    @Test
    public void givenValidLogin_WhenValidateLogin_ThenReturnTrue() {
        assertTrue(LoginValidator.isValid("flavio10"));
        assertTrue(LoginValidator.isValid("user_name"));
        assertTrue(LoginValidator.isValid("user-name"));
        assertTrue(LoginValidator.isValid("user123"));
    }

    @Test
    public void givenInvalidLogin_WhenValidateLogin_ThenReturnFalse() {
        assertFalse(LoginValidator.isValid(null));
        assertFalse(LoginValidator.isValid(""));
        assertFalse(LoginValidator.isValid(" "));
        assertFalse(LoginValidator.isValid("user name")); // Contains space
        assertFalse(LoginValidator.isValid("user@name")); // Contains special character
        assertFalse(LoginValidator.isValid("user!name")); // Contains special character
        assertFalse(LoginValidator.isValid("user")); // Less than 6 characters
        assertFalse(LoginValidator.isValid("user12345678901234567890")); // More than 20 characters
    }
}
