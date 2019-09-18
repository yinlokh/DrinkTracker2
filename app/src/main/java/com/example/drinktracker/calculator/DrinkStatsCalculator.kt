package com.example.drinktracker.calculator

import com.example.drinktracker.storage.DrinkTrackerPreferences
import java.util.concurrent.TimeUnit

class DrinkStatsCalculator (val preferences : DrinkTrackerPreferences){

    companion object {
        val STD_DRINK_OZ : Float = 0.5f
        val LIQUID_TO_WT_OZ : Float = 5.14f
        val FEMALE_DISTRIBUTION_RATIO : Float = 0.66f
        val MALE_DISTRIBUTION_RATIO : Float = 0.73f
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
        val bacReduction = 0.015f * elapsedTimeMs / TimeUnit.HOURS.toMillis(1)
        val currentBac = Math.max(0f, bac - bacReduction)
        preferences.setBac(currentBac)
        preferences.setLastUpdateTimeMs(currentTImeMs)
        lastUpdateTimeMs = currentTImeMs
    }

    fun addDrink(amount : Float) {
        refreshStats()
        val previousDrinks = totalDrinks
        totalDrinks += amount
        totalDrinks = Math.max(0f, totalDrinks)

        val actualChange = totalDrinks - previousDrinks
        bac += actualChange * STD_DRINK_OZ * LIQUID_TO_WT_OZ / weightLbs * MALE_DISTRIBUTION_RATIO
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