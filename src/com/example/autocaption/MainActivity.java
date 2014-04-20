package com.example.autocaption;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.autocaption.CaptionMaker.Person;
import com.example.autocaption.CaptionMaker.Place;

public class MainActivity extends Activity {
	private final static int MAX_PERSON_NUM = 3;

	private int mClickedButtonResId = 0;

	// where we display the selected date and time
	private Button mDateButton;
	private Button mTimeButton;

	private EditText mPersonNameEditTextGroup[] = new EditText[MAX_PERSON_NUM];
	private Button mBirthdayButtonGroup[] = new Button[MAX_PERSON_NUM];
	private Date mBirthdayGroup[] = new Date[MAX_PERSON_NUM];

	// date and time
	private Date mDate = new Date();
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	static final int TIME_12_DIALOG_ID = 0;
	static final int TIME_24_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 2;

	//
	private TextView mSentenceTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDateButton = (Button) findViewById(R.id.pickDate);
		mTimeButton = (Button) findViewById(R.id.pickTime24);

		setDialogOnClickListener(R.id.pickDate, DATE_DIALOG_ID);
		setDialogOnClickListener(R.id.pickTime24, TIME_24_DIALOG_ID);

		mPersonNameEditTextGroup[0] = (EditText) findViewById(R.id.person1);
		mPersonNameEditTextGroup[1] = (EditText) findViewById(R.id.person2);
		mPersonNameEditTextGroup[2] = (EditText) findViewById(R.id.person3);

		mBirthdayButtonGroup[0] = (Button) findViewById(R.id.birthday1);
		mBirthdayButtonGroup[1] = (Button) findViewById(R.id.birthday2);
		mBirthdayButtonGroup[2] = (Button) findViewById(R.id.birthday3);

		setDialogOnClickListener(R.id.birthday1, DATE_DIALOG_ID);
		setDialogOnClickListener(R.id.birthday2, DATE_DIALOG_ID);
		setDialogOnClickListener(R.id.birthday3, DATE_DIALOG_ID);

		Button b = (Button) findViewById(R.id.submit);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				submit();
			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		mSentenceTextView = (TextView) findViewById(R.id.sentence);

		updateDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setDialogOnClickListener(int buttonId, final int dialogId) {
		Button b = (Button) findViewById(buttonId);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mClickedButtonResId = v.getId();
				showDialog(dialogId);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_12_DIALOG_ID:
		case TIME_24_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					id == TIME_24_DIALOG_ID);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TIME_12_DIALOG_ID:
		case TIME_24_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private Date updateDate(Button button, int year, int monthOfYear,
			int dayOfMonth) {
		button.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(monthOfYear + 1).append("-").append(dayOfMonth)
				.append("-").append(year));

		return SimpleDateTime.getDate(year, monthOfYear, dayOfMonth, 0, 0);
	}

	private void updateDisplay() {
		mDateButton.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear));

		mTimeButton.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));

		mDate = SimpleDateTime.getDate(mYear, mMonth, mDay, mHour, mMinute);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			if (R.id.pickDate == mClickedButtonResId) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
			} else if (R.id.birthday1 == mClickedButtonResId) {
				mBirthdayGroup[0] = updateDate(mBirthdayButtonGroup[0], year,
						monthOfYear, dayOfMonth);
			} else if (R.id.birthday2 == mClickedButtonResId) {
				mBirthdayGroup[1] = updateDate(mBirthdayButtonGroup[1], year,
						monthOfYear, dayOfMonth);
			} else if (R.id.birthday3 == mClickedButtonResId) {
				mBirthdayGroup[2] = updateDate(mBirthdayButtonGroup[2], year,
						monthOfYear, dayOfMonth);
			}
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	protected void submit() {

		// Date
		Log.v("Date: " + mDate);

		// Event
		String event = ((EditText) this.findViewById(R.id.event)).getText()
				.toString();

		Log.v("Event: " + event);

		// Place
		Place place = new Place();
		int selectId = ((RadioGroup) this.findViewById(R.id.place_type_radio))
				.getCheckedRadioButtonId();
		if (selectId == findViewById(R.id.venue_type).getId()) {
			place.type = Place.VENUE;
		} else {
			place.type = Place.CITY;
		}
		place.value = ((EditText) this.findViewById(R.id.place)).getText()
				.toString();

		Log.v("Place: " + place.type + ", " + place.value);

		// Persons
		Log.v("Persons:");

		ArrayList<Person> personList = new ArrayList<Person>();
		for (int index = 0; index < MAX_PERSON_NUM; index++) {
			String name = mPersonNameEditTextGroup[index].getText().toString();
			if (!TextUtils.isEmpty(name)) {
				Person person = new Person();
				person.name = name;
				person.birthday = mBirthdayGroup[index];

				Log.v("NO." + personList.size() + ":" + name + ", "
						+ mBirthdayGroup[index]);
				personList.add(person);
			}
		}

		String personNameStr = ((EditText) this
				.findViewById(R.id.other_persons)).getText().toString();
		if (!TextUtils.isEmpty(personNameStr)) {
			String[] personNames = personNameStr.split(";");
			for (int index = 0; index < personNames.length; index++) {
				Person person = new Person();
				person.name = personNames[index];
				person.birthday = null;

				Log.v("NO." + personList.size() + ":" + personNames[index]);
				personList.add(person);
			}
		}

		Person[] persons = (Person[]) personList.toArray(new Person[personList
				.size()]);

		String sentence = CaptionMaker.generate(this, mDate, event, place,
				persons);

		Log.v("Sentence: " + sentence);

		mSentenceTextView.setText(sentence);
	}
}
