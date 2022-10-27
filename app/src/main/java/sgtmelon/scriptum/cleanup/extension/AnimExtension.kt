package sgtmelon.scriptum.cleanup.extension

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.addListener
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.getAlphaInterpolator
import sgtmelon.test.idling.addIdlingListener

@Deprecated("Check how will work simple animation (note object animator), may be it's more light")
inline fun View.animateAlpha(
    isVisible: Boolean,
    duration: Long = context.resources.getInteger(R.integer.list_fade_time).toLong(),
    crossinline onEnd: () -> Unit = {}
) {
    val interpolator = getAlphaInterpolator(isVisible)
    val valueTo = if (isVisible) 1f else 0f

    ObjectAnimator.ofFloat(this, View.ALPHA, valueTo).apply {
        this.interpolator = interpolator
        this.duration = duration

        addIdlingListener()
        addListener(onEnd = { onEnd() })
    }.start()
}