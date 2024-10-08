package com.polifono.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HashingUtilTest {

    @Test
    public void givenEmptyString_WhenGenerateMD5Hash_ThenReturnExpectedHash() {
        String expectedHash = "";
        String actualHash = HashingUtil.generateMD5Hash("");
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void givenNullString_WhenGenerateMD5Hash_ThenReturnNull() {
        String expectedHash = "";
        String actualHash = HashingUtil.generateMD5Hash(null);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void givenString_WhenGenerateMD5Hash_ThenReturnExpectedHash() {
        String expectedHash = "4c0cd4d99b6086f9174315bbfbb103e9";
        String actualHash = HashingUtil.generateMD5Hash("flavio@email.com");
        assertEquals(expectedHash, actualHash);
    }
}
