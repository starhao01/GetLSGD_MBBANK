package com.viettelpost.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return sdf.format(date);
    }

    public static Date dateToDateFormat(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(format.format(date));
    }

    public static Date dateToDateFormatV2(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return format.parse(format.format(date));
    }

    /**
     * d2-d1
     */
    public static long diff(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (diff < 0 ? -1 : 1) * (TimeUnit.DAYS.convert(Math.abs(diff), TimeUnit.MILLISECONDS) + 1);
    }

    public static boolean sameMonth(Date d1, Date d2) {
        LocalDate ld1 = convertToLocalDate(d1);
        LocalDate ld2 = convertToLocalDate(d2);
        return ld1.getMonthValue() == ld2.getMonthValue() && ld1.getYear() == ld2.getYear();
    }
}
