package com.example.autocaption;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;

public class AutoCaption {

	public static String generateSentence() {
		return null;
	}

	public static class Sentence {

		public Sentence(Context ctx) {
			mCtx = ctx;
		}

		public String generate() {
			return null;
		}

		public final String[] getStringArray(int resId) {
			return mCtx.getResources().getStringArray(resId);
		}

		private Context mCtx;
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

	public static String getStringValue(Context ctx, int arrayResId, int index) { 
		String[] values = ctx.getResources().getStringArray(arrayResId);
		if (index >= 0 && index < values.length && !values[index].isEmpty()) {
			return values[index];
		}

		return null;
	}

	public static int getIndexByValue(Context ctx, int arrayResId, int value) {
		int[] values = ctx.getResources().getIntArray(arrayResId);

		for (int index = 0; index < values.length; index ++) {
			if (value <= values[index]) {
				return index;
			}
		}

		return -1;
	}

	public static class Sentence1 extends Sentence {
		public Sentence1(Context ctx) {
			super(ctx);
		}

		public Sentence1 setTime(Time time) {
			mTime = time;

			mDate = new Date();
			mDate.setTime(time.normalize(false));

			return this;
		}

		private void generateDate() {
			// 1. Check the named days.
			int diff = compareToday(mDate);

			mDateString = getStringValue(mCtx, R.array.named_days_string_values, -1 * diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 2. Check if it's in this week
			diff = compareFirstDayInLastWeek(mDate);

			mDateString = getStringValue(mCtx, R.array.days_in_week_string_values, diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 3. Check if it's in the last week.
			mDateString = getStringValue(mCtx, R.array.days_in_last_week_string_values, -1 * diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 4. Others.
			mDateString = getDayString(mDate);
		}

		private void generateTime() {
			int hour = getHourOfDay(mDate);
			int index = getIndexByValue(mCtx, R.array.hour_values, hour);
			mTimeString = getStringValue(index);
		}

		public String generate() {
			return null;
		}

		private Date mDate;
		private Time mTime;

		private String mDateString;
		private String mTimeString;
	}

	public static class Sentence2 extends Sentence {
		public Sentence2(Context ctx) {
			super(ctx);
		}

		public Sentence2 setTime(Time time) {
			mTime = time;
			return this;
		}

		public Sentence2 setEvent(String event) {
			mEvent = event;
			return this;
		}

		public Sentence2 setVenue(Venue venue) {
			mVenue = venue;
			return this;
		}

		public Sentence2 setPersons(String[] persons) {
			mPersons = persons;
			return this;
		}

		private void generateDate() {
		}

		private void generateTime() {
		}

		private void generateEvent() {
		}

		private void generateVenue() {
		}

		private void generatePerson() {
		}

		public String generate() {
			return null;
		}

		private Time mTime;
		private String mEvent;
		private Venue mVenue;
		private String[] mPersons;
	}

	public static String generateSentence1(Context ctx, Time time) {
		return (new Sentence1(ctx)).setTime(time).generate();
	}

	public static String generateSentence2(Context ctx, Time time,
			String event, Venue venue, String[] persons) {
		return (new Sentence2(ctx)).setTime(time).setEvent(event)
				.setVenue(venue).setPersons(persons).generate();
	}

}
