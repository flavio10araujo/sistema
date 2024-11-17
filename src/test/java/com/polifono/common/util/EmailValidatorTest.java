package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EmailValidatorTest {

    @Test
    public void givenValidEmail_WhenValidateEmail_ThenReturnTrue() {
        assertTrue(EmailValidator.isValid("test@example.com"));
        assertTrue(EmailValidator.isValid("user.name@example.com"));
        assertTrue(EmailValidator.isValid("user_name@example.co.uk"));
    }

    @Test
    public void givenInvalidEmail_WhenValidateEmail_ThenReturnFalse() {
        assertFalse(EmailValidator.isValid(null));
        assertFalse(EmailValidator.isValid(""));
        assertFalse(EmailValidator.isValid(" "));
        assertFalse(EmailValidator.isValid("a1"));
        assertFalse(EmailValidator.isValid("plainaddress"));
        assertFalse(EmailValidator.isValid("plainaddress@"));
        assertFalse(EmailValidator.isValid("plainaddress@domain"));
        assertFalse(EmailValidator.isValid("plainaddress@domain."));
        assertFalse(EmailValidator.isValid("plainaddress@domain.c"));
        assertFalse(EmailValidator.isValid("plainaddress@domain..com"));
    }
}
