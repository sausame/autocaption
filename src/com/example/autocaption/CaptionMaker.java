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
	public final static int VENUE_FIELD_MASK = 0x00F0;
	public final static int PERSON_FIELD_MASK = 0x000F;

	public final static String DATE_PIECE = "[DATE-PIECE]";
	public final static String EVENT_PIECE = "[EVENT-PIECE]";
	public final static String VENUE_PIECE = "[VENUE-PIECE]";
	public final static String PERSON_piece = "[PERSON-PIECE]";

	public final static String NAMED_DATE = "[NAMED-DATE]";
	public final static String UPPER_NAMED_DATE = "[UPPER-NAMED-DATE]";
	public final static String LOWER_NAMED_DATE = "[LOWER-NAMED-DATE]";
	public final static String DATE = "[DATE]";
	public final static String TIME = "[TIME]";

	public final static String VENUS = "[VENUS]";
	public final static String CITY = "[CITY]";

	private int mRuleId;

	private Date mDate;
	private String mEvent;
	private Venue mVenue;
	private Person[] mPersons;

	private String mDateTimeString;
	private String mEventString;
	private String mVenueString;
	private String mVenueValueString;
	private String[] mPersonStrings;
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

	private CaptionMaker setVenue(Venue venue) {
		mVenue = venue;
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

		if (mVenue != null && !TextUtils.isEmpty(mVenue.value)) {
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
		int[] fields = SimpleResources.getIntArray(mCtx,
				resIds[DATE_RULES_FIELDS]);

		for (int ruleIndex = 0; ruleIndex < rulesMasks.length; ruleIndex++) {
			if (mRuleId == (mRuleId & rulesMasks[ruleIndex])) {
				int field = fields[ruleIndex];
				int fieldMask = 0xf;
				boolean found = true;

				for (int fieldIndex = values.length - 1; fieldIndex >= 0; fieldIndex--) {
					if (0 != (field & fieldMask)) {
						if (TextUtils.isEmpty(values[fieldIndex])) {
							found = false;
							break;
						}
					}
					fieldMask <<= 4;
				}

				if (found) {
					String piece = SimpleResources.getStringValue(mCtx,
							resIds[RULES], ruleIndex);
					for (int fieldIndex = values.length - 1; fieldIndex >= 0; fieldIndex--) {
						if (0 != (field & fieldMask)) {
							if (TextUtils.isEmpty(values[fieldIndex])) {
								found = false;
								break;
							}
						}

						fieldMask <<= 4;
					}

					return piece;
				}
			}
		}

		return null;
	}

	private String generateBriefDate(int namedDateResId, int daysInWeekResId,
			int daysInLastWeekResId) {
		String briefString;

		// 1. Check the named days.
		int diff = SimpleDateTime.compareToday(mDate);

		briefString = SimpleResources.getStringValue(mCtx, namedDateResId, -1
				* diff);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		// 2. Check if it's in this week
		diff = SimpleDateTime.compareFirstDayInThisWeek(mDate);

		briefString = SimpleResources.getStringValue(mCtx, daysInWeekResId,
				diff);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		// 3. Check if it's in the last week.
		diff = SimpleDateTime.compareFirstDayInLastWeek(mDate);

		briefString = SimpleResources.getStringValue(mCtx, daysInLastWeekResId,
				diff);
		if (!TextUtils.isEmpty(briefString)) {
			return briefString;
		}

		// 4. Others.
		return null;
	}

	private String generateNameDate() {
		return generateBriefDate(R.array.named_dates_string_values,
				R.array.days_in_week_string_values,
				R.array.days_in_last_week_string_values);
	}

	private String generateLowerNameDate() {
		return generateBriefDate(R.array.lower_named_dates_string_values,
				R.array.days_in_week_string_values,
				R.array.days_in_last_week_string_values);
	}

	private String generateUpperNameDate() {
		return generateBriefDate(R.array.upper_named_dates_string_values,
				R.array.days_in_week_string_values,
				R.array.days_in_last_week_string_values);
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
		String values[] = new String[] { generateNameDate(),
				generateLowerNameDate(), generateUpperNameDate(),
				generateDate(), generateTime() };

		mDateTimeString = generatePiece(resIds, values);
	}

	private void generateEvent() {
	}

	private void generateVenue() {
	}

	private void generatePersons() {
	}

	private String implementDate(String sentence) {
		if (0 == (DATE_FIELD_MASK & mRuleId)) {
			return sentence;
		}

		generateDateTime();
		return sentence.replace(DATE_PIECE, mDateTimeString);
	}

	private String implementEvent(String sentence) {
		if (0 == (EVENT_FIELD_MASK & mRuleId)) {
			return sentence;
		}

		generateEvent();
		return sentence.replace(DATE_PIECE, mEventString);
	}

	private String implementVenue(String sentence) {
		if (0 == (VENUE_FIELD_MASK & mRuleId)) {
			return sentence;
		}

		generateVenue();
		return sentence.replace(DATE_PIECE, mVenueString);
	}

	private String implementPersons(String sentence) {
		if (0 == (PERSON_FIELD_MASK & mRuleId)) {
			return sentence;
		}

		generatePersons();
		return sentence.replace(DATE_PIECE, mPersonsString);
	}

	private String generate() {
		int index = SimpleResources.getIndexByValue(mCtx,
				R.array.sentence_rules_masks, getRuleId());

		if (index >= 0) {
			String sentence = SimpleResources.getStringValue(mCtx,
					R.array.sentence_rules, index);
			return implementPersons(implementVenue(implementEvent(implementDate(sentence))));
		}

		Log.e(TAG, "Do NOT support " + Integer.toHexString(mRuleId));
		return null;
	}

	public static String generate(Context ctx, Date date, String event,
			Venue venue, Person[] persons) {
		return (new CaptionMaker(ctx)).setDate(date).setEvent(event)
				.setVenue(venue).setPersons(persons).generate();
	}

}
