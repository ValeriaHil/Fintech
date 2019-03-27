package com.example.lenovo.myproject.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.lenovo.myproject.R
import java.lang.Math.min
import kotlin.random.Random

class UserImage(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private var roundColor: Int = 0
    private var initials: String
    private val circlePaint: Paint = Paint()

    init {
        context.theme.obtainStyledAttributes(attributes, R.styleable.UserImage, 0, 0).apply {
            try {
                initials = getString(R.styleable.UserImage_initials) ?: "??"
                roundColor = getInt(R.styleable.UserImage_roundColor, R.color.black)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val y: Float = (measuredHeight / 2).toFloat()
        val x: Float = (measuredWidth / 2).toFloat()
        val radius = min(x, y)
        circlePaint.style = Paint.Style.FILL
        circlePaint.color = roundColor
        canvas?.drawCircle(x, y, radius, circlePaint)
        circlePaint.color = Color.WHITE
        circlePaint.textAlign = Paint.Align.CENTER
        circlePaint.textSize = radius
        canvas?.drawText(initials, x, y + radius / 3, circlePaint)
    }

    fun setRoundColor(color: Int) {
        roundColor = color
        invalidate()
    }

    fun setInitials(initials: String) {
        this.initials = initials
        invalidate()
        requestLayout()
    }

    companion object {
        fun transform(name: String): String {
            val separate = name.split(" ")
            var res = ""
            for (i in 0 until Math.min(separate.size, 2)) {
                res += separate[i][0]
            }
            return res
        }

        fun getColor(): Int {
            return Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        }
    }
}