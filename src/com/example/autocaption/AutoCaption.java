package com.example.autocaption;

import android.text.format.Time;

public class AutoCaption {

	public static String generateSentence() {
		return null;
	}

	public interface Sentence {
		public String generate();
	}

	public static class Sentence1 implements Sentence{
		public Sentence1 setTime(Time time) {
			mTime = time;
			return this;
		}

		private void generateDate() {
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

	public static class Sentence2 implements Sentence{
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

	public static String generateSentence1(Time time) {
		return (new Sentence1()).setTime(time).generate();
	}

	public static String generateSentence2(Time time, String event, Venue venue, String[] persons) {
		return (new Sentence2()).setTime(time).setEvent(event).setVenue(venue).setPersons(persons).generate();
	}

}

