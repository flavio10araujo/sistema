package com.polifono.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

	public static void main(String[] args) {
		
		/*String senha = "F123456";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}
		
		senha = "123456";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}
		
		senha = "AAAAAA";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}
		
		senha = "F123 456";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}
		
		senha = "F123*456";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}
		
		senha = "F123(¨%*&*456";
		
		if (validatePassword(senha)) {
			System.out.println(senha + " é SENHA BOA");
		}
		else {
			System.out.println(senha + " é SENHA RUIM");
		}*/
		
		/*String login = "flavio"; // OK
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "1234567890123456"; // OK
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "flavio10araujo"; // OK
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "flavio_araujo"; // ERROR
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "flavio araujo"; // ERROR
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "flavio*araujo"; // ERROR
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "flávio"; // ERROR
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}
		
		login = "acão"; // ERROR
		
		if (validateLogin(login)) {
			System.out.println(login + " é LOGIN BOM");
		}
		else {
			System.out.println(login + " é LOGIN RUIM");
		}*/
	}
	
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
	 * Rules:
	 * - at least one number;
	 * - at least one letter;
	 * - no spaces.
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
	
	/**
	 * Validate a login.
	 * Return true if the login is valid. False if the login is not valid.
	 * Rules:
	 * - size between 6 and 20;
	 * - only number and letters;
	 * - underline is accepted;
	 * - no spaces and other special characters.
	 * 
	 * @param password
	 * @return
	 */
	public static boolean validateLogin(String s) {
		if (s == null || "".equals(s)) {
			return false;
		}
		
        String PATTERN = "^[a-z0-9_-]{6,20}$";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}
}