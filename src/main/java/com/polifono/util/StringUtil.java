package com.polifono.util;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.annotation.Nonnull;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class StringUtil {

	public static void main(String args[]) {
		//System.out.print(formatNamePlayer("FLÁVIO MOISÉS DE ARAÚJO"));
		//System.out.print(formatNamePlayer("FlávIO MoiSÉS De AraÚJO"));
		
		System.out.println(formatYoutubeUrl("https://www.youtube.com/watch?v="));
		System.out.println(formatYoutubeUrl("https://www.youtube.com/watch?v=jCJhLO90DEU"));
		System.out.println(formatYoutubeUrl("https://youtu.be/jCJhLO90DEU"));
		System.out.println(formatYoutubeUrl("https://m.youtube.com/watch?v=jCJhLO90DEU"));
		System.out.println(formatYoutubeUrl("https://m.youtube.com/watch?v=jCJhLO90DEU&t=13sv"));
		System.out.println(formatYoutubeUrl("https://www.youtube.com/watch?v=jCJhLO90DEU&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMIqPATbDhrb4&start_radio=1"));
		System.out.println(formatYoutubeUrl("https://ankushs92.github.io/tutorial/2016/05/03/pagination-with-spring-boot.html"));
	}
	
	public static String encryptPassword(@Nonnull final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
        return BCrypt.hashpw(rawPassword, salt);
    }
	
	/**
	 * Format the name of the player at the moment of the registration.
	 * From: TESTE DA SILVA
	 * To: Teste da Silva
	 * 
	 * @param name
	 * @return
	 */
	public static String formatNamePlayer(String name) {
		
		if (name == null || "".equals(name)) {
			return name;
		}
		
		name = name.toLowerCase();
		String[] tempArray = name.split(" ");
		
		for (int i = 0; i < tempArray.length; i++) {
			if (tempArray[i].length() > 2) {
				tempArray[i] = tempArray[i].substring(0, 1).toUpperCase() + tempArray[i].substring(1).toLowerCase();
			}
		}
		
		name = Arrays.toString(tempArray).replace(", ", " ").replaceAll("[\\[\\]]", "");
		
		return name;
	}
	
	/**
	 * Format the youtube url that the player saved in the database.
	 * 
	 * The goal is to return: https://www.youtube.com/embed/jCJhLO90DEU
	 * 
	 * urlOriginal must be one of the following options:
	 * 1) https://www.youtube.com/watch?v=jCJhLO90DEU
	 * 2) https://youtu.be/jCJhLO90DEU
	 * 3) https://m.youtube.com/watch?v=jCJhLO90DEU
	 * 
	 * It's not a problem to have parameter after the url:
	 * a) https://m.youtube.com/watch?v=jCJhLO90DEU&t=13s
	 * b) https://www.youtube.com/watch?v=jCJhLO90DEU&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMIqPATbDhrb4&start_radio=1
	 * 
	 * In this case, the parameters should be ignored.
	 * 
	 * If the url is not one of the options above, it will be ignored and the method will return an empty string.
	 * 
	 * @param urlOriginal
	 * @return
	 */
	public static String formatYoutubeUrl(String urlOriginal) {
		
		if (urlOriginal == null || "".equals(urlOriginal)) {
			return "";
		}
		
		String url = "", v = "";
		
		if (urlOriginal.startsWith("https://www.youtube.com/watch?v=")) {
			v = urlOriginal.substring(urlOriginal.indexOf("?v=") + 3);
			
			if (v.contains("&")) {
				v = v.substring(0, v.indexOf("&"));
			}
		}
		else if (urlOriginal.startsWith("https://youtu.be/")) {
			v = urlOriginal.substring(urlOriginal.indexOf(".be/") + 4);
			
			if (v.contains("&")) {
				v = v.substring(0, v.indexOf("&"));
			}
		}
		else if (urlOriginal.startsWith("https://m.youtube.com/watch?v=")) {
			v = urlOriginal.substring(urlOriginal.indexOf("?v=") + 3);
			
			if (v.contains("&")) {
				v = v.substring(0, v.indexOf("&"));
			}
		}
		
		if (!"".equals(v)) {
			url = "https://www.youtube.com/embed/" + v;
		}
		
		return url;
	}
}