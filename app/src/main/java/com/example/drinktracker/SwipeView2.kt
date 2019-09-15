package com.example.drinktracker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View

class SwipeView2 : View {

    var swiping = false
    var downPressX : Float = 0f
    var percentSwiped : Float = 0f
    var listener : Listener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnTouchListener { _, event ->
            processTouchEvent(event)
            true}
    }

    private fun processTouchEvent(event : MotionEvent) {
        when(event.action) {
            ACTION_DOWN -> {
                downPressX = event.x
                swiping = true
            }
            ACTION_MOVE -> percentSwiped = clamp(1.3f * (event.x - downPressX) / width.toFloat(), -1f, 1f)
            ACTION_UP -> {
                percentSwiped = 0f
                swiping = false
            }
        }

        if (swiping && percentSwiped == 1f) {
            listener?.swipeRight()
            swiping = false
        } else if (swiping && percentSwiped == -1f) {
            listener?.swipeLeft()
            swiping = false
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var glassWidth = 32
        var glassHeight = 64
        var midX = width / 2
        var newGlassRight = percentSwiped * (midX + glassWidth / 2)
        var oldGlassRight = newGlassRight + midX
        var glassTop = height.toFloat() * 2 / 3

        var paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        canvas?.drawRect(oldGlassRight - glassWidth, glassTop, oldGlassRight, glassTop + glassHeight, paint)
        canvas?.drawRect(newGlassRight - glassWidth, glassTop, newGlassRight, glassTop + glassHeight, paint)
    }

    fun legacyDraw(canvas : Canvas?) {
        var paint = Paint()
        paint.style = Paint.Style.FILL
        if (percentSwiped > 0) {
            paint.color = Color.GRAY
            canvas?.drawRect(0f, 0f, width * percentSwiped, height.toFloat(), paint)
        } else if (percentSwiped < 0) {
            paint.color = Color.BLACK
            canvas?.drawRect((1f + percentSwiped) * width, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }

    private fun clamp(value : Float, min : Float, max : Float) : Float =
        Math.max(min, Math.min(max, value))

    interface Listener {

        fun swipeLeft()

        fun swipeRight()
    }
}
