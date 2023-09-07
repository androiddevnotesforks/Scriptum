package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.annotation.ColorInt
import sgtmelon.extensions.getDimen
import sgtmelon.scriptum.R
import kotlin.math.min

/**
 * View element which uses inside [RippleContainer].
 */
class RippleView(context: Context) : View(context) {

    init {
        visibility = INVISIBLE
    }

    private val paint = Paint()

    fun setup(paintStyle: Paint.Style, @ColorInt fillColor: Int) = apply {
        paint.isAntiAlias = true
        paint.style = paintStyle
        paint.strokeWidth = resources.getDimension(R.dimen.stroke_4dp)
        paint.color = fillColor
    }

    /**
     * Remove from radius small space for prevent cutting of circle sides.
     * That's why we draw circle a bit smaller then view.
     */
    override fun onDraw(canvas: Canvas) {
        val center = (min(width, height) / 2).toFloat()
        val radius = center - context.getDimen(value = 2f)

        canvas.drawCircle(center, center, radius, paint)
    }
}