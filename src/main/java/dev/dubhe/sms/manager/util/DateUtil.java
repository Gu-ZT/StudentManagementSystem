package dev.dubhe.sms.manager.util;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({"deprecation", "unused"})
public class DateUtil {
    @Nonnull
    public static ZonedDateTime nowDate() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.atZone(ZoneId.of("GMT+8"));
    }

    @Nonnull
    public static Date now() {
        return Date.from(nowDate().toInstant());
    }

    @Nonnull
    public static Date afterDays(Date now, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static boolean isSameDay(@Nonnull Date date, @Nonnull Date date1) {
        return date.getYear() == date1.getYear() && date.getMonth() == date1.getMonth() && date.getDate() == date1.getDate();
    }

    /**
     * 格式化输出日期时间
     *
     * @param date 日期时间
     * @return yyyy-MM-DD HH:mm:ss
     */
    @Nonnull
    public static String toString(@Nonnull Date date) {
        StringBuilder sb = new StringBuilder();
        DateUtil.sprintf0d(sb, date.getYear() + 1900, 4).append('-');    // yyyy
        DateUtil.sprintf0d(sb, date.getMonth(), 2).append('-');   // MM
        DateUtil.sprintf0d(sb, date.getDate(), 2).append(' ');    // dd
        DateUtil.sprintf0d(sb, date.getHours(), 2).append(':');   // HH
        DateUtil.sprintf0d(sb, date.getMinutes(), 2).append(':'); // mm
        DateUtil.sprintf0d(sb, date.getSeconds(), 2);             // ss
        return sb.toString();
    }

    @Nonnull
    @SuppressWarnings("SameParameterValue")
    private static StringBuilder sprintf0d(StringBuilder sb, int value, int width) {
        long d = value;
        if (d < 0) {
            sb.append('-');
            d = -d;
            --width;
        }
        int n = 10;
        for (int i = 2; i < width; i++) {
            n *= 10;
        }
        for (int i = 1; i < width && d < n; i++) {
            sb.append('0');
            n /= 10;
        }
        sb.append(d);
        return sb;
    }
}
