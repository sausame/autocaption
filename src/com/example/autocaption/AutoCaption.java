package com.example.autocaption;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;

public class AutoCaption {

	public final static int AGE_MAX = 18;

	public final static int MAX_PERSON = 3;

	private Date mDate;
	private String mEvent;
	private Venue mVenue;
	private Person[] mPersons;

	private String mDateBriefString;
	private String mDateString;
	private String mTimeString;
	private String mEventString;
	private String mVenueTypeString;
	private String mVenueValueString;
	private String[] mPersonStrings;
	private String mPersonsString;

	private Context mCtx;

	private AutoCaption() {
	}

	private AutoCaption(Context ctx) {
		mCtx = ctx;
	}

	private AutoCaption setDate(Date date) {
		mDate = date;
		return this;
	}

	private AutoCaption setEvent(String event) {
		mEvent = event;
		return this;
	}

	private AutoCaption setVenue(Venue venue) {
		mVenue = venue;
		return this;
	}

	private AutoCaption setPersons(Person[] persons) {
		mPersons = persons;
		return this;
	}

	private void generateBriefDate() {
		// 1. Check the named days.
		int diff = SimpleDateTime.compareToday(mDate);

		mDateBriefString = SimpleResources.getStringValue(mCtx,
				R.array.named_days_string_values, -1 * diff);
		if (mDateBriefString != null && !mDateBriefString.isEmpty()) {
			return;
		}

		// 2. Check if it's in this week
		diff = SimpleDateTime.compareFirstDayInLastWeek(mDate);

		mDateBriefString = SimpleResources.getStringValue(mCtx,
				R.array.days_in_week_string_values, diff);
		if (mDateBriefString != null && !mDateBriefString.isEmpty()) {
			return;
		}

		// 3. Check if it's in the last week.
		mDateBriefString = SimpleResources.getStringValue(mCtx,
				R.array.days_in_last_week_string_values, -1 * diff);
		if (mDateBriefString != null && !mDateBriefString.isEmpty()) {
			return;
		}

		// 4. Others.
		// Do nothing.
	}

	private void generateDate() {
		mDateString = SimpleDateTime.getDayString(mDate);
	}

	private void generateTime() {
		int hour = SimpleDateTime.getHourOfDay(mDate);
		int index = SimpleResources.getIndexByValue(mCtx, R.array.hour_values,
				hour);
		mTimeString = SimpleResources.getStringValue(index);
	}

	private void generateEvent() {
		mEventString = mEvent;
	}

	private void generateVenue() {
		mVenueTypeString = SimpleResources.getStringValue(mVenue.type);
		mVenueValueString = mVenue.value;
	}

	private String generateBirthdayString(Date birthday) {
		int[] counts = SimpleDateTime.getNeturalAge(birthday);
		final int[] resIds = { R.plurals.year_age, R.plurals.month_age,
				R.plurals.day_age };

		if (counts[0] > AGE_MAX) {
			return null;
		}

		for (int index = 0; index < resIds.length; index++) {
			if (counts[0] > 0) {
				return SimpleResources.getResources(mCtx).getQuantityString(
						resIds[index], counts[index], counts[index]);
			}
		}

		return null;
	}

	private String generatePersonString(Person person) {
		birthdayString = generateBirthdayString(person.birthday);
		nameString = person.name;

		return SimpleResources.getResources(mCtx).getString(
				R.string.each_person_piece, nameString, birthdayString);
	}

	private void generatePersons() {
		int num = mPersons.length;
		mPersonStrings = new String[num];

		for (int index = 0; index < MAX_PERSON && index < num; index++) {
			mPersonStrings[i] = generatePersonString(mPersons[i]);
		}

		switch (num) {
		case 0:
			break;
		case 1:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.one_person_piece, mPersonStrings[0]);
			break;
		case 2:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.two_person_piece, mPersonStrings[0],
					mPersonStrings[1]);
			break;
		case 3:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.three_person_piece, mPersonStrings[0],
					mPersonStrings[1], mPersonStrings[2]);
			break;
		default:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.other_person_piece, mPersonStrings[0],
					mPersonStrings[1], mPersonStrings[2]);
			break;
		}
	}

	private String generateSentence1() {
		generateBriefDate();
		generateDate();
		generateTime();

		return SimpleResources.getResources(mCtx).getString(R.string.date_piece,
				mDateBriefString, mTimeString, mDateString);
	}

	private String generateSentence2() {
		String sentence;

		generateBriefDate();
		generateDate();
		generateTime();
		generateEvent();
		generateVenue();
		generatePersons();

		switch (num) {
		case 0:
			break;
		case 1:
			sentence = 
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.one_person_piece, mPersonStrings[0]);
			break;
		case 2:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.two_person_piece, mPersonStrings[0],
					mPersonStrings[1]);
			break;
		case 3:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.three_person_piece, mPersonStrings[0],
					mPersonStrings[1], mPersonStrings[2]);
			break;
		default:
			mPersonsString = SimpleResources.getResources(mCtx).getString(
					R.string.other_person_piece, mPersonStrings[0],
					mPersonStrings[1], mPersonStrings[2]);
			break;
		}

		return SimpleResources.getResources(mCtx).getString(R.string.date_piece,
				mDateBriefString, mTimeString, mDateString);
	}

	public static String generateSentence1(Context ctx, Date date) {
		return (new AutoCaption(ctx)).setDate(date).generateSentence1();
	}

	public static String generateSentence2(Context ctx, Date date,
			String event, Venue venue, Person[] persons) {
		return (new AutoCaption(ctx)).setDate(date).setEvent(event)
				.setVenue(venue).setPersons(persons).generateSentence2();
	}

}
