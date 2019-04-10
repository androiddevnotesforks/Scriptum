package sgtmelon.scriptum.ui

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.TestUtils.random
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.basic.BasicValue

/**
 * Родительский класс для экранов содержащих [RecyclerView]
 *
 * @author SerjnatArbuz
 */
abstract class ParentRecyclerScreen(protected val recyclerId: Int) : ParentUi() {

    private val value: BasicValue = BasicValue()

    val count = value.getCount(recyclerId)

    protected val positionRandom: Int get() = (0 until count - 1).random()

    fun onClickItem(position: Int = positionRandom) = action { onClick(recyclerId, position) }

    fun onScroll(scroll: Scroll, time: Int = 1) = action {
        repeat(time) {
            when (scroll) {
                Scroll.START -> onSwipeDown(recyclerId)
                Scroll.END -> onSwipeUp(recyclerId)
            }
        }
    }

}