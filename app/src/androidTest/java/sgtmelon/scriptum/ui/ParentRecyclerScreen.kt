package sgtmelon.scriptum.ui

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.basic.BasicValue
import sgtmelon.scriptum.waitAfter

/**
 * Родительский класс для экранов содержащих [RecyclerView]
 *
 * @author SerjantArbuz
 */
abstract class ParentRecyclerScreen(protected val recyclerId: Int) : ParentUi() {

    private val value: BasicValue = BasicValue()

    val count: Int get() = value.getCount(recyclerId)

    protected val positionRandom: Int get() = (0 until count).random()

    fun onClickItem(position: Int = positionRandom) = action { onClick(recyclerId, position) }

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