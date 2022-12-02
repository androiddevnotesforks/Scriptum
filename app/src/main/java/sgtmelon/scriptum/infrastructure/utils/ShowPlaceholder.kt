package sgtmelon.scriptum.infrastructure.utils

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible

/**
 * Class for help control showing placeholders while transition happen (and hiding at the end).
 */
class ShowPlaceholder(
    lifecycle: Lifecycle,
    private val context: Context,
    private val delayTimeId: Int = R.integer.placeholder_fade_time
) {

    private val delayTime get() = context.resources.getInteger(delayTimeId).toLong()
    private val hideJob = DelayedJob(lifecycle)

    fun start(vararg views: View?) {
        changeVisibility(isVisible = true, views)
        hideJob.start(delayTime) { changeVisibility(isVisible = false, views) }
    }

    private fun changeVisibility(isVisible: Boolean, viewArray: Array<out View?>) {
        for (view in viewArray) {
            if (isVisible) view?.makeVisible() else view?.makeInvisible()
        }
    }
}