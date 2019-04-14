package com.github.sofaid.app.utils;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * originally by Mr. Robik
 * created by umer on 05/01/2017.
 */

public class DateTimeUtil {
    // 9:41 AM
    private static final String IN_HOUR_FORMAT = "h:mm a";

    // Mon, 9:41 AM
    private static final String IN_DAY_FORMAT = "EEE, h:mm a";

    // OCT 1, 9:41 AM
    private static final String IN_MONTH_FORMAT = "MMM d, h:mm a";

    //1 OCT 2014, 9:41 AM
    private static final String IN_YEAR_FORMAT = "d MMM yyyy, h:mm a";

    // UTC formate
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    // Local format to show in app
    private static final String LOCALE_DATE_FORMAT = "MMM d, h:mm a";


    public static String getTimeDiff(Date timeBegin, Date timeEnd) {
        long daysDiff = getDaysDiff(timeBegin, timeEnd);
        if (daysDiff > 0) {
            return daysDiff + "d";
        }

        long hoursDiff = getHoursDiff(timeBegin, timeEnd);
        if (hoursDiff > 0) {
            return hoursDiff + "h";
        }

        long minutesDiff = getMinutesDiff(timeBegin, timeEnd);
        if (minutesDiff > 0) {
            return minutesDiff + "m";
        }

        long secsDiff = getSecondsDiff(timeBegin, timeEnd);
        if (secsDiff > 0) {
            return secsDiff + "s";
        } else {
            return "1s";
        }
    }

    public static String getTimeDiffInLongForm(Date timeBegin, Date timeEnd) {
        long daysDiff = getDaysDiff(timeBegin, timeEnd);
        if (daysDiff > 0) {
            return daysDiff + " days";
        }

        long hoursDiff = getHoursDiff(timeBegin, timeEnd);
        if (hoursDiff > 0) {
            return hoursDiff + " hrs";
        }

        long minutesDiff = getMinutesDiff(timeBegin, timeEnd);
        if (minutesDiff > 0) {
            return minutesDiff + " mins";
        }

        long secsDiff = getSecondsDiff(timeBegin, timeEnd);
        if (secsDiff > 0) {
            return secsDiff + " secs";
        } else {
            return "1s";
        }
    }


    public static final long getDaysDiff(Date timeBegin, Date timeEnd) {
        long diff = timeEnd.getTime() - timeBegin.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static final long getHoursDiff(Date timeBegin, Date timeEnd) {
        long diff = timeEnd.getTime() - timeBegin.getTime();
        return diff / (1000 * 60 * 60);
    }

    public static final long getMinutesDiff(Date timeBegin, Date timeEnd) {
        long diff = timeEnd.getTime() - timeBegin.getTime();
        return diff / (1000 * 60);
    }

    public static final long getSecondsDiff(Date timeBegin, Date timeEnd) {
        long diff = timeEnd.getTime() - timeBegin.getTime();
        return diff / (1000);
    }

    public static void main(String[] args) {
        //test getMinDiff
        String time1Str = "2016/09/22 03:06:04";
        String time2Str = "2016/09/23 04:08:04";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date time1 = simpleDateFormat.parse(time1Str);
            Date time2 = simpleDateFormat.parse(time2Str);
            System.out.println("Days diff: " + getDaysDiff(time1, time2));
            System.out.println("Hours diff: " + getHoursDiff(time1, time2));
            System.out.println("Minutes diff: " + getMinutesDiff(time1, time2));
            System.out.println("Time diff: " + getTimeDiff(time1, time2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * formats timestamp into in-day, in-week, in-month, in-year, format
     *
     * @param timestamp
     * @return string formatted time
     */
    public static String format(Long timestamp) {
        if (inDay(timestamp)) {
            return formatDateTime(timestamp, IN_HOUR_FORMAT);
        } else if (inWeek(timestamp)) {
            return formatDateTime(timestamp, IN_DAY_FORMAT);
        } else if (inYear(timestamp)) {
            return formatDateTime(timestamp, IN_MONTH_FORMAT);
        } else {
            return formatDateTime(timestamp, IN_YEAR_FORMAT);
        }
    }

    public static String formatInHour(Long timestamp) {
        return formatDateTime(timestamp, IN_HOUR_FORMAT);
    }

    private static boolean inMonth(Long timestamp) {
        return false;
    }

    /**
     * formtas timestamp string with given format
     *
     * @param timestamp
     * @return boolean
     */
    private static String formatDateTime(Long timestamp, String format) {
        Date date = new Date(timestamp);

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * calculates given timestmap to check if date is with in current day
     *
     * @param timestamp
     * @return boolean
     */
    private static boolean inDay(Long timestamp) {
        return DateUtils.isToday(timestamp);
    }

    /**
     * calculates given timestmap to check if date is with in current week
     *
     * @param timestamp
     * @return boolean
     */
    private static boolean inWeek(Long timestamp) {
        Date date = new Date(timestamp);

        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        return week == currentWeek && year == currentYear;
    }

    private static boolean inYear(Long timestamp) {
        Date date = new Date(timestamp);

        Calendar currentCalendar = Calendar.getInstance();
        int year = currentCalendar.get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentYear = calendar.get(Calendar.YEAR);

        return year == currentYear;
    }

    /**
     * Converts timestamp in millis to formatted Local Date/Time String
     *
     * @param timestamp
     * @return formatted Date/Time String in Local formatted String
     */

    public static String formatToLocale(long timestamp) {
        Date date = new Date(timestamp);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LOCALE_DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getDefault());

        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatToConversationTime(long timestamp) {
        return format(timestamp);
    }

    /**
     * Converts timestamp in millis to formatted UTC Date/Time String
     *
     * @param timestamp
     * @return formatted Date/Time String in UTC format
     */

    public static String formatInUTC(long timestamp) {
        try {
            Date date = new Date(timestamp);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
            TimeZone UTC = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(UTC);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.setTimeZone(UTC);

            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts UTC formated DateString to Local format to show in adapter
     *
     * @param dateInString
     * @return formatted Date/Time string local format
     */

    public static String formatToLocale(String dateInString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATE_FORMAT);

            Date date = formatter.parse(dateInString);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LOCALE_DATE_FORMAT, Locale.getDefault());
            TimeZone timeZone = TimeZone.getDefault();
            simpleDateFormat.setTimeZone(timeZone);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.setTimeZone(timeZone);

            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception ex) {
            return null;
        }

    }

    public static Date newDate(int year, int month, int day) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String dateStr = year + "-" + month + "-" + day;
        return sdf.parse(dateStr);
    }

    public static String toYYYYMMdd(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date fromYYYYMMdd(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}