package com.polifono.util;

import java.util.Arrays;

public class PlayerUtil {

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
