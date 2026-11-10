package com.salarygrows.live;

import java.time.*;
import java.time.temporal.ChronoUnit;

public final class SalaryMath {
    private SalaryMath() {}
    // Payday is the beginning of a new pay period. For months without the requested day,
    // the last day of the month is used. Calendar duration accounts for DST.
    private static ZonedDateTime payday(YearMonth month, int day, ZoneId zone) {
        return month.atDay(Math.min(day, month.lengthOfMonth())).atStartOfDay(zone);
    }
    public static double[] calculate(long monthlyWon, int paydayDay, long timestamp) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = Instant.ofEpochMilli(timestamp).atZone(zone);
        int day = Math.max(1, Math.min(31, paydayDay));
        YearMonth month = YearMonth.from(now);
        ZonedDateTime start = payday(month, day, zone);
        if (now.isBefore(start)) start = payday(month.minusMonths(1), day, zone);
        ZonedDateTime end = payday(YearMonth.from(start).plusMonths(1), day, zone);
        long periodMs = ChronoUnit.MILLIS.between(start, end);
        long elapsedMs = Math.max(0, Math.min(periodMs, ChronoUnit.MILLIS.between(start, now)));
        double rate = monthlyWon * 1000.0 / periodMs;
        return new double[] {monthlyWon * ((double) elapsedMs / periodMs), rate};
    }
}
