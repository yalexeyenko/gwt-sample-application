package com.mySampleApplication.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

import java.util.Date;

public final class GWTDateUtils {

    private static final String US_DATE_FORMAT = "MM/dd/yy";
    private static final String COMMON_DATE_FORMAT = "dd/MM/yy";
    private static final String US_LOCALE_SIGN = "us";

    private GWTDateUtils() {}

    public static String shortValueOf(Date sourceDate) {
        if (sourceDate != null) {
            return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(
                    sourceDate);
        } else {
            return "";
        }
    }

    public static boolean isBeforeCurrent(Date checkedDate) {
        return checkedDate == null || checkedDate.before(new Date());
    }

    public static String getLocaleDependentDateFormat(String localeName) {
        if (localeName.toLowerCase().contains(US_LOCALE_SIGN)) {
            return US_DATE_FORMAT;
        } else {
            return COMMON_DATE_FORMAT;
        }
    }

    public static String formatUTC(Date date) {
        return formatUTC(date, getLocaleDependentDateFormat(IsRedactor.getLocaleName()));
    }

    public static String formatUTC(Date date, String format) {
        return DateTimeFormat.getFormat(format).format(date, TimeZone.createTimeZone(0));
    }
}
