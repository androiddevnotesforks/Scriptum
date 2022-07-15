package sgtmelon.scriptum.cleanup.domain.model.state

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * State for from/to colors of menu
 */
class MenuColorState(var from: Int = ND_VALUE, var to: Int = ND_VALUE) {

    fun isDifferent() = from != to

    fun blend(@FloatRange(from = 0.0, to = 1.0) ratio: Float) = from.blend(to, ratio)

    /**
     * Get middle RGB color with dependency of [ratio] value.
     *
     * [ratio] - position of transformation
     * [this] - color from which transformations goes
     */
    @ColorInt private fun Int.blend(to: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
        val inverseRatio = 1f - ratio

        val r = Color.red(to) * ratio + Color.red(this) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(this) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(this) * inverseRatio

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    companion object {
        const val ND_VALUE = 0
    }

}