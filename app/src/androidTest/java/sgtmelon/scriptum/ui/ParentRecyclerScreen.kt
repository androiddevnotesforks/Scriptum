package sgtmelon.scriptum.ui

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.basic.BasicValue
import sgtmelon.scriptum.waitAfter

/**
 * Parent class for screens which contains [RecyclerView]
 */
abstract class ParentRecyclerScreen(@IdRes protected val recyclerId: Int) : ParentUi() {

    val count: Int get() = BasicValue().getCount(recyclerId)

    protected val positionRandom: Int get() = (0 until count).random()

    protected fun onClickItem(position: Int = positionRandom) = action {
        onClick(recyclerId, position)
    }

    fun onScroll(scroll: Scroll, time: Int = 2) = action {
        repeat(time) {
            waitAfter(time = 200) {
                when (scroll) {
                    Scroll.START -> onSwipeDown(recyclerId)
                    Scroll.END -> onSwipeUp(recyclerId)
                }
            }
        }
    }

    fun onScrollThrough() = repeat(times = 2) {
        onScroll(Scroll.END)
        onScroll(Scroll.START)
    }

}