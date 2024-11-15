package com.polifono.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    /**
     * Validate a password.
     * Return true if the password is valid. False if the password is not valid.
     * Rules:
     * - size between 6 and 20;
     * - at least one number;
     * - at least one letter;
     * - no spaces.
     */
    public static boolean isValid(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        if (s.length() < 6 || s.length() > 20) {
            return false;
        }

        String PATTERN = "\\S*(\\S*([a-zA-Z]\\S*[0-9])|([0-9]\\S*[a-zA-Z]))\\S*";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
