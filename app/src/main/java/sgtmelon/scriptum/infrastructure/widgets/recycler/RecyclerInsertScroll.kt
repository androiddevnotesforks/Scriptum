package sgtmelon.scriptum.infrastructure.widgets.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

/**
 * Class for correct scrolling to inserted item.
 */
class RecyclerInsertScroll(
    private val recyclerView: RecyclerView?,
    private val layoutManager: LinearLayoutManager
) {

    //  val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
    //  val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
    //
    //  Log.i("HERE", "p=$p | firstVisible=$firstVisiblePosition | lastVisible=$lastVisiblePosition")
    //
    //  /**
    //   *  FirstVisiblePosition can be equal [p] if:
    //   *  - Click on first item remove;
    //   *  - Click on snackbar undo.
    //   *
    //   *  Then [p] = 0 and firstVisiblePosition = 0.
    //   */
    //  if (p <= firstVisiblePosition || p > lastVisiblePosition) {
    //      if (p == 0 && firstVisiblePosition == 0) {
    //          recyclerView?.scrollToPosition(p)
    //      } else if (p == list.lastIndex && lastVisiblePosition == list.lastIndex - 1) {
    //          recyclerView?.scrollToPosition(p)
    //      } else {
    //          recyclerView?.smoothScrollToPosition(p)
    //      }
    //  }

    fun scroll(indices: IntRange, p: Int) {
        val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

        if (p <= /*min(a = */firstVisible /*+ VISIBLE_GAP, indices.last)*/) {
            scrollTop(indices, p, firstVisible)
        }

        if (p >= /*min(a = */lastVisible/* - VISIBLE_GAP, indices.first)*/) {
            scrollBottom(indices, p, lastVisible)
        }
        //
        //        /**
        //         * Если первая или последняя позиция (самый край) - простой скролл и будет плавная анимация
        //         *
        //         * Если позиция за пределами видимости - плавный скролл
        //         *
        //         * К кейсу выше надо добавить кейс, чтобы позиция была в пределах определённого промежутка
        //         * не дальше 5 item'ов например. И тогда быстро скролить до края gap'а и потом уже
        //         * плавный скролл (на подобии как сделано в telegram).
        //         *
        //         * Если позиция соответствует первой или последней видимой - надо доскролить плавно
        //         * вверх и вниз соответственно, чтобы не было дёрганной анимации при insert'е.
        //         */
        //
        //        when {
        //            p == 0 && firstVisible == p -> recyclerView?.scrollToPosition(p)
        //            p == firstVisible  -> recyclerView?.scrollToPosition(p - 1)
        //            p == list.lastIndex && lastVisible == p - 1 -> recyclerView?.scrollToPosition(p)
        //            p == lastVisible -> recyclerView?.scrollToPosition(p + 1)
        //            p < firstVisible || p > lastVisible -> recyclerView?.smoothScrollToPosition(p)
        //        }
    }

    private fun scrollTop(indices: IntRange, p: Int, firstVisible: Int) {
        when {
            p == 0 && firstVisible == 0 -> recyclerView?.scrollToPosition(p)
            p == firstVisible -> recyclerView?.scrollToPosition(max(a = p - 1, indices.first))
            p < firstVisible -> recyclerView?.smoothScrollToPosition(p)
            //            abs(n = firstVisible - p) <= VISIBLE_GAP -> TODO()
        }
    }

    private fun scrollBottom(indices: IntRange, p: Int, lastVisible: Int) {
        when {
            p == indices.last && lastVisible == p - 1 -> recyclerView?.scrollToPosition(p)
            p == lastVisible -> recyclerView?.scrollToPosition(min(a = p + 1, indices.last))
            p > lastVisible -> recyclerView?.smoothScrollToPosition(p)
        }
    }

    companion object {
        const val VISIBLE_GAP = 1
        const val SCROLL_GAP = 5
    }
}