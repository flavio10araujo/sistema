package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlayerUtilTest {

    @Test
    public void givenName_whenFormatNamePlayer_thenReturnFormattedName() {
        String expected = "Flávio Moisés de Araújo";

        assertEquals(expected, PlayerUtil.formatNamePlayer("FLÁVIO MOISÉS DE ARAÚJO"));
        assertEquals(expected, PlayerUtil.formatNamePlayer("flávio moisés de araújo"));
        assertEquals(expected, PlayerUtil.formatNamePlayer("fláViO moIsés dE araújo"));
    }

    @Test
    public void givenEmptyName_whenFormatNamePlayer_thenReturnEmptyName() {
        String name = "";
        String expected = "";

        String result = PlayerUtil.formatNamePlayer(name);

        assertEquals(expected, result);
    }
}
