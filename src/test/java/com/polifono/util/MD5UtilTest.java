package com.polifono.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MD5UtilTest {

    @Test
    public void givenEmptyString_WhenMd5Hex_ThenReturnExpectedHash() {
        String expectedHash = "";
        String actualHash = MD5Util.md5Hex("");
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void givenNullString_WhenMd5Hex_ThenReturnNull() {
        String expectedHash = "";
        String actualHash = MD5Util.md5Hex(null);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void givenString_WhenMd5Hex_ThenReturnExpectedHash() {
        String expectedHash = "4c0cd4d99b6086f9174315bbfbb103e9";
        String actualHash = MD5Util.md5Hex("flavio@email.com");
        assertEquals(expectedHash, actualHash);
    }
}
