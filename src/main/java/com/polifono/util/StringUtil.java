package com.polifono.util;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.annotation.Nonnull;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class StringUtil {

    public static void main(String args[]) {
        //System.out.print(formatNamePlayer("FLÁVIO MOISÉS DE ARAÚJO"));
        //System.out.print(formatNamePlayer("FlávIO MoiSÉS De AraÚJO"));
    }

    public static String encryptPassword(@Nonnull final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
        return BCrypt.hashpw(rawPassword, salt);
    }

    /**
     * Format the name of the player at the moment of the registration.
     * From: TESTE DA SILVA
     * To: Teste da Silva
     */
    public static String formatNamePlayer(String name) {
        if (name == null || name.isEmpty()) {
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
}
