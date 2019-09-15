package com.example.drinktracker

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.FloatRange

class DrinkAnimationView : FrameLayout {

    private var offset: Float = 0.toFloat()
    private var leftDrink: DrinkImageView? = null
    private var centerDrink: DrinkImageView? = null
    private var rightDrink: DrinkImageView? = null


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onFinishInflate() {
        super.onFinishInflate()

        leftDrink = findViewById(R.id.drink_left)
        centerDrink = findViewById(R.id.drink_middle)
        rightDrink = findViewById(R.id.drink_right)
        setOffset(0f)
    }

    fun setOffset(@FloatRange(from = -1.0, to = 1.0) offset: Float) {
        this.offset = offset
        leftDrink?.fillPercent = 1f
        leftDrink?.translationX = (width / 2 + leftDrink!!.width / 2) * offset - width / 2 - (leftDrink!!.width / 2)
        leftDrink?.invalidate()

        centerDrink?.translationX = (width / 2 + centerDrink!!.width) * offset
        centerDrink?.fillPercent = 1f - offset
        centerDrink?.invalidate()

        rightDrink?.translationX = (width / 2 + rightDrink!!.width / 2) * offset + width / 2 + rightDrink!!.width / 2
        rightDrink?.fillPercent = -1f * offset
        rightDrink?.invalidate()
    }
}
