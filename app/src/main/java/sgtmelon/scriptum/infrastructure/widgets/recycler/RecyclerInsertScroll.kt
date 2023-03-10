package sgtmelon.scriptum.infrastructure.widgets.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Class for correct scrolling to inserted item.
 */
interface RecyclerInsertScroll {

    val layoutManager: LinearLayoutManager
    val recyclerView: RecyclerView?

    fun scroll(position: Int) {
        val size = recyclerView?.adapter?.itemCount?.takeIf { it != 0 } ?: return
        val indices = 0 until size

        val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

        /**
         * Если первая или последняя позиция (самый край + небольшой запас [VISIBLE_GAP]), то
         * простой (быстрый) скролл и будет плавная анимация.
         *
         * Если позиция за пределами видимости - плавный скролл.
         */
        when {
            position <= firstVisible -> scrollTop(indices, position, firstVisible)
            position >= lastVisible -> scrollBottom(indices, position, lastVisible)
        }
    }

    private fun scrollTop(indices: IntRange, p: Int, firstVisible: Int) {
        when {
            abs(n = p - firstVisible) < VISIBLE_GAP -> {
                recyclerView?.scrollToPosition(max(a = p - 1, indices.first))
            }
            p < firstVisible -> recyclerView?.smoothScrollToPosition(p)
        }
    }

    private fun scrollBottom(indices: IntRange, p: Int, lastVisible: Int) {
        when {
            abs(n = p - lastVisible) <= VISIBLE_GAP -> {
                recyclerView?.scrollToPosition(min(a = p + 1, indices.last))
            }
            p > lastVisible -> recyclerView?.smoothScrollToPosition(p)
        }
    }

    companion object {
        const val VISIBLE_GAP = 1
    }
}