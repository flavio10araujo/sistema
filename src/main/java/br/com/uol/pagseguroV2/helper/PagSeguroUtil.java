/*
 ************************************************************************
 Copyright [2011] [PagSeguro Internet Ltda.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************
 */

package br.com.uol.pagseguroV2.helper;

import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.properties.PagSeguroV2System;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * @author asilva
 */
public class PagSeguroUtil {

    private static final int NUMBER_6 = 6;
    private static final int NUMBER_4 = 4;
    private static final int NUMBER_3 = 3;
    private static final int NUMBER_1 = 1;
    private static final int NUMBER_29 = 29;
    private static final int NUMBER_23 = 23;
    private static final int NUMBER_0 = 0;

    private static final int NUMBER_100 = 100;

    private PagSeguroUtil() {
    }

    /**
     * @param s
     * @return
     * @throws ParseException
     */
    public static Date parse(String s) throws ParseException {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        // date format: YYYY-MM-DDThh:mm:ss.sTZD
        String dateWithoutTZ = s.substring(NUMBER_0, NUMBER_23);
        String timeZone = s.substring(NUMBER_23, NUMBER_29);

        Calendar calWithoutTZ = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        Date date = df.parse(dateWithoutTZ);
        calWithoutTZ.setTimeInMillis(date.getTime());

        calendar.set(Calendar.YEAR, calWithoutTZ.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calWithoutTZ.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calWithoutTZ.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calWithoutTZ.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calWithoutTZ.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calWithoutTZ.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calWithoutTZ.get(Calendar.MILLISECOND));

        int tzHour = Integer.parseInt(timeZone.substring(NUMBER_1, NUMBER_3));
        int tzMin = Integer.parseInt(timeZone.substring(NUMBER_4, NUMBER_6));

        boolean plus = "+".equals(timeZone.substring(NUMBER_0, NUMBER_1));

        if (plus) {
            calendar.add(Calendar.HOUR, -tzHour);
            calendar.add(Calendar.MINUTE, -tzMin);
        } else {
            calendar.add(Calendar.HOUR, tzHour);
            calendar.add(Calendar.MINUTE, tzMin);
        }
        return calendar.getTime();
    }

    /**
     * Get only numbers from a string value
     *
     * @param value
     * @return
     */
    public static String getOnlyNumbers(String value) {
        return value.replaceAll("\\D+", "").toString();
    }

    public static String truncateValue(String value, int limit, String endChars) {
        String result = value;
        if (value != null && value.length() > limit) {
            result = value.substring(0, limit - endChars.length()) + endChars;
        }
        return result;
    }

    public static String removeExtraSpaces(String value) {
        return value.replaceAll("( +)", " ").trim();
    }

    public static String removeCharacterPhone(String number) {
        String result = number;
        result = number.replaceAll("[()]", "");
        result = number.replaceAll("-", "");
        result = number.replaceAll(" ", "");
        return result;
    }

    public static String urlQuery(Map<Object, Object> map) throws PagSeguroServiceException {

        boolean first = true;
        StringBuilder sb = new StringBuilder();

        for (Entry<Object, Object> pay : map.entrySet()) {

            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            try {
                sb.append(URLEncoder.encode(pay.getKey().toString(), PagSeguroV2System.getPagSeguroEncoding()));
                sb.append("=");
                sb.append(URLEncoder.encode(pay.getValue().toString(), PagSeguroV2System.getPagSeguroEncoding()));
            } catch (UnsupportedEncodingException e) {
                throw new PagSeguroServiceException("Error when trying enconde", e);
            } catch (Exception e) {
                throw new PagSeguroServiceException("Error when trying enconde", e);
            }
        }
        return sb.toString();
    }
}
