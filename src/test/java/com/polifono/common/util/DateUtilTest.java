package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateUtilTest {

    @Test
    public void givenNullInput_WhenFormatDate_ThenReturnEmptyString() {
        assertEquals("", DateUtil.formatDate(null));
    }

    @Test
    public void givenEmptyStringInput_WhenFormatDate_ThenReturnEmptyString() {
        assertEquals("", DateUtil.formatDate(""));
    }

    @Test
    public void givenValidDateInput_WhenFormatDate_ThenReturnFormattedDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse("01/31/2024");
        assertEquals("31/01/2024", DateUtil.formatDate(date));
    }

    @Test
    public void givenInvalidObjectInput_WhenFormatDate_ThenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.formatDate(new Object());
        });
    }

    @Test
    public void givenCurrentDateAndPositiveNumber_WhenSubtractMonth_ThenReturnDateInThePast() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();

        calendar.set(2023, Calendar.NOVEMBER, 30);
        Date expectedDate = calendar.getTime();

        assertEquals(expectedDate, DateUtil.subtractMonth(date, 2));
    }

    @Test
    public void givenCurrentDateAndZero_WhenSubtractMonth_ThenReturnSameDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();

        assertEquals(date, DateUtil.subtractMonth(date, 0));
    }

    @Test
    public void givenCurrentDateAndNegativeNumber_WhenSubtractMonth_ThenReturnDateInTheFuture() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();

        calendar.set(2024, Calendar.MARCH, 31);
        Date expectedDate = calendar.getTime();

        assertEquals(expectedDate, DateUtil.subtractMonth(date, -2));
    }

    @Test
    public void givenNullDate_WhenGetDateField_ThenReturnMinusOne() {
        assertEquals(-1, DateUtil.getDateField(null, Calendar.DAY_OF_MONTH));
    }

    @Test
    public void givenValidDate_WhenGetDayOfMonth_ThenReturnCorrectDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();
        assertEquals(31, DateUtil.getDateField(date, Calendar.DAY_OF_MONTH));
    }

    @Test
    public void givenValidDate_WhenGetMonth_ThenReturnCorrectMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();
        assertEquals(Calendar.JANUARY, DateUtil.getDateField(date, Calendar.MONTH));
    }

    @Test
    public void givenValidDate_WhenGetYear_ThenReturnCorrectYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 31);
        Date date = calendar.getTime();
        assertEquals(2024, DateUtil.getDateField(date, Calendar.YEAR));
    }
}
