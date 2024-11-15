package com.polifono.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    /**
     * Validate an email.
     * Return true if the email is valid. False if the email is not valid.
     */
    public static boolean isValid(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
