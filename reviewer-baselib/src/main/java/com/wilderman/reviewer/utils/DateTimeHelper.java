package com.wilderman.reviewer.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeHelper {

    public final static String sqlDateFormat = "yyyy-MM-dd";

    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime convertCalendarToLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    public static String LongToString(Long ts, String format) {
        Date date=new Date(ts);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Period diff(Long ts1, Long ts2) {
        LocalDate ldt1 =  Instant.ofEpochMilli(ts1).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ldt2 =  Instant.ofEpochMilli(ts2).atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(ldt1, ldt2);

    }
}
