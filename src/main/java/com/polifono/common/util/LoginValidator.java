package com.polifono.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator {

    /**
     * Validate a login.
     * Return true if the login is valid. False if the login is not valid.
     * Rules:
     * - size between 6 and 20;
     * - only number and letters;
     * - underline and hyphen are accepted;
     * - no spaces and other special characters.
     */
    public static boolean isValid(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        String PATTERN = "^[a-z0-9_-]{6,20}$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
