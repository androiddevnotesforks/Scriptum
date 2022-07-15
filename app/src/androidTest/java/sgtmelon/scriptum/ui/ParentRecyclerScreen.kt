package sgtmelon.scriptum.ui

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.cleanup.data.Scroll

/**
 * Parent class for screens which contains [RecyclerView].
 */
abstract class ParentRecyclerScreen(@IdRes private val recyclerId: Int) : ParentUi() {

    protected val recyclerView = getViewById(recyclerId)

    val count: Int get() = recyclerView.getCount()
    val last: Int get() = count - 1

    protected val random: Int? get() = recyclerView.getRandomPosition()

    fun onScroll(scroll: Scroll, time: Int = REPEAT_COUNT) = repeat(time) {
        waitAfter(SCROLL_TIME) {
            when (scroll) {
                Scroll.START -> recyclerView.swipeDown()
                Scroll.END -> recyclerView.swipeUp()
            }
        }
    }

    fun onScrollThrough() = repeat(REPEAT_COUNT) {
        onScroll(Scroll.END)
        onScroll(Scroll.START)
    }

    companion object {
        const val SCROLL_TIME = 200L
        const val SNACK_BAR_TIME = 3000L

        const val REPEAT_COUNT = 2
    }
}