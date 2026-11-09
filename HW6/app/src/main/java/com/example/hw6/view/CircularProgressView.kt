package com.example.hw6.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(0, 100)
            invalidate()
        }

    var centerText: String = ""
        set(value) {
            field = value
            invalidate()
        }

    private val strokeWidthDp = 18f
    private var strokeWidthPx = 0f

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#E0E0E0")
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val oval = RectF()

    init {
        strokeWidthPx = strokeWidthDp * context.resources.displayMetrics.density
        backgroundPaint.strokeWidth = strokeWidthPx
        progressPaint.strokeWidth = strokeWidthPx
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = strokeWidthPx / 2f + 16f
        oval.set(padding, padding, w - padding, h - padding)

        val cx = w / 2f
        val cy = h / 2f

        val colors = intArrayOf(
            Color.parseColor("#00E5FF"), // Cyan
            Color.parseColor("#7C4DFF"), // Purple
            Color.parseColor("#00E5FF")  // Cyan
        )
        val shader = SweepGradient(cx, cy, colors, null)
        progressPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background ring track
        canvas.drawOval(oval, backgroundPaint)

        // Draw progress arc starting from top (-90 degrees)
        val sweepAngle = (progress / 100f) * 360f
        if (sweepAngle > 0f) {
            canvas.save()
            canvas.rotate(-90f, oval.centerX(), oval.centerY())
            canvas.drawArc(oval, 0f, sweepAngle, false, progressPaint)
            canvas.restore()
        }

        // Draw center text
        val textToDraw = centerText
        if (textToDraw.isNotEmpty()) {
            val baseDensity = context.resources.displayMetrics.density
            val textSize = if (textToDraw.length > 5) 28f * baseDensity else 42f * baseDensity
            textPaint.textSize = textSize

            val textY = oval.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2f)
            canvas.drawText(textToDraw, oval.centerX(), textY, textPaint)
        }
    }
}
