package com.polifono.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static void main (String[] args) {
		System.out.println(md5Hex("flavio10araujo@gmail.com"));
	}

	public static String hex(byte[] array) {
		StringBuilder sb = new StringBuilder();

        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }

		return sb.toString();
	}

	public static String md5Hex(String message) {
		try {
			MessageDigest md =  MessageDigest.getInstance("MD5");
			return hex(md.digest(message.getBytes("CP1252")));
		}
		catch (NoSuchAlgorithmException e) {}
		catch (UnsupportedEncodingException e) {}

		return null;
	}
}
