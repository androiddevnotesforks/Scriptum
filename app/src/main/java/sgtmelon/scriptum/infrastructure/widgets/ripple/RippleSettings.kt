package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.view.View
import android.widget.RelativeLayout
import kotlin.math.max
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Model with settings/params for [RippleContainer].
 */
class RippleSettings(
    private val theme: ThemeDisplayed,
    private val hookView: View,
    private val parentView: View
) {

    /** Min and max sizes for future calculation. */
    private val minSize get() = hookView.width / 1.3
    private val maxSize get() = max(parentView.width, parentView.height)

    /** Count of ripple views need to create. */
    val viewCount get() = maxSize / (minSize / 2).toInt()

    /** Duration of animation (how long it takes for one ripple reaching the final scale). */
    val duration get() = 1000L * viewCount / 2

    /** Delay before start ripple run. */
    val delay get() = duration / viewCount

    fun getDelay(position: Int): Long = position * delay

    /**
     * Scale factor needed because on each [theme] we create unique ripple effect with
     * different sizes.
     */
    val scaleTo: Float
        get() {
            val scaleFactor = when (theme) {
                ThemeDisplayed.LIGHT -> 2f
                ThemeDisplayed.DARK -> 1.5f
            }

            return (maxSize / minSize).toFloat() * scaleFactor
        }

    val scaleFrom = 1f
    val alphaFrom = 0.7f
    val alphaTo = 0f

    /** Params for children views - [RippleView]. */
    val childParams: RelativeLayout.LayoutParams
        get() {
            val params = RelativeLayout.LayoutParams(minSize.toInt(), minSize.toInt())
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            params.topMargin = hookView.top + ((hookView.height - minSize) / 2).toInt()
            return params
        }

    val logoDelay = 0L
    val logoPulse = floatArrayOf(1f, 0.95f, 1f)
}