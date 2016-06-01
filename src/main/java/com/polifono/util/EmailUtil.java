package com.polifono.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

	/**
	 * Validate an email.
	 * Return true if the email is valid. False if the email is not valid.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String s) {
		if (s == null || "".equals(s)) {
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
	 * 
	 * @param password
	 * @return
	 */
	public static boolean validatePassword(String s) {
		if (s == null || "".equals(s)) {
			return false;
		}
		
        String PATTERN = "\\S*(\\S*([a-zA-Z]\\S*[0-9])|([0-9]\\S*[a-zA-Z]))\\S*";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}
}