package com.example.autocaption;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleDateTime {

    public static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day, hour, minute);

		return calendar.getTime();
    }

    public static int compareDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(date2);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int compareToday(Date date) {
        return compareDate(new Date(), date);
    }

    public static int getDayInWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

    public static int getTodayInWeek() {
        return getDayInWeek(new Date());
	}

    public static int compareFirstDayInThisWeek(Date date) {
        int diff = compareToday(date);
		int dayInWeek = getTodayInWeek();

		return (diff + dayInWeek);
	}

    public static int compareFirstDayInLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.DAY_OF_WEEK, 1); // This sunday.
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.DAY_OF_WEEK, 1); // Last sunday.

        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(date);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }

    public static String getDayString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int day = calendar.get(Calendar.HOUR_OF_DAY);

        return day;
    }

    public static int[] getNeturalAge(Date birthday) {
        Calendar calendarBirth = new GregorianCalendar();
        Calendar calendarNow = new GregorianCalendar();

        calendarBirth.setTime(birthday);
        calendarNow.setTime(new Date());

        return getNeturalAge(calendarBirth, calendarNow);
    }

    public static int[] getNeturalAge(Calendar calendarBirth,
                                      Calendar calendarNow) {
        int diffYears = 0, diffMonths, diffDays;
        int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH);
        int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH);
        if (dayOfBirth <= dayOfNow) {
            diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
            diffDays = dayOfNow - dayOfBirth;
            if (diffMonths == 0) {
                diffDays++;
            }
        } else {
            if (isEndOfMonth(calendarBirth)) {
                if (isEndOfMonth(calendarNow)) {
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = 0;
                } else {
                    calendarNow.add(Calendar.MONTH, -1);
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = dayOfNow + 1;
                }
            } else {
                if (isEndOfMonth(calendarNow)) {
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = 0;
                } else {
                    calendarNow.add(Calendar.MONTH, -1); // last month
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);

                    // Get the latest one in last month
                    int maxDayOfLastMonth = calendarNow
                            .getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (maxDayOfLastMonth > dayOfBirth) {
                        diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow;
                    } else {
                        diffDays = dayOfNow;
                    }
                }
            }
        }

        // Calculate the month without considering the year, so adjust it.
        diffYears = diffMonths / 12;
        diffMonths = diffMonths % 12;

        return new int[]{diffYears, diffMonths, diffDays};
    }

    /**
     * Get month number of an age.
     *
     * @param calendarBirth
     * @param calendarNow
     * @return
     */
    public static int getMonthsOfAge(Calendar calendarBirth,
                                     Calendar calendarNow) {
        return (calendarNow.get(Calendar.YEAR) - calendarBirth
                .get(Calendar.YEAR))
                * 12
                + calendarNow.get(Calendar.MONTH)
                - calendarBirth.get(Calendar.MONTH);
    }

    /**
     * If it's the end of a month.
     *
     * @param calendar
     * @return
     */
    public static boolean isEndOfMonth(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }
}
