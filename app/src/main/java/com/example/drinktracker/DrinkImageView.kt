package com.example.drinktracker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class DrinkImageView : AppCompatImageView {

    var paint = Paint()
    var transferPaint = Paint()
    var fillPercent : Float = 0f

    init {
        paint.color = resources.getColor(R.color.beer3)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        transferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas : Canvas) {
        canvas.drawColor(Color.TRANSPARENT)

        var bufferBitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        var buffer = Canvas(bufferBitmap)
        buffer.drawColor(Color.TRANSPARENT)
        super.onDraw(buffer)

        buffer.drawRect(0f, height.toFloat() - (height.toFloat() * fillPercent), width.toFloat(), height.toFloat(), paint)
        canvas.drawBitmap(bufferBitmap, 0f, 0f, transferPaint)
    }
}
