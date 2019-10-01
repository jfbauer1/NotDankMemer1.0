/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses;

import me.turulix.main.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FormatUtil {
    @NotNull
    public static String secondsToTime(long timeseconds) {
        @NotNull StringBuilder builder = new StringBuilder();
        int years = (int) (timeseconds / (60 * 60 * 24 * 365));
        if (years > 0) {
            builder.append("**").append(years).append("** years, ");
            timeseconds = timeseconds % (60 * 60 * 24 * 365);
        }
        int weeks = (int) (timeseconds / (60 * 60 * 24 * 365));
        if (weeks > 0) {
            builder.append("**").append(weeks).append("** weeks, ");
            timeseconds = timeseconds % (60 * 60 * 24 * 7);
        }
        int days = (int) (timeseconds / (60 * 60 * 24));
        if (days > 0) {
            builder.append("**").append(days).append("** days, ");
            timeseconds = timeseconds % (60 * 60 * 24);
        }
        int hours = (int) (timeseconds / (60 * 60));
        if (hours > 0) {
            builder.append("**").append(hours).append("** hours, ");
            timeseconds = timeseconds % (60 * 60);
        }
        int minutes = (int) (timeseconds / (60));
        if (minutes > 0) {
            builder.append("**").append(minutes).append("** minutes, ");
            timeseconds = timeseconds % (60);
        }
        if (timeseconds > 0) builder.append("**").append(timeseconds).append("** seconds");
        @NotNull String str = builder.toString();
        if (str.endsWith(", ")) str = str.substring(0, str.length() - 2);
        if (str.isEmpty()) str = "**No time**";
        return str;
    }

    public static int parseTime(String timestr) {
        timestr = timestr.replaceAll("(?i)(\\s|,|and)", "").replaceAll("(?is)(-?\\d+|[a-z]+)", "$1 ").trim();
        @NotNull String[] vals = timestr.split("\\s+");
        int timeinseconds = 0;
        try {
            for (int j = 0; j < vals.length; j += 2) {
                int num = Integer.parseInt(vals[j]);
                if (vals[j + 1].toLowerCase().startsWith("m")) num *= 60;
                else if (vals[j + 1].toLowerCase().startsWith("h")) num *= 60 * 60;
                else if (vals[j + 1].toLowerCase().startsWith("d")) num *= 60 * 60 * 24;
                timeinseconds += num;
            }
        } catch (Exception ex) {
            return 0;
        }
        return timeinseconds;
    }

    public static String removeEscapeChars(String msg) {
        msg = msg.replaceAll("[\b\n\t\r\f]+", " ");
        return msg;
    }

    public static String getCurrentTimeAsString() {
        @NotNull DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        return formatter.format(new Date());
    }

    public static String decodeUTF8(String msg) {
        try {
            byte[] bytes = msg.getBytes();
            //System.out.println("Converted String: " + new String(bytes, StandardCharsets.UTF_8));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            Logger.error(ex);
        }
        return msg;
    }
}
