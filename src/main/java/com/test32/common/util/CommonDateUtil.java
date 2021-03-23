package com.test32.common.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CommonDateUtil
{
    public static Date addSecond(Date targetDate, int secondToAdd)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(targetDate);
        cal.add(Calendar.SECOND, secondToAdd);
        return cal.getTime();
    }

    public static Date addMinute(Date targetDate, int minuteToAdd)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(targetDate);
        cal.add(Calendar.MINUTE, minuteToAdd);
        return cal.getTime();
    }

    public static Date addDay(Date targetDate, int dayToAdd)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(targetDate);
        cal.add(Calendar.HOUR, 24 * dayToAdd);
        return cal.getTime();
    }

    public static Date addMonth(Date targetDate, int monthToAdd)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(targetDate);
        cal.add(Calendar.MONTH, monthToAdd);
        return cal.getTime();
    }

    public static boolean isDateInRangeByHour(Date targetDate, int hour)
    {
        Date calculated = addMinute(targetDate, 60 * hour);
        return calculated.getTime() > new Date().getTime();
    }

    public static boolean isDateInRangeByMinute(Date targetDate, int minute)
    {
        Date calculated = addMinute(targetDate, minute);
        return calculated.getTime() > new Date().getTime();
    }

    public static Date getDayStart(Date targetDate, String timeZone)
    {
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if( ! StringUtils.isEmpty(timeZone))
            resultFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String temp = resultFormat.format(targetDate);
        temp = temp.substring(0, 10);
        try
        {
            return resultFormat.parse(temp + " 00:00:00");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getDayEnd(Date targetDate, String timeZone)
    {
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if( ! StringUtils.isEmpty(timeZone))
            resultFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String temp = resultFormat.format(targetDate);
        temp = temp.substring(0, 10);
        try
        {
            return resultFormat.parse(temp + " 23:59:59");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getMonthStart(Date targetDate, String timeZone)
    {
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if( ! StringUtils.isEmpty(timeZone))
            resultFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String temp = resultFormat.format(targetDate);
        temp = temp.substring(0, 8);
        try
        {
            return resultFormat.parse(temp + "01 00:00:00");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getMonthEnd(Date targetDate, String timeZone)
    {
        Date monthStart = addMonth(getMonthStart(targetDate, timeZone), 1);
        return addSecond(monthStart, -1);
    }
    public static Date getYearStart(Date targetDate, String timeZone)
    {
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy");
        if( ! StringUtils.isEmpty(timeZone))
            resultFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String temp = resultFormat.format(targetDate);
        try
        {
            return resultFormat.parse(temp + "-01-01 00:00:00");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getYearEnd(Date targetDate, String timeZone)
    {
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy");
        if( ! StringUtils.isEmpty(timeZone))
            resultFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String temp = resultFormat.format(targetDate);
        try
        {
            return resultFormat.parse(temp + "-12-31 23:59:59");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDaysDifference(Date startDay, Date endDay)
    {
        long statDayTime = startDay.getTime();
        long endDayTime = endDay.getTime();

        return (int)((endDayTime - statDayTime)/(1000 * 60 * 60 * 24));
    }

    public static float getIntervalBySecond(Date startDay, Date endDay)
    {
        long statDayTime = startDay.getTime();
        long endDayTime = endDay.getTime();

        return (float)((endDayTime - statDayTime)/1000);
    }

    public static String addStringData(String target, String addStr)
    {
        if ("".equals(target) || target == null)
        {
            target = "";
            target += addStr;
        }
        else
        {
            target += ", " + addStr;
        }

        return target;
    }
}