package sgtmelon.scriptum.parent.ui.parts.recycler

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.getCount
import sgtmelon.test.cappuccino.utils.getRandomPosition
import sgtmelon.test.cappuccino.utils.swipeDown
import sgtmelon.test.cappuccino.utils.swipeUp

interface RecyclerPart {

    val recyclerView: Matcher<View>

    val count: Int get() = recyclerView.getCount()
    val last: Int get() = count - 1

    val random: Int? get() = recyclerView.getRandomPosition()

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
        private const val SCROLL_TIME = 200L
        private const val REPEAT_COUNT = 2
    }
}