package com.polifono.common.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStringGenerator {

    /**
     * Generates a random string of the specified length.
     *
     * @return a random string
     */
    public static String generate(int length) {
        if (length < 1) {
            return "";
        }

        char[] symbols = getSymbols();
        char[] buf = new char[length];

        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[ThreadLocalRandom.current().nextInt(symbols.length)];
        }

        return new String(buf);
    }

    /**
     * Get the symbols array with digits and lowercase letters.
     *
     * @return the initialized symbols array
     */
    private static char[] getSymbols() {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) {
            tmp.append(ch);
        }

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            tmp.append(ch);
        }

        return tmp.toString().toCharArray();
    }
}
