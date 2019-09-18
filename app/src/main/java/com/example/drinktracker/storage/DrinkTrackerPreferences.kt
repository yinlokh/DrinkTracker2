package com.example.drinktracker.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * Preferences to store drink count
 */
class DrinkTrackerPreferences {

    companion object {
        private val SHARED_PREF_NAME = "DrinkTrackerPref"
        private val PREF_KEY_BAC = "bac"
        private val PREF_KEY_LAST_UPDATE_TIME_MS = "last_update_time"
        private val PREF_KEY_START_TIME_MS = "start_time"
        private val PREF_KEY_TOTAL_DRINK_COUNT = "totalDrinKCount"
    }

    internal var bac: Float = 0.0f
    internal var sharedPreferences: SharedPreferences? = null
    internal var lastUpdateTimeMs: Long = 0
    internal var startTimeMs: Long = 0
    internal var totalDrinkCount: Float = 0f

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        bac = sharedPreferences?.getFloat(PREF_KEY_BAC, 0f)?:0f
        lastUpdateTimeMs = sharedPreferences?.getLong(PREF_KEY_LAST_UPDATE_TIME_MS, 0)?:0
        startTimeMs = sharedPreferences?.getLong(PREF_KEY_START_TIME_MS, 0)?:0
        totalDrinkCount = sharedPreferences?.getFloat(PREF_KEY_TOTAL_DRINK_COUNT, 0f)?:0f
    }

    fun setLastUpdateTimeMs(timeMs: Long) {
        this.lastUpdateTimeMs = timeMs
        sharedPreferences?.edit()
            ?.putLong(PREF_KEY_LAST_UPDATE_TIME_MS, timeMs)
            ?.apply()
    }

    fun setStartTimeMs(timeMs: Long) {
        this.startTimeMs = timeMs
        sharedPreferences?.edit()
            ?.putLong(PREF_KEY_START_TIME_MS, timeMs)
            ?.apply()
    }

    fun setTotalDrinkCount(drinkCount: Float) {
        this.totalDrinkCount = Math.max(0f, drinkCount)
        sharedPreferences?.edit()
            ?.putFloat(PREF_KEY_TOTAL_DRINK_COUNT, totalDrinkCount)
            ?.apply()
    }

    fun setBac(bac : Float) {
        this.bac = bac
        sharedPreferences?.edit()
            ?.putFloat(PREF_KEY_BAC, bac)
            ?.apply()
    }
}
