package sgtmelon.scriptum.cleanup.domain.model.state

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * State for from/to colors of menu
 */
class MenuColorState(var from: Int = ND_VALUE, var to: Int = ND_VALUE) {

    fun isReady() = from != to

    fun blend(@FloatRange(from = 0.0, to = 1.0) ratio: Float) = blend(from, to, ratio)

    /**
     * Get middle RGB color with dependency of [ratio] value.
     *
     * [ratio]  - position of transformation
     * [from]   - color from which transformations goes
     * [to]     - final color destination
     */
    @ColorInt
    private fun blend(
        from: Int,
        to: Int,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float
    ): Int {
        val inverseRatio = 1f - ratio

        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    companion object {
        const val ND_VALUE = 0
    }
}