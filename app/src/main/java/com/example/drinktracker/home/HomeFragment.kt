package com.example.drinktracker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.drinktracker.R
import com.example.drinktracker.SwipeView
import com.example.drinktracker.calculator.DrinkStatsCalculator
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.home_fragment.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class HomeFragment(
    val calculator: DrinkStatsCalculator) : Fragment(R.layout.home_fragment), SwipeView.Listener {

    private val detaches : PublishSubject<Unit> = PublishSubject.create()

    private val bacFormat = DecimalFormat()
    private val drinksFormat = DecimalFormat()

    init {
        bacFormat.setMaximumFractionDigits(3)
        drinksFormat.setMaximumFractionDigits(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val swipeView = view?.findViewById<SwipeView>(R.id.swipe_view)
        swipeView?.listener = this
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profile_button.setOnClickListener{view -> Activity
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
        calculator.addDrink(change)
        update()
    }

    private fun update() {
        calculator.refreshStats()
        val stats = calculator.getStats()
        drinks_counter.setText(drinksFormat.format(Math.min(stats?.totalDrinks?:0f, 99.99F)))
        bac.setText(bacFormat.format(stats?.bac?:0f))
        totalTime.setText(String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(stats?.totalTimeMs?:0),
            TimeUnit.MILLISECONDS.toMinutes(stats?.totalTimeMs?:0) % TimeUnit.HOURS.toMinutes(1)))
    }
}