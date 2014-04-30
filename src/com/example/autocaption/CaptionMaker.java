package com.example.autocaption;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;

public class CaptionMaker {

	public final static String TAG = "CaptionMaker";

	public final static int AGE_MAX = 18;
	public final static int MAX_PERSON = 3;

	public final static int DATE_FIELD_DEFAULT = 0x1000;
	public final static int EVENT_FIELD_DEFAULT = 0x0100;
	public final static int VENUE_FIELD_DEFAULT = 0x0010;
	public final static int PERSON_FIELD_DEFAULT = 0x0001;

	public final static int DATE_FIELD_MASK = 0xF000;
	public final static int EVENT_FIELD_MASK = 0x0F00;
	public final static int PLACE_FIELD_MASK = 0x00F0;
	public final static int PERSON_FIELD_MASK = 0x000F;

	private int mRuleId;

	private Date mDate;
	private String mEvent;
	private Place mPlace;
	private Person[] mPersons;

	private String mDateTimeString;
	private String mEventString;
	private String mPlaceString;
	private String mPersonsString;

	private Context mCtx;

	public final static int RULES_MASKS_ID = 0;
	public final static int DATE_RULES_FIELDS = 1;
	public final static int RULES = 2;

	private CaptionMaker() {
	}

	private CaptionMaker(Context ctx) {
		mCtx = ctx;
	}

	private CaptionMaker setDate(Date date) {
		mDate = date;
		return this;
	}

	private CaptionMaker setEvent(String event) {
		mEvent = event;
		return this;
	}

	private CaptionMaker setPlace(Place place) {
		mPlace = place;
		return this;
	}

	private CaptionMaker setPersons(Person[] persons) {
		mPersons = persons;
		return this;
	}

	private int getRuleId() {
		mRuleId = 0;

		if (mDate != null) {
			mRuleId |= DATE_FIELD_DEFAULT;
		}

		if (!TextUtils.isEmpty(mEvent)) {
			mRuleId |= EVENT_FIELD_DEFAULT;
		}

		if (mPlace != null && !TextUtils.isEmpty(mPlace.value)) {
			mRuleId |= VENUE_FIELD_DEFAULT;
		}

		int num = ((mPersons != null) ? mPersons.length : 0);
		switch (num) {
		case 0:
		case 1:
		case 2:
		case 3:
			mRuleId |= num;
			break;
		default:
			mRuleId |= 0xf;
			break;
		}

		return mRuleId;
	}

	private String generatePiece(int[] resIds, String[] values) {
		int[] rulesMasks = SimpleResources.getIntArray(mCtx,
				resIds[RULES_MASKS_ID]);
		int[] fieldIndexs = SimpleResources.getIntArray(mCtx,
				resIds[DATE_RULES_FIELDS]);

		for (int ruleIndex = 0; ruleIndex < rulesMasks.length; ruleIndex++) {
			if (mRuleId == (mRuleId & rulesMasks[ruleIndex])) {
				int field = fieldIndexs[ruleIndex];
				int fieldMask = 0xf;
				boolean found = true;
				int fieldIndex;

				Log.v("Check NO." + ruleIndex + ": "
						+ Integer.toHexString(rulesMasks[ruleIndex])
						+ " (mask) " + Integer.toHexString(field)
						+ " (fields index)");

				for (fieldIndex = values.length - 1; fieldIndex >= 0; fieldIndex--) {
					if (0 != (field & fieldMask)) {
						if (TextUtils.isEmpty(values[fieldIndex])) {
							found = false;
							break;
						}
					}
					fieldMask <<= 4;
				}

				if (found) {
					Log.v("Matched! NO." + ruleIndex);

					String piece = SimpleResources.getStringValue(mCtx,
							resIds[RULES], ruleIndex, values);
					return piece;
				}

				Log.v("Ignore NO." + ruleIndex + " rule mask.");
			}
		}

		return null;
	}

	private String generateNamedDate(int namedDateResId) {
		String briefString;

		// 1. Check the named days.
		int diff = SimpleDateTime.compareToday(mDate);

		briefString = SimpleResources.getStringValue(mCtx, namedDateResId, -1
				* diff);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		return null;
	}

	private String generateWeeklyDate(int daysInLastWeekResId) {
		String briefString;

		// 1. Check if it's in this week
		briefString = SimpleDateTime.getDayInThisWeek(mDate);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		// 2. Check if it's in the last week.
		int diff = SimpleDateTime.compareFirstDayInLastWeek(mDate);

		briefString = SimpleResources.getStringValue(mCtx, daysInLastWeekResId,
				diff);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		// 3. Others.
		return null;
	}

	private String generateUpperNamedDate() {
		return generateNamedDate(R.array.upper_named_dates_string_values);
	}

	private String generateLowerNamedDate() {
		return generateNamedDate(R.array.lower_named_dates_string_values);
	}

	private String generateUpperWeeklyDate() {
		// return
		// generateWeeklyDate(R.array.upper_days_in_last_week_string_values);
		return SimpleDateTime.getDayNameOfWeek(mDate);
	}

	private String generateLowerWeeklyDate() {
		// return
		// generateWeeklyDate(R.array.lower_days_in_last_week_string_values);
		return SimpleDateTime.getDayNameOfWeek(mDate);
	}

	private String generateDate() {
		return SimpleDateTime.getDayString(mDate);
	}

	private String generateTime() {
		int hour = SimpleDateTime.getHourOfDay(mDate);
		int index = SimpleResources.getMaxIndexLessThanValue(mCtx,
				R.array.hour_indexs, hour);
		return SimpleResources.getStringValue(mCtx, R.array.hour_values, index);
	}

	private void generateDateTime() {
		final int resIds[] = { R.array.date_rules_masks,
				R.array.date_rules_fields, R.array.date_rules };
		String values[] = new String[] { generateUpperNamedDate(),
				generateLowerNamedDate(), generateUpperWeeklyDate(),
				generateLowerWeeklyDate(), generateDate(), generateTime() };

		mDateTimeString = generatePiece(resIds, values);
	}

	private void generateEvent() {
		final int resIds[] = { R.array.event_rules_masks,
				R.array.event_rules_fields, R.array.event_rules };
		String values[] = new String[] { mEvent };

		mEventString = generatePiece(resIds, values);
	}

	private String generateVenue() {
		if (Place.VENUE == mPlace.type) {
			return mPlace.value;
		}

		return null;
	}

	private String generateCity() {
		if (Place.CITY == mPlace.type) {
			return mPlace.value;
		}

		return null;
	}

	private void generatePlace() {
		final int resIds[] = { R.array.place_rules_masks,
				R.array.place_rules_fields, R.array.place_rules };
		String values[] = new String[] { generateVenue(), generateCity() };

		mPlaceString = generatePiece(resIds, values);
	}

	private String generateBirthdayString(Date birthday) {
		if (null == birthday) {
			return null;
		}

		int[] counts = SimpleDateTime.getNeturalAge(birthday);
		final int[] resIds = { R.plurals.year_age, R.plurals.month_age,
				R.plurals.week_age, R.plurals.day_age };

		if (counts[0] > AGE_MAX) {
			return null;
		}

		for (int index = 0; index < resIds.length; index++) {
			if (counts[index] > 0) {
				return SimpleResources.getResources(mCtx).getQuantityString(
						resIds[index], counts[index], counts[index]);
			}
		}

		return null;
	}

	private String generatePersonString(Person person) {
		String birthdayString = generateBirthdayString(person.birthday);
		String nameString = person.name;

		if (!TextUtils.isEmpty(birthdayString)) {
			return SimpleResources.getResources(mCtx).getString(
					R.string.each_person_piece, nameString, birthdayString);
		}

		return nameString;
	}

	private void generatePersons() {
		int num = mPersons.length;
		final int resIds[] = { R.array.person_rules_masks,
				R.array.person_rules_fields, R.array.person_rules };

		int index = 0;
		String values[] = new String[MAX_PERSON];
		for (; index < MAX_PERSON && index < num; index++) {
			values[index] = generatePersonString(mPersons[index]);
		}
		for (; index < MAX_PERSON; index++) {
			values[index] = null;
		}

		mPersonsString = generatePiece(resIds, values);
	}

	private String generate() {
		int index = SimpleResources.getIndexByMaskValue(mCtx,
				R.array.sentence_rules_masks, getRuleId());

		Log.v("Rule Id = " + Integer.toHexString(mRuleId));
		if (index >= 0) {
			String sentence = SimpleResources.getStringValue(mCtx,
					R.array.sentence_rules, index);

			if (0 != (DATE_FIELD_MASK & mRuleId)) {
				generateDateTime();
				Log.v("DateTime = " + mDateTimeString);
			}

			if (0 != (EVENT_FIELD_MASK & mRuleId)) {
				generateEvent();
				Log.v("Event = " + mEventString);
			}

			if (0 != (PLACE_FIELD_MASK & mRuleId)) {
				generatePlace();
				Log.v("Place = " + mPlaceString);
			}

			if (0 != (PERSON_FIELD_MASK & mRuleId)) {
				generatePersons();
				Log.v("Persons = " + mPersonsString);
			}

			sentence = String.format(sentence, mDateTimeString, mEventString,
					mPlaceString, mPersonsString);
			Log.v("Sentence = " + sentence);
			return sentence;
		}

		Log.e(TAG, "Do NOT support " + Integer.toHexString(mRuleId));
		return null;
	}

	public static String generate(Context ctx, Date date, String event,
			Place place, Person[] persons) {
		return (new CaptionMaker(ctx)).setDate(date).setEvent(event)
				.setPlace(place).setPersons(persons).generate();
	}

	// -----------------------------------------------------------------------------
	// Place
	// -----------------------------------------------------------------------------
	public static class Place {
		public final static int VENUE = 0;
		public final static int CITY = 1;

		public int type;
		public String value;
	}

	// -----------------------------------------------------------------------------
	// Person
	// -----------------------------------------------------------------------------
	public static class Person {
		public Date birthday;
		public String name;
	}

}
