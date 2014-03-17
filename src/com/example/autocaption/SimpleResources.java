package com.example.autocaption;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

public class SimpleResources {

    public static String getStringValue(Context ctx, int arrayResId, int index) {
        String[] values = ctx.getResources().getStringArray(arrayResId);
        if (index >= 0 && index < values.length && !TextUtils.isEmpty(values[index])) {
            return values[index];
        }

        return null;
    }

    public static int getIndexByValue(Context ctx, int arrayResId, int value) {
        int[] values = ctx.getResources().getIntArray(arrayResId);

        for (int index = 0; index < values.length; index++) {
            if (value <= values[index]) {
                return index;
            }
        }

        return -1;
    }

    public static Resources getResources(Context ctx) {
        return ctx.getResources();
    }

}
