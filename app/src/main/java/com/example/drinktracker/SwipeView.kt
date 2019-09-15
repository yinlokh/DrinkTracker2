package com.example.drinktracker

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import androidx.annotation.FloatRange

class SwipeView : View {

    var swiping = false
    var downPressX : Float = 0f
    var percentSwiped : Float = 0f
    var listener : Listener? = null
    var animator = ValueAnimator.ofFloat(0f, 1f)

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
                animator.end()
            }
            ACTION_MOVE -> percentSwiped = clamp(1.3f * (event.x - downPressX) / width.toFloat(), -1f, 1f)
            ACTION_UP -> {
                if (swiping) {
                    animator = ValueAnimator.ofFloat(percentSwiped, 0f)
                    animator.duration = 100
                    animator.repeatCount = 0
                    animator.addUpdateListener { animator ->
                        percentSwiped = animator.animatedValue as Float
                        listener?.onOffsetUpdate(percentSwiped)
                    }
                    animator.start()
                    swiping = false
                }
            }
        }

        if (swiping && percentSwiped == 1f) {
            listener?.swipeRight()
            swiping = false
        } else if (swiping && percentSwiped == -1f) {
            listener?.swipeLeft()
            swiping = false
        }

        listener?.onOffsetUpdate(percentSwiped)

        invalidate()
    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//
//        if (!swiping) {
//            return
//        }
//
//        var paint = Paint()
//        paint.style = Paint.Style.FILL
//        if (percentSwiped > 0) {
//            paint.color = Color.GRAY
//            canvas?.drawRect(0f, 0f, width * percentSwiped, height.toFloat(), paint)
//        } else if (percentSwiped < 0) {
//            paint.color = Color.BLACK
//            canvas?.drawRect((1f + percentSwiped) * width, 0f, width.toFloat(), height.toFloat(), paint)
//        }
//
//    }

    private fun clamp(value : Float, min : Float, max : Float) : Float =
        Math.max(min, Math.min(max, value))

    interface Listener {

        fun onOffsetUpdate(offset: Float)

        fun swipeLeft()

        fun swipeRight()
    }
}
