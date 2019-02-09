package sgtmelon.scriptum.ui

import sgtmelon.scriptum.TestUtils
import sgtmelon.scriptum.basic.BasicValue

abstract class ParentRecyclerScreen(private val recyclerId: Int) : ParentUi() {

    private val value: BasicValue = BasicValue()

    val count = value.getCount(recyclerId)

    fun onClickItem(position: Int = TestUtils.random(0 until count - 1)) =
            action { onClick(recyclerId, position) }

    fun onScroll(scroll: SCROLL, time: Int = 1) = action {
        repeat(time) {
            when (scroll) {
                SCROLL.START -> onSwipeDown(recyclerId)
                SCROLL.END -> onSwipeUp(recyclerId)
            }
        }
    }

}