package sgtmelon.scriptum.cleanup.extension

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.test.idling.getWaitIdling

fun ViewGroup.createVisibleAnim(
    target: View?,
    isVisible: Boolean,
    @IntegerRes durationId: Int = R.integer.list_fade_time
) = let {
    val visibility = if (isVisible) View.VISIBLE else View.GONE

    if (target == null || target.visibility == visibility) return@let

    val time = context.resources.getInteger(durationId).toLong()
    val transition = Fade().setDuration(time).addTarget(target)

    TransitionManager.beginDelayedTransition(it, transition)

    getWaitIdling().start(time)
    target.visibility = visibility
}