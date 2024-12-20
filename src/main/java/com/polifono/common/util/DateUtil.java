package com.polifono.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * This method converts an Object to a String of format dd/MM/yyyy.
     *
     * @param data an object of type Date
     * @return a String of format dd/MM/yyyy
     */
    public static String formatDate(Object data) {
        if (data == null || data.toString().isEmpty()) {
            return "";
        }

        return simpleDateFormat.format(data);
    }

    public static java.util.Date getFirstDayOfTheCurrentMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static java.util.Date getLastDayOfTheCurrentMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static java.util.Date subtractMonth(java.util.Date date, int subtract) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, (subtract * -1));
        return cal.getTime();
    }

    public static java.util.Date getCurrentDateWithHourAndSeconds() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static int getDateField(Date date, int field) {
        if (date == null) {
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }
}
