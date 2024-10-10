package com.polifono.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class GenerateRandomStringService {

    /**
     * Get the symbols array with digits and lowercase letters.
     *
     * @return the initialized symbols array
     */
    private char[] getSymbols() {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) {
            tmp.append(ch);
        }

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            tmp.append(ch);
        }

        return tmp.toString().toCharArray();
    }

    /**
     * Generates a random string of the specified length.
     *
     * @return a random string
     */
    public String generate(int length) {
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
}
