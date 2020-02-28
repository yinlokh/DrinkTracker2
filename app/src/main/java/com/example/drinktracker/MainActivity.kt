package com.example.drinktracker

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.drinktracker.calculator.DrinkStatsCalculator
import com.example.drinktracker.home.HomeFragment
import com.example.drinktracker.storage.DrinkTrackerPreferences

class MainActivity : FragmentActivity(), NavHost {

    val navController : DrinkTrackerNavController = DrinkTrackerNavController(this)

    override fun getNavController(): NavController = navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val preferences = DrinkTrackerPreferences()
        preferences.init(this)
        val calculator = DrinkStatsCalculator(preferences)
        val homeFragment = HomeFragment(calculator);
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_host, homeFragment)
            .commit()
    }
}
