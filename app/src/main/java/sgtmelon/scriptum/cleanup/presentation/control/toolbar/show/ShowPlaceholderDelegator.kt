package sgtmelon.scriptum.cleanup.presentation.control.toolbar.show

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.Lifecycle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.DelayedJob
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible

/**
 * Class for help control showing placeholders while transition happen (and hiding at the end).
 */
class ShowPlaceholderDelegator private constructor(
    lifecycle: Lifecycle,
    resources: Resources,
    private val viewList: List<View?>
) {

    constructor(lifecycle: Lifecycle, resources: Resources, vararg views: View?)
            : this(lifecycle, resources, listOf(*views))

    private val delayTime = resources.getInteger(R.integer.placeholder_fade_time).toLong()
    private val hideJob = DelayedJob(lifecycle)

    fun start() {
        changeVisibility(isVisible = true)
        hideJob.start(delayTime) { changeVisibility(isVisible = false) }
    }

    private fun changeVisibility(isVisible: Boolean) {
        for (view in viewList) {
            if (isVisible) view?.makeVisible() else view?.makeInvisible()
        }
    }
}