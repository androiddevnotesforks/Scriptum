package sgtmelon.scriptum.infrastructure.widgets.recycler

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Workaround because [View.OVER_SCROLL_IF_CONTENT_SCROLLS] not working properly.
 *
 * [showHeader]/[showFooter] - for customization, if need show only specific scroll edge.
 */
class RecyclerOverScrollListener(
    private val showHeader: Boolean = true,
    private val showFooter: Boolean = true
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return

        val isFirstVisible = lm.findFirstCompletelyVisibleItemPosition() == 0
        val isLastVisible = lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1
        val allVisible = isFirstVisible && isLastVisible

        recyclerView.overScrollMode = if (allVisible) {
            View.OVER_SCROLL_NEVER
        } else {
            val showHeaderEdge = showHeader && isFirstVisible && !isLastVisible
            val showFooterEdge = showFooter && !isFirstVisible && isLastVisible

            if (showHeader && showFooter || showHeaderEdge || showFooterEdge) {
                View.OVER_SCROLL_ALWAYS
            } else {
                View.OVER_SCROLL_NEVER
            }
        }
    }
}