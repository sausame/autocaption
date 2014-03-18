package com.example.autocaption;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;

public class AutoCaption {

    public final static int AGE_MAX = 18;

    public final static int MAX_PERSON = 3;

    private Date mDate;
    private String mEvent;
    private Place mPlace;
    private Person[] mPersons;

    private String mDateTimeString;
    private String mEventString;
    private String mPlaceTypeString;
    private String mPlaceValueString;
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

    private AutoCaption setPlace(Place place) {
        mPlace = place;
        return this;
    }

    private AutoCaption setPersons(Person[] persons) {
        mPersons = persons;
        return this;
    }

    private String generateBriefDate() {
		String briefString;

        // 1. Check the named days.
        int diff = SimpleDateTime.compareToday(mDate);

        briefString = SimpleResources.getStringValue(mCtx,
                R.array.named_dates_string_values, -1 * diff);
		if (!TextUtils.isEmpty(briefString)) {
            return briefString;
        }

        // 2. Check if it's in this week
        diff = SimpleDateTime.compareFirstDayInThisWeek(mDate);

        briefString = SimpleResources.getStringValue(mCtx,
                R.array.days_in_week_string_values, diff);
		if (!TextUtils.isEmpty(briefString)) {
            return briefString;
        }

        // 3. Check if it's in the last week.
        diff = SimpleDateTime.compareFirstDayInLastWeek(mDate);

        briefString = SimpleResources.getStringValue(mCtx,
                R.array.days_in_last_week_string_values, diff);
		if (!TextUtils.isEmpty(briefString)) {
            return briefString;
		}

        // 4. Others.
		return null;
    }

    private String generateDate() {
        return SimpleDateTime.getDayString(mDate);
    }

    private String generateTime() {
        int hour = SimpleDateTime.getHourOfDay(mDate);
        int index = SimpleResources.getIndexByValue(mCtx, R.array.hour_indexs,
                hour);
        return SimpleResources.getStringValue(mCtx, R.array.hour_values, index);
    }

    private void generateDateTime() {
        String briefString = generateBriefDate();
        String dateString = generateDate();
        String timeString = generateTime();

		if (TextUtils.isEmpty(briefString)) {
			mDateTimeString = SimpleResources.getResources(mCtx).getString(R.string.date_piece,
						dateString);
		} else {
			mDateTimeString = SimpleResources.getResources(mCtx).getString(R.string.date_piece_with_brief_date,
					briefString, timeString, dateString);
		}
    }

    private void generateEvent() {
        mEventString = mEvent;
    }

    private void generatePlace() {
        mPlaceTypeString = "";
        mPlaceValueString = mPlace.value;
    }

    private String generateBirthdayString(Date birthday) {
        int[] counts = SimpleDateTime.getNeturalAge(birthday);
        final int[] resIds = {R.plurals.year_age, R.plurals.month_age,
                R.plurals.day_age};

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
        String birthdayString = generateBirthdayString(person.birthday);
        String nameString = person.name;

        return SimpleResources.getResources(mCtx).getString(
                R.string.each_person_piece, nameString, birthdayString);
    }

    private void generatePersons() {
        int num = mPersons.length;
        mPersonStrings = new String[num];

        for (int index = 0; index < MAX_PERSON && index < num; index++) {
            mPersonStrings[index] = generatePersonString(mPersons[index]);
        }

        switch (mPersons.length) {
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

    private String generateSentenceWithDateOnly() {
		generateDateTime();

		return mDateTimeString;
    }

    private String generateSentenceWithDateEventPlacePerson() {
        String sentence;

        generateDateTime();
        generateEvent();
        generatePlace();
        generatePersons();

        switch (mPersons.length) {
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

        return mPersonsString;
    }

    public static String generateSentenceWithDateOnly(Context ctx, Date date) {
        return (new AutoCaption(ctx)).setDate(date).generateSentenceWithDateOnly();
    }

    public static String generateSentenceWithDateEventPlacePerson(Context ctx, Date date,
                                                                  String event, Place place, Person[] persons) {
        return (new AutoCaption(ctx)).setDate(date).setEvent(event)
                .setPlace(place).setPersons(persons).generateSentenceWithDateEventPlacePerson();
    }

}
