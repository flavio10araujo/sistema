package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

    @Test
    public void givenValidPassword_WhenValidatePassword_ThenReturnTrue() {
        assertTrue(PasswordValidator.isValid("Pabcd1"));
        assertTrue(PasswordValidator.isValid("1_#*a&?"));
    }

    @Test
    public void givenInvalidPassword_WhenValidatePassword_ThenReturnFalse() {
        assertFalse(PasswordValidator.isValid(null));
        assertFalse(PasswordValidator.isValid(""));
        assertFalse(PasswordValidator.isValid(" "));
        assertFalse(PasswordValidator.isValid("password")); // No number.
        assertFalse(PasswordValidator.isValid("12345678")); // No letter.
        assertFalse(PasswordValidator.isValid("pass word1")); // Contains space.
        assertFalse(PasswordValidator.isValid("a1234")); // Smaller than 6 characters.
        assertFalse(PasswordValidator.isValid("a1234567890b123456780c")); // Bigger than 20 characters.
    }
}
