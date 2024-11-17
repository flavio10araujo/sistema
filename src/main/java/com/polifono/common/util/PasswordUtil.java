package com.polifono.common.util;

import java.security.SecureRandom;

import javax.annotation.Nonnull;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String encryptPassword(@Nonnull final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
        return BCrypt.hashpw(rawPassword, salt);
    }
}
