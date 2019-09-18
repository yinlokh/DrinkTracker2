package com.example.drinktracker.calculator

/**
 * Stats returned by [DrinkStatsCalculator]
 */
data class DrinkStats(
    val bac : Float,
    val totalDrinks : Float,
    val totalTimeMs : Long) { }