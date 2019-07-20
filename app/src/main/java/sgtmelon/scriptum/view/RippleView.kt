package sgtmelon.scriptum.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import sgtmelon.scriptum.R
import kotlin.math.min

/**
 * View элемент использующийся внутри [RippleContainer]
 *
 * @author SerjantArbuz
 */
class RippleView(context: Context) : View(context) {

    var paint: Paint? = null

    init {
        visibility = INVISIBLE
    }

    /**
     * Из радиуса вычетается небольшой отступ, чтобы круг не был обрезанным
     */
    override fun onDraw(canvas: Canvas) = (min(width, height) / 2).toFloat().let {
        canvas.drawCircle(it, it, it - resources.getDimension(R.dimen.radius_2dp), paint ?: return@let)
    }

}