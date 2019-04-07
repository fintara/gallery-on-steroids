package com.tsovedenski.galleryonsteroids.features.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceView
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Tsvetan Ovedenski on 07/04/19.
 */
class WaveformView : SurfaceView {

    var background: Int = Color.BLACK
    var barBackground: Int = Color.YELLOW
    var bars: Int = 100
    var spacing: Int = 5
    var maxAmplitude = 4096f

    private val history by lazy {
        LinkedList<Int>(List(bars) { 0 })
    }

    private val paint by lazy { Paint().apply {
        style = Paint.Style.FILL
        color = barBackground
        isAntiAlias = true
    } }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun addAmplitude(value: Int): Unit = synchronized(history) {
        history.removeFirst()
        history.addLast(value)

        holder.lockCanvas()?.let { canvas ->
            drawWave(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawWave(canvas: Canvas) {
        canvas.drawColor(background)

        val barWidth = width / bars
        val halfHeight = height / 2

        history.forEachIndexed { i, amplitude ->
            val x = i * barWidth
            val y = (amplitude / maxAmplitude * halfHeight + halfHeight).roundToInt()

            val rect = Rect(x, y, x + barWidth - spacing, height - y)
            canvas.drawRect(rect, paint)
        }
    }
}