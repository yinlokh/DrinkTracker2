package com.example.drinktracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preferences to store drink count
 */
public class DTPreferences {

    private static final String SHARED_PREF_NAME = "DrinkTrackerPref";
    private static final String PREF_KEY_TIME_MS = "time";
    private static final String PREF_KEY_DRINK_COUNT = "drinKCount";
    private static final String PREF_KEY_TOTAL_DRINK_COUNT = "totalDrinKCount";

    SharedPreferences sharedPreferences;

    long timeMs;
    float drinkCount;
    int totalDrinkCount;

    public DTPreferences() { }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        timeMs = sharedPreferences.getLong(PREF_KEY_TIME_MS, 0);
        drinkCount = sharedPreferences.getFloat(PREF_KEY_DRINK_COUNT, 0);
        totalDrinkCount = sharedPreferences.getInt(PREF_KEY_TOTAL_DRINK_COUNT, 0);
    }

    public void setTime(long timeMs) {
        this.timeMs = timeMs;
        sharedPreferences.edit()
                .putLong(PREF_KEY_TIME_MS, timeMs)
                .apply();
    }

    public void setDrinkCount(float drinkCount) {
        this.drinkCount = drinkCount;
        sharedPreferences.edit()
                .putFloat(PREF_KEY_DRINK_COUNT, drinkCount)
                .apply();
    }

    public void setTotalDrinkCount(int drinkCount) {
        this.totalDrinkCount = Math.max(0, drinkCount);
        sharedPreferences.edit()
                .putInt(PREF_KEY_TOTAL_DRINK_COUNT, totalDrinkCount)
                .apply();
    }
}
