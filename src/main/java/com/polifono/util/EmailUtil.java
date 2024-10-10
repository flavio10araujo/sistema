package com.polifono.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    private static final Map<String, String> DOMAIN_CORRECTIONS = new HashMap<>();

    static {
        DOMAIN_CORRECTIONS.put("@gmail.com.br", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.co", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.comm", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gamail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gamil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemeil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gimal.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmai.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaiil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaio.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmakl.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmeil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmial.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gnail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@g-mail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.cm", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmal.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaill.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.om", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.org", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.comk", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.ccom", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmais.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gma.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.vom", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@fmail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@hmail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@hotmail.co", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmail.comm", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hitmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@homail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hot.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotamil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotimail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaeil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmayl.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmsil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hptmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@htmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@rotmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotnail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmal.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaill.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmailk.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hormail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaol.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@jotmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmai.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hitmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@homail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hot.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotamil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotimail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmaeil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmayl.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmsil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hptmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@htmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@rotmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@yaho.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yhoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@uahoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yahoo.co.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yahoo.vom.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@ahoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yaho.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@yhoo.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@uahoo.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@boll.com.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@bl.com.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@bol.co.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@outllok.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@outluoock.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@otlook.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@aiclod.com", "@icloud.com");
    }

    /**
     * Validate an email.
     * Return true if the email is valid. False if the email is not valid.
     */
    public static boolean validateEmail(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * Validate a password.
     * Return true if the password is valid. False if the password is not valid.
     * Rules:
     * - size between 6 and 20;
     * - at least one number;
     * - at least one letter;
     * - no spaces.
     */
    public static boolean validatePassword(String s) {
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

    /**
     * Validate a login.
     * Return true if the login is valid. False if the login is not valid.
     * Rules:
     * - size between 6 and 20;
     * - only number and letters;
     * - underline and hyphen are accepted;
     * - no spaces and other special characters.
     */
    public static boolean validateLogin(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        String PATTERN = "^[a-z0-9_-]{6,20}$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static String avoidWrongDomain(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        int indexAt = email.indexOf("@");

        if (indexAt < 0) {
            return email;
        }

        email = email.toLowerCase();
        String domain = email.substring(indexAt);

        if (DOMAIN_CORRECTIONS.containsKey(domain)) {
            return email.replaceAll(domain, DOMAIN_CORRECTIONS.get(domain));
        }

        return email;
    }
}
