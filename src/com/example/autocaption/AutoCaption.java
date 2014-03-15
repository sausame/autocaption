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

	public static int calulateDays(Time time) {
		Date date = new Date();
		date.setTime(time.normalize(false));

		Calendar c = Calendar.getInstance();

		c.setTime(new Date());
		int nowDay = c.get(Calendar.DAY_OF_YEAR);

		c.setTime(date);
		int theDay = c.get(Calendar.DAY_OF_YEAR);

		return theDay - nowDay;
	}

	public static class Sentence1 extends Sentence {
		public Sentence1(Context ctx) {
			super(ctx);
		}

		public Sentence1 setTime(Time time) {
			mTime = time;
			return this;
		}

		private void generateDate() {
			int diff = calulateDays(mTime);

			if (diff < 0) {
				diff *= -1;
				// Check the named days.
				// 0, today
				// 1, yesterday
				// 2, the day before yesterday
				// 3, the bigger day before yesterday
				String[] namedDays = getStringArray(R.array.named_days_string_values);
				if (diff < namedDays.length && !namedDays[diff].isEmpty()) {
					mDateString = namedDays[diff];
					return;
				}

				// if (R.array.days_near_string_values)

				// Check in the last week.
			}

			// Others.
		}

		private void generateTime() {
		}

		public String generate() {
			return null;
		}

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
