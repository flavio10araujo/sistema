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

		/*System.out.println(avoidWrongDomain("test1@bol.com"));
		System.out.println(avoidWrongDomain("test1@bol.com.br"));
		System.out.println(avoidWrongDomain("test1@gmail.com"));
		System.out.println(avoidWrongDomain("test1@gmail.com.br"));
		System.out.println(avoidWrongDomain("test1@yahoo.com"));
		System.out.println(avoidWrongDomain("test1@yahoo.com.br"));
		System.out.println(avoidWrongDomain("test1@hotmail.com"));
		System.out.println(avoidWrongDomain("test1@hotmail.com.br"));
		System.out.println(avoidWrongDomain("test1@outlook.com"));
		System.out.println(avoidWrongDomain("test1@outlook.com.br"));*/

		System.out.println(avoidWrongDomain("test1@gmail.co"));
		System.out.println(avoidWrongDomain("test1@gmail.comm"));
		System.out.println(avoidWrongDomain("test1@gamail.com"));
		System.out.println(avoidWrongDomain("test1@gamil.com"));
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

	public static String avoidWrongDomain(String email) {
		
		//System.out.print(email + " => ");
		
		if (email == null || "".equals(email)) {
			return email;
		}

		int indexAt = email.indexOf("@");

		if (indexAt < 0) {
			return email;
		}

		email = email.toLowerCase();
		String domain = email.substring(indexAt); //System.out.println("Domain = [" + domain + "]");

		String gmailCom = "@gmail.com";
		String hotmailCom = "@hotmail.com";
		String hotmailComBr = "@hotmail.com.br";
		String yahooComBr = "@yahoo.com.br";
		String yahooCom = "@yahoo.com";
		String bolComBr = "@bol.com.br";
		String outlookCom = "@outlook.com";

		//gmail.com
		if (domain.equals("@gmail.com.br")) {
			return email.replaceAll("@gmail.com.br", gmailCom);
		}
		if (domain.equals("@gmail.co")) {
			return email.replaceAll("@gmail.co", gmailCom);
		}
		if (domain.equals("@gmail.comm")) {
			return email.replaceAll("@gmail.comm", gmailCom);
		}
		if (domain.equals("@gamail.com")) {
			return email.replaceAll("@gamail.com", gmailCom);
		}
		if (domain.equals("@gamil.com")) {
			return email.replaceAll("@gamil.com", gmailCom);
		}
		if (domain.equals("@gemeil.com")) {
			return email.replaceAll("@gemeil.com", gmailCom);
		}
		if (domain.equals("@gemil.com")) {
			return email.replaceAll("@gemil.com", gmailCom);
		}
		if (domain.equals("@gimal.com")) {
			return email.replaceAll("@gimal.com", gmailCom);
		}
		if (domain.equals("@gmai.com")) {
			return email.replaceAll("@gmai.com", gmailCom);
		}
		if (domain.equals("@gmaiil.com")) {
			return email.replaceAll("@gmaiil.com", gmailCom);
		}
		if (domain.equals("@gmaio.com")) {
			return email.replaceAll("@gmaio.com", gmailCom);
		}
		if (domain.equals("@gmakl.com")) {
			return email.replaceAll("@gmakl.com", gmailCom);
		}
		if (domain.equals("@gmeil.com")) {
			return email.replaceAll("@gmeil.com", gmailCom);
		}
		if (domain.equals("@gmil.com")) {
			return email.replaceAll("@gmil.com", gmailCom);
		}
		if (domain.equals("@gmial.com")) {
			return email.replaceAll("@gmial.com", gmailCom);
		}
		if (domain.equals("@gnail.com")) {
			return email.replaceAll("@gnail.com", gmailCom);
		}
		if (domain.equals("@g-mail.com")) {
			return email.replaceAll("@g-mail.com", gmailCom);
		}

		//hotmail.com
		if (domain.equals("@hotmail.co")) {
			return email.replaceAll("@hotmail.co", hotmailCom);
		}
		if (domain.equals("@hotmail.comm")) {
			return email.replaceAll("@hotmail.comm", hotmailCom); 
		}
		if (domain.equals("@hitmail.com")) {
			return email.replaceAll("@hitmail.com", hotmailCom);
		}
		if (domain.equals("@homail.com")) {
			return email.replaceAll("@homail.com", hotmailCom);
		}
		if (domain.equals("@hot.com")) {
			return email.replaceAll("@hot.com", hotmailCom);
		}
		if (domain.equals("@hotail.com")) {
			return email.replaceAll("@hotail.com", hotmailCom);
		}
		if (domain.equals("@hotamil.com")) {
			return email.replaceAll("@hotamil.com", hotmailCom);
		}
		if (domain.equals("@hotimail.com")) {
			return email.replaceAll("@hotimail.com", hotmailCom);
		}
		if (domain.equals("@hotmaeil.com")) {
			return email.replaceAll("@hotmaeil.com", hotmailCom);
		}
		if (domain.equals("@hotmayl.com")) {
			return email.replaceAll("@hotmayl.com", hotmailCom);
		}
		if (domain.equals("@hotmsil.com")) {
			return email.replaceAll("@hotmsil.com", hotmailCom);
		}
		if (domain.equals("@hptmail.com")) {
			return email.replaceAll("@hptmail.com", hotmailCom);
		}
		if (domain.equals("@htmail.com")) {
			return email.replaceAll("@htmail.com", hotmailCom);
		}
		if (domain.equals("@rotmail.com")) {
			return email.replaceAll("@rotmail.com", hotmailCom);
		}
		if (domain.equals("@hotmil.com")) {
			return email.replaceAll("@hotmil.com", hotmailCom);
		}

		//hotmail.com.br
		if (domain.equals("@hitmail.com.br")) {
			return email.replaceAll("@hitmail.com.br", hotmailComBr);
		}
		if (domain.equals("@homail.com.br")) {
			return email.replaceAll("@homail.com.br", hotmailComBr);
		}
		if (domain.equals("@hot.com.br")) {
			return email.replaceAll("@hot.com.br", hotmailComBr);
		}
		if (domain.equals("@hotail.com.br")) {
			return email.replaceAll("@hotail.com.br", hotmailComBr);
		}
		if (domain.equals("@hotamil.com.br")) {
			return email.replaceAll("@hotamil.com.br", hotmailComBr);
		}
		if (domain.equals("@hotimail.com.br")) {
			return email.replaceAll("@hotimail.com.br", hotmailComBr);
		}
		if (domain.equals("@hotmaeil.com.br")) {
			return email.replaceAll("@hotmaeil.com.br", hotmailComBr);
		}
		if (domain.equals("@hotmayl.com.br")) {
			return email.replaceAll("@hotmayl.com.br", hotmailComBr);
		}
		if (domain.equals("@hotmsil.com.br")) {
			return email.replaceAll("@hotmsil.com.br", hotmailComBr);
		}
		if (domain.equals("@hptmail.com.br")) {
			return email.replaceAll("@hptmail.com.br", hotmailComBr);
		}
		if (domain.equals("@htmail.com.br")) {
			return email.replaceAll("@htmail.com.br", hotmailComBr);
		}
		if (domain.equals("@rotmail.com.br")) {
			return email.replaceAll("@rotmail.com.br", hotmailComBr);
		}
		if (domain.equals("@hotmil.com.br")) {
			return email.replaceAll("@hotmil.com.br", hotmailComBr);
		}

		//yahoo.com.br
		if (domain.equals("@yaho.com.br")) {
			return email.replaceAll("@yaho.com.br", yahooComBr);
		}
		if (domain.equals("@yhoo.com.br")) {
			return email.replaceAll("@yhoo.com.br", yahooComBr);
		}
		if (domain.equals("@uahoo.com.br")) {
			return email.replaceAll("@uahoo.com.br", yahooComBr);
		}
		
		//yahoo.com
		if (domain.equals("@yaho.com")) {
			return email.replaceAll("@yaho.com", yahooCom);
		}
		if (domain.equals("@yhoo.com")) {
			return email.replaceAll("@yhoo.com", yahooCom);
		}
		if (domain.equals("@uahoo.com")) {
			return email.replaceAll("@uahoo.com", yahooCom);
		}

		//bol.com.br
		if (domain.equals("@boll.com.br")) {
			return email.replaceAll("@boll.com.br", bolComBr);
		}				
		if (domain.equals("@bl.com.br")) {
			return email.replaceAll("@bl.com.br", bolComBr);
		}

		//outlook.com
		if (domain.equals("@outllok.com")) {
			return email.replaceAll("@outllok.com", outlookCom);
		}
		if (domain.equals("@outllok.com")) {
			return email.replaceAll("@outllok.com", outlookCom);
		}
		if (domain.equals("@outluoock.com")) {
			return email.replaceAll("@outluoock.com", outlookCom);
		}

		return email;
	}
}