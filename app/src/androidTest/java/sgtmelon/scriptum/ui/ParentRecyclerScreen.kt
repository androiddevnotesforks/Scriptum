package sgtmelon.scriptum.ui

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.basic.*
import sgtmelon.scriptum.waitAfter

/**
 * Parent class for screens which contains [RecyclerView]
 */
abstract class ParentRecyclerScreen(@IdRes protected val recyclerId: Int) : ParentUi() {

    protected val recyclerView = getViewById(recyclerId)

    val count: Int get() = recyclerView.getCount()

    protected val positionRandom: Int get() = recyclerView.getRandomPosition()

    protected fun onClickItem(position: Int = positionRandom) {
        recyclerView.click(position)
    }

    fun onScroll(scroll: Scroll, time: Int = 2) = repeat(time) {
        waitAfter(time = 200) {
            when (scroll) {
                Scroll.START -> recyclerView.swipeDown()
                Scroll.END -> recyclerView.swipeUp()
            }
        }
    }

    fun onScrollThrough() = repeat(times = 2) {
        onScroll(Scroll.END)
        onScroll(Scroll.START)
    }

}