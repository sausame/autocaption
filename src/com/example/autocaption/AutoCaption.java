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

	public static class Sentence1 extends Sentence {
		public Sentence1(Context ctx) {
			super(ctx);
		}

		public Sentence1 setDate(Date date) {
			mDate = date;
			return this;
		}

		private void generateDate() {
			// 1. Check the named days.
			int diff = SimpleDateTime.compareToday(mDate);

			mDateString = SimpleResources.getStringValue(mCtx, R.array.named_days_string_values, -1 * diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 2. Check if it's in this week
			diff = SimpleDateTime.compareFirstDayInLastWeek(mDate);

			mDateString = SimpleResources.getStringValue(mCtx, R.array.days_in_week_string_values, diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 3. Check if it's in the last week.
			mDateString = SimpleResources.getStringValue(mCtx, R.array.days_in_last_week_string_values, -1 * diff);
			if (mDateString != null && !mDateString.isEmpty()) 
				return;
			}

			// 4. Others.
			mDateString = SimpleDateTime.getDayString(mDate);
		}

		private void generateTime() {
			int hour = SimpleDateTime.getHourOfDay(mDate);
			int index = SimpleResources.getIndexByValue(mCtx, R.array.hour_values, hour);
			mTimeString = SimpleResources.getStringValue(index);
		}

		public String generate() {
			return null;
		}

		private Date mDate;

		private String mDateString;
		private String mTimeString;
	}

	public static class Sentence2 extends Sentence {
		public Sentence2(Context ctx) {
			super(ctx);
		}

		public Sentence2 setDate(Date date) {
			mDate = date;
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

		private Date mDate;
		private String mEvent;
		private Venue mVenue;
		private String[] mPersons;
	}

	public static String generateSentence1(Context ctx, Date date) {
		return (new Sentence1(ctx)).setDate(date).generate();
	}

	public static String generateSentence2(Context ctx, Date date,
			String event, Venue venue, String[] persons) {
		return (new Sentence2(ctx)).setDate(date).setEvent(event)
				.setVenue(venue).setPersons(persons).generate();
	}

}
