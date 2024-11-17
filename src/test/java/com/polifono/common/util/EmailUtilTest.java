package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class EmailUtilTest {

    @Test
    public void givenEmailWithCorrectDomain_WhenCorrectDomainThenReturnSameEmail() {
        assertEquals("test1@gmail.com", EmailDomainCorrector.correctDomain("test1@gmail.com"));
        assertEquals("test1@hotmail.com", EmailDomainCorrector.correctDomain("test1@hotmail.com"));
        assertEquals("test1@yahoo.com", EmailDomainCorrector.correctDomain("test1@yahoo.com"));
    }

    @Test
    public void givenNullEmail_WhenCorrectDomainThenReturnNull() {
        assertNull(EmailDomainCorrector.correctDomain(null));
    }

    @Test
    public void givenEmptyEmail_WhenCorrectDomainThenReturnEmpty() {
        assertEquals("", EmailDomainCorrector.correctDomain(""));
    }

    @Test
    public void givenEmailWithoutAtSymbol_WhenCorrectDomainThenReturnSameEmail() {
        assertEquals("test1gmail.com", EmailDomainCorrector.correctDomain("test1gmail.com"));
    }

    @Test
    public void givenEmailWithWrongDomain_WhenCorrectDomainThenReturnCorrectedEmail() {
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmail.com.br"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmail.co"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmail.comm"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gamail.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gamil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gemeil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gemil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gimal.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmai.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmaiil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmaio.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmakl.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmeil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmil.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gmial.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@gnail.com"));
        assertEquals("email@gmail.com", EmailDomainCorrector.correctDomain("email@g-mail.com"));

        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmail.co"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmail.comm"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hitmail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@homail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hot.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotamil.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotimail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmaeil.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmayl.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmsil.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hptmail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@htmail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@rotmail.com"));
        assertEquals("email@hotmail.com", EmailDomainCorrector.correctDomain("email@hotmil.com"));

        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hitmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@homail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hot.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotamil.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotimail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotmaeil.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotmayl.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotmsil.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hptmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@htmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@rotmail.com.br"));
        assertEquals("email@hotmail.com.br", EmailDomainCorrector.correctDomain("email@hotmil.com.br"));

        assertEquals("email@yahoo.com.br", EmailDomainCorrector.correctDomain("email@yaho.com.br"));
        assertEquals("email@yahoo.com.br", EmailDomainCorrector.correctDomain("email@yhoo.com.br"));
        assertEquals("email@yahoo.com.br", EmailDomainCorrector.correctDomain("email@uahoo.com.br"));

        assertEquals("email@yahoo.com", EmailDomainCorrector.correctDomain("email@yaho.com"));
        assertEquals("email@yahoo.com", EmailDomainCorrector.correctDomain("email@yhoo.com"));
        assertEquals("email@yahoo.com", EmailDomainCorrector.correctDomain("email@uahoo.com"));

        assertEquals("email@bol.com.br", EmailDomainCorrector.correctDomain("email@boll.com.br"));
        assertEquals("email@bol.com.br", EmailDomainCorrector.correctDomain("email@bl.com.br"));

        assertEquals("email@outlook.com", EmailDomainCorrector.correctDomain("email@outllok.com"));
        assertEquals("email@outlook.com", EmailDomainCorrector.correctDomain("email@outllok.com"));
        assertEquals("email@outlook.com", EmailDomainCorrector.correctDomain("email@outluoock.com"));
    }
}
