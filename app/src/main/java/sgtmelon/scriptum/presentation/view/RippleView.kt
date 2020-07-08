package sgtmelon.scriptum.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import sgtmelon.scriptum.R
import kotlin.math.min

/**
 * View element which uses inside [RippleContainer].
 */
class RippleView(context: Context) : View(context) {

    var paint: Paint? = null

    init {
        visibility = INVISIBLE
    }

    override fun onDraw(canvas: Canvas) = (min(width, height) / 2).toFloat().let {
        /**
         * Remove from radius small space for prevent cutting circle sides. 
         * Because of that we need circle a bit smaller then view.  
         */
        val radius = it - resources.getDimension(R.dimen.radius_2dp)
        canvas.drawCircle(it, it, radius, paint ?: return@let)
    }

}