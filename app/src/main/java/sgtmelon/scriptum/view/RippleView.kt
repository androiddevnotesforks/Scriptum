package sgtmelon.scriptum.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

/**
 * View элемент использующийся внутри [RippleContainer]
 *
 * @author SerjantArbuz
 */
class RippleView(context: Context) : View(context) {

    lateinit var paint: Paint

    init {
        visibility = INVISIBLE
    }

    override fun onDraw(canvas: Canvas) = (Math.min(width, height) / 2).toFloat().let {
        canvas.drawCircle(it, it, it, paint)
    }

}