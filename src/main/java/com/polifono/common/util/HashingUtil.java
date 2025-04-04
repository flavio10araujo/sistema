package com.polifono.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtil {

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();

        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }

        return sb.toString();
    }

    public static String generateMD5Hash(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        s = s.toLowerCase();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(s.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return "";
        }
    }
}
