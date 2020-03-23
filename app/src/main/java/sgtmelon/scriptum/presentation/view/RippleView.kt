package sgtmelon.scriptum.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import sgtmelon.scriptum.R
import kotlin.math.min

/**
 * View element which uses inside [RippleContainer]
 */
class RippleView(context: Context) : View(context) {

    var paint: Paint? = null

    init {
        visibility = INVISIBLE
    }

    /**
     * Remove from radius small space for better performance
     */
    override fun onDraw(canvas: Canvas) = (min(width, height) / 2).toFloat().let {
        canvas.drawCircle(it, it, it - resources.getDimension(R.dimen.radius_2dp), paint ?: return@let)
    }

}