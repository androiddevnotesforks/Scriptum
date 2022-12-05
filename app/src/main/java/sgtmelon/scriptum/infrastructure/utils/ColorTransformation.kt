package sgtmelon.scriptum.infrastructure.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * Transform colors [from] one [to] another.
 */
class ColorTransformation(var from: Int = ND_VALUE, var to: Int = ND_VALUE) {

    fun isReady() = from != to

    operator fun get(@FloatRange(from = 0.0, to = 1.0) ratio: Float) = get(from, to, ratio)

    /**
     * Get middle RGB color with dependency of [ratio] value.
     *
     * [ratio]  - position of transformation
     * [from]   - color from which transformations goes
     * [to]     - final color destination
     */
    @ColorInt
    private fun get(from: Int, to: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
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