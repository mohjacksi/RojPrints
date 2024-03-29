package com.mjacksi.rojprints.Utilises;

/**
 * @author Mohammed Jacksi
 * to return time stamp as (before x time)
 */
public class Utilises {
    public static String getTime(long receivedTime) {
        long nowTime = System.currentTimeMillis() / 1000L;
        long time = nowTime - receivedTime;
        String unit = "";
        if (time < 60) {
            unit = "s";
        } else if (time < 3600) {
            time /= 60;
            unit = "m";
        } else if (time < 86400) {
            time /= 60 * 60;
            unit = "h";
        } else {
            time /= 60 * 60 * 24;
            unit = "d";
        }
        return time + unit;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000L;
    }
}
