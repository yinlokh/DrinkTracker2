package com.example.drinktracker.calculator

import com.example.drinktracker.storage.DrinkTrackerPreferences
import java.util.concurrent.TimeUnit


class DrinkStatsCalculator (val preferences : DrinkTrackerPreferences){

    /**
     * https://en.wikipedia.org/wiki/Blood_alcohol_content
     * Use Widmark's formula
     */
    companion object {
        val LB_TO_KG = 0.453592f
        val PCT_WATER_IN_BLOOD = 0.806f
        val SD_TO_GRAMS : Float = 1.2f
        val FEMALE_BODY_WATER_CONSTANT = 0.49f
        val FEMALE_METABOLISM_CONSTANT = 0.017f
        val MALE_BODY_WATER_CONSTANT = 0.58f
        val MALE_METABOLISM_CONSTANT = 0.015f
    }

    var bac : Float = 0f
    var lastUpdateTimeMs : Long = 0L
    var startTimeMs : Long = 0L
    var totalDrinks : Float = 0f
    val weightLbs : Int = 140

    init {
        bac = preferences.bac
        lastUpdateTimeMs = preferences.lastUpdateTimeMs
        startTimeMs = preferences.startTimeMs
        totalDrinks = preferences.totalDrinkCount
    }

    fun refreshStats() {
        val currentTImeMs = System.currentTimeMillis()
        val elapsedTimeMs = currentTImeMs - lastUpdateTimeMs
        val bacReduction = MALE_METABOLISM_CONSTANT * elapsedTimeMs / TimeUnit.HOURS.toMillis(1)
        val currentBac = Math.max(0f, bac - bacReduction)
        bac = currentBac
        preferences.setBac(bac)
        preferences.setLastUpdateTimeMs(currentTImeMs)
        lastUpdateTimeMs = currentTImeMs
    }

    fun addDrink(amount : Float) {
        refreshStats()
        val previousDrinks = totalDrinks
        totalDrinks += amount
        totalDrinks = Math.max(0f, totalDrinks)

        val actualChange = totalDrinks - previousDrinks
        bac +=
            actualChange * PCT_WATER_IN_BLOOD * SD_TO_GRAMS /
                    (weightLbs * LB_TO_KG * MALE_BODY_WATER_CONSTANT)
        bac = Math.max(0f, bac)
        preferences.setBac(bac)
        preferences.setTotalDrinkCount(Math.max(0f, totalDrinks))

        if(previousDrinks == 0f && totalDrinks > previousDrinks) {
            startTimeMs = System.currentTimeMillis()
            preferences.setStartTimeMs(startTimeMs)
        }
    }

    fun getStats() : DrinkStats =
        DrinkStats(
            bac,
            totalDrinks,
            if (totalDrinks > 0f) (System.currentTimeMillis() - startTimeMs) else 0L)
}