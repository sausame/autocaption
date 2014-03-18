package com.example.autocaption;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    // where we display the selected date and time
    private Button mDateButton;
    private Button mTimeButton;

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
    private TextView mSentence1TextView;
    private TextView mSentence2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDateButton = (Button) findViewById(R.id.pickDate);
        mTimeButton = (Button) findViewById(R.id.pickTime24);

        setDialogOnClickListener(R.id.pickDate, DATE_DIALOG_ID);
        setDialogOnClickListener(R.id.pickTime24, TIME_24_DIALOG_ID);

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

        mSentence1TextView = (TextView) findViewById(R.id.sentence_1);
        mSentence2TextView = (TextView) findViewById(R.id.sentence_2);

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
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
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
        String event = ((EditText) this.findViewById(R.id.event)).getText()
                .toString();
        Place place = new Place();
        place.type = ((RadioGroup) this.findViewById(R.id.place_type_radio))
                .getCheckedRadioButtonId();
        place.value = ((EditText) this.findViewById(R.id.place)).getText()
                .toString();

        String personNameStr = ((EditText) this.findViewById(R.id.persons))
                .getText().toString();
        String[] personNames = personNameStr.split(";");

        Person[] persons = new Person[personNames.length];

        for (int index = 0; index < personNames.length; index++) {
            Log.v("oooooo", "---" + personNames[index]);
            persons[index] = new Person();
            persons[index].birthday = new Date();
            persons[index].name = personNames[index];
        }

        String sentence1 = AutoCaption.generateSentenceWithDateOnly(this, mDate);
        String sentence2 = null;

        Log.v("Date: " + mDate);
        Log.v("Event: " + event);
        Log.v("Place: " + place.type + ", " + place.value);
        Log.v("Person: " + personNameStr);

        mSentence1TextView.setText(sentence1);
        mSentence2TextView.setText(sentence2);
        
        int as[] = getResources().getIntArray(R.array.date_rules_masks);
        for (int a: as) {
        	Log.v("a = " + a + ", " + Integer.toHexString(a));
        }
        
        as = getResources().getIntArray(R.array.rule_ids);
        for (int a: as) {
        	Log.v("b = " + a + ", " + Integer.toHexString(a));
        }
    }
}
