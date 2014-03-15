package com.example.autocaption;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;

public class SimpleDateTime {

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
		return compareDate(date, new Date());
	}

	public static int compareFirstDayInLastWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		calendar.set(Calendar.DAY_OF_WEEK, 1);
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
}
