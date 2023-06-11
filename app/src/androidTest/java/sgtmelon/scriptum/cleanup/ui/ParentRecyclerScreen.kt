package sgtmelon.scriptum.cleanup.ui

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.getCount
import sgtmelon.test.cappuccino.utils.getRandomPosition
import sgtmelon.test.cappuccino.utils.swipeDown
import sgtmelon.test.cappuccino.utils.swipeUp

/**
 * Parent class for screens which contains [RecyclerView].
 */
@Deprecated("Use RecyclerPart")
abstract class ParentRecyclerScreen(@IdRes private val recyclerId: Int) : ParentScreen() {

    protected val recyclerView = getViewById(recyclerId)

    val count: Int get() = recyclerView.getCount()
    val last: Int get() = count - 1

    protected val random: Int? get() = recyclerView.getRandomPosition()

    fun scrollTo(scroll: Scroll, time: Int = REPEAT_COUNT) = repeat(time) {
        when (scroll) {
            Scroll.START -> recyclerView.swipeDown()
            Scroll.END -> recyclerView.swipeUp()
        }

        await(SCROLL_TIME)
    }

    fun scrollThrough() = repeat(REPEAT_COUNT) {
        scrollTo(Scroll.END)
        scrollTo(Scroll.START)
    }

    companion object {
        const val SCROLL_TIME = 200L
        const val SNACK_BAR_TIME = 3000L

        const val REPEAT_COUNT = 2
    }
}