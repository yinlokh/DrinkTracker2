package com.example.drinktracker

import android.os.Bundle
import android.os.SystemClock
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.drinktracker.SwipeView.Listener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), Listener {

    private val preferences : DTPreferences = DTPreferences()
    private val detaches : PublishSubject<Unit> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences.init(this)
        setContentView(R.layout.activity_main)
        swipe_view.listener = this
    }

    override fun onResume() {
        super.onResume()

        refreshCount()
        Observable.interval(3L, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .takeUntil(detaches)
            .subscribe{unused -> refreshCount()}
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
        preferences.setTotalDrinkCount(preferences.totalDrinkCount + 1)
    }

    override fun swipeLeft() {
        Snackbar.make(swipe_view, "Removing 1 drink", Snackbar.LENGTH_LONG).show()
        changeCount(-1F)
        preferences.setTotalDrinkCount(preferences.totalDrinkCount - 1)
    }

    private fun changeCount(change : Float) {
        refreshCount()
        val current = Math.max(0F, preferences.drinkCount + change)
        preferences.setDrinkCount(current)
        preferences.setTime(SystemClock.elapsedRealtime())
        update()
    }

    private fun refreshCount() {
        val elapsedTimeMs = SystemClock.elapsedRealtime()
        val processedDrinks = (elapsedTimeMs - preferences.timeMs).toDouble() / TimeUnit.HOURS.toMillis(1)
        val current = Math.max(0F, preferences.drinkCount - processedDrinks.toFloat())
        preferences.setDrinkCount(current)
        preferences.setTime(elapsedTimeMs)
        update()
    }

    private fun update() {
        val remainingDrinks = DecimalFormat()
        remainingDrinks.setMaximumFractionDigits(1)

        val bacFormat = DecimalFormat()
        bacFormat.setMaximumFractionDigits(2)

        drinks_counter.setText(remainingDrinks.format(Math.min(preferences.drinkCount, 99.99F)))
        bac.setText(bacFormat.format(calculateBAC()))
    }

    private fun calculateBAC() : Double {
        val drinkOz = preferences.drinkCount * 0.5f
        val weightLbs = 140
        val distributionRatio = 0.73 // 0.66 for women
        return (drinkOz * 5.14 / weightLbs * distributionRatio) - 0.015 * preferences.timeMs / TimeUnit.HOURS.toMillis(1)
    }
}
