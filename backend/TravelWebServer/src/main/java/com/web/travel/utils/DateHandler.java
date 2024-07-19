package com.web.travel.utils;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

@Component
public class DateHandler {
    public int getDiffDay(Date date1, Date date2){
        long date1InMs = date1.getTime();
        long date2InMs = date2.getTime();
        long timeDiff = 0;

        timeDiff = date1InMs - date2InMs;

        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    public static Date getCurrentDateTime(){
        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Date currentDateInVietnam = new Date(System.currentTimeMillis());

        // Set the time zone to Vietnam's time zone
        vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        currentDateInVietnam.setTime(currentDateInVietnam.getTime() + vietnamTimeZone.getRawOffset());
        return currentDateInVietnam;
    }
}
