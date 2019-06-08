package sgtmelon.scriptum.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import sgtmelon.scriptum.R

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

    /**
     * Из радиуса вычетается небольшой отступ, чтобы круг не был обрезанным
     */
    override fun onDraw(canvas: Canvas) = (Math.min(width, height) / 2).toFloat().let {
        canvas.drawCircle(it, it, it - resources.getDimension(R.dimen.radius_2dp), paint)
    }

}