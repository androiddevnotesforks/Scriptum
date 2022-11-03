package sgtmelon.scriptum.infrastructure.widgets.recycler

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.utils.DelayJobDelegator

class RecyclerMainFabListener(
    lifecycle: Lifecycle,
    private val callback: Callback?
) : RecyclerView.OnScrollListener() {

    /** Delay for showing FAB after long standstill. */
    private val delayShowJob = DelayJobDelegator(lifecycle)

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        /** Visible only if scroll to top. */
        val isTopScroll = dy <= 0

        callback?.changeFabVisibility(isTopScroll, withGap = true)
        delayShowJob.run(FAB_STANDSTILL_TIME) {
            callback?.changeFabVisibility()
        }
    }

    interface Callback {

        /**
         * When call this func without parameters - it's means just fetch FAB visibility updates.
         */
        fun changeFabVisibility(isVisible: Boolean = true, withGap: Boolean = false)
    }

    companion object {
        const val FAB_STANDSTILL_TIME = 2000L
    }
}