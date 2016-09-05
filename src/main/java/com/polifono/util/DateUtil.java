package com.polifono.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/*public static void main(String args[]) {

	}*/

	/**
	 * This method converts an Object to a String of format dd/MM/yyyy.
	 *
	 * @return a String of format dd/MM/yyyy
	 * @param data an object of type Date
	 */
	public static String formatDate(Object data) {
		if (data != null && !"".equals(data) && data instanceof String) {
			//try {
				return simpleDateFormat.format(data);
			/*}
			catch(IllegalArgumentException e) {
				return "";
			}*/
		}

		return "";
	}

	/**
	 * @return a java.util.Date object.
	 * @param data A string with a "dd/MM/yyyy" pattern.
	 */
	public static java.util.Date parseDate(String data) throws ParseException {
		if (Util.isEmpty(data)) {
			return null;
		}

		return simpleDateFormat.parse(data);
	}
}