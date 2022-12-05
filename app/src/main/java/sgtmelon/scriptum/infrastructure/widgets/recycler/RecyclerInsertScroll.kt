package sgtmelon.scriptum.infrastructure.widgets.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Class for correct scrolling to inserted item.
 */
class RecyclerInsertScroll(
    private val recyclerView: RecyclerView?,
    private val layoutManager: LinearLayoutManager
) {

    fun scroll(list: List<*>, p: Int) {
        val indices = list.indices
        val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

        /**
         * Если первая или последняя позиция (самый край + небольшой запас [VISIBLE_GAP]), то
         * простой (быстрый) скролл и будет плавная анимация.
         *
         * Если позиция за пределами видимости - плавный скролл.
         */
        when {
            p <= firstVisible -> scrollTop(indices, p, firstVisible)
            p >= lastVisible -> scrollBottom(indices, p, lastVisible)
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