package com.example.autocaption;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;

public class SimpleResources {

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

}
