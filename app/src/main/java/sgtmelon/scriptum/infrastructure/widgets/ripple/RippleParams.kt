package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.view.View
import android.widget.RelativeLayout
import kotlin.math.max
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Model with params for [RippleContainer]
 */
class RippleParams(
    private val theme: ThemeDisplayed,
    private val hookView: View,
    private val parentView: View
) {

    private val minSize get() = hookView.width / 1.3
    private val maxSize get() = max(parentView.width, parentView.height)

    val count get() = maxSize / (minSize / 2).toInt()
    val duration get() = 1000L * count / 2
    val delay get() = duration / count

    private val scaleFactor get() = if (theme == ThemeDisplayed.LIGHT) 2f else 1.5f

    val scaleTo get() = (maxSize / minSize).toFloat() * scaleFactor

    val childParams get() = RelativeLayout.LayoutParams(minSize.toInt(), minSize.toInt()).apply {
        addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        topMargin = hookView.top + ((hookView.height - minSize) / 2).toInt()
    }

}