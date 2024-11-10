package com.polifono.model.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiplomaTest {

    @Test
    public void givenLevel_whenGetQtdHours_thenReturnCorrectHours() {
        Diploma diploma = new Diploma();
        diploma.setLevel(new Level());
        diploma.getLevel().setOrder(1);
        assert diploma.getQtdHours() == 15;
        diploma.getLevel().setOrder(2);
        assert diploma.getQtdHours() == 15;
        diploma.getLevel().setOrder(3);
        assert diploma.getQtdHours() == 15;
        diploma.getLevel().setOrder(4);
        assert diploma.getQtdHours() == 15;
        diploma.getLevel().setOrder(5);
        assert diploma.getQtdHours() == 15;
        diploma.getLevel().setOrder(6);
        assert diploma.getQtdHours() == 0;
    }

    @Test
    public void givenLevelNull_whenGetQtdHours_thenReturnZero() {
        Diploma diploma = new Diploma();
        assert diploma.getQtdHours() == 0;
    }

    @Test
    public void givenDt_whenGetDtStr_thenReturnCorrectString() throws ParseException {
        Diploma diploma = new Diploma();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        diploma.setDt(sdf.parse("09/28/2024"));
        Assertions.assertEquals("28/09/2024", diploma.getDtStr());
    }

    @Test
    public void givenDtNull_whenGetDtStr_thenReturnEmptyString() {
        Diploma diploma = new Diploma();
        Assertions.assertEquals("", diploma.getDtStr());
    }
}
