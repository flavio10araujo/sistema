package com.polifono.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat simpleDateFormatYearMonthDay = new SimpleDateFormat("yyy-MM-dd");

	public static void main(String[] args) {
		//try {
			//System.out.println(getFirstDayOfTheCurrentMonth());
			//System.out.println(getLastDayOfTheCurrentMonth());

			//String d1Str = "2019-01-05";
			//String d1Str = "05/01/2019";
			//java.util.Date d1 = parseDateYearMonthDayFormat(d1Str);
			//System.out.println(d1);

			//System.out.println(subtractMonth(new java.util.Date(), 2));

		//} catch (Exception e) {
			//e.printStackTrace();
		//}
	}

	/**
	 * This method converts an Object to a String of format dd/MM/yyyy.
	 *
	 * @return a String of format dd/MM/yyyy
	 * @param data an object of type Date
	 */
	public static String formatDate(Object data) {
		if (!"".equals(data) && data instanceof String) {
			return simpleDateFormat.format(data);
		}

		return "";
	}

	/**
	 * @return a java.util.Date object.
	 * @param data is a string with a "dd/MM/yyyy" pattern.
	 */
	public static java.util.Date parseDate(String data) throws ParseException {
		if (Util.isEmpty(data)) {
			return null;
		}

		return simpleDateFormat.parse(data);
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

	public static java.util.Date subtractMonth(java.util.Date currentDate, int subtract) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, (subtract * -1));
		return cal.getTime();
	}

	public static java.util.Date getCurrentDateWithHourAndSeconds() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
}
