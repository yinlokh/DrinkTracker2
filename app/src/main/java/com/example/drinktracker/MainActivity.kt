package com.example.drinktracker

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.drinktracker.SwipeView.Listener
import com.example.drinktracker.calculator.DrinkStatsCalculator
import com.example.drinktracker.storage.DrinkTrackerPreferences
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), Listener {

    private val preferences : DrinkTrackerPreferences =
        DrinkTrackerPreferences()
    private val detaches : PublishSubject<Unit> = PublishSubject.create()

    private val bacFormat = DecimalFormat()
    private val drinksFormat = DecimalFormat()

    private var calculator : DrinkStatsCalculator? = null

    init {
        bacFormat.setMaximumFractionDigits(3)
        drinksFormat.setMaximumFractionDigits(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences.init(this)
        calculator = DrinkStatsCalculator(preferences)
        setContentView(R.layout.activity_main)
        swipe_view.listener = this
    }

    override fun onResume() {
        super.onResume()

        update()
        Observable.interval(1L, TimeUnit.MINUTES)
            .observeOn(AndroidSchedulers.mainThread())
            .takeUntil(detaches)
            .subscribe{unused ->
                update()
            }
    }

    override fun onPause() {
        super.onPause()
        detaches.onNext(Unit)
    }

    override fun onOffsetUpdate(offset: Float) {
        drink_animation.setOffset(offset)
    }

    override fun swipeRight() {
        Snackbar.make(swipe_view, "Adding 1 drink", Snackbar.LENGTH_LONG).show()
        changeCount(1F)
    }

    override fun swipeLeft() {
        Snackbar.make(swipe_view, "Removing 1 drink", Snackbar.LENGTH_LONG).show()
        changeCount(-1F)
    }

    private fun changeCount(change : Float) {
        calculator?.addDrink(change)
        update()
    }

    private fun update() {
        calculator?.refreshStats()
        val stats = calculator?.getStats()
        drinks_counter.setText(drinksFormat.format(Math.min(stats?.totalDrinks?:0f, 99.99F)))
        bac.setText(bacFormat.format(stats?.bac?:0f))
        totalTime.setText(String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(stats?.totalTimeMs?:0),
            TimeUnit.MILLISECONDS.toMinutes(stats?.totalTimeMs?:0) % TimeUnit.HOURS.toMinutes(1)))
    }
}
