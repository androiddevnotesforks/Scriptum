package sgtmelon.scriptum.ui.testing.parent.screen

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.ui.testing.model.key.Scroll
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

    fun onScroll(scroll: Scroll, time: Int = REPEAT_COUNT) = repeat(time) {
        when (scroll) {
            Scroll.START -> recyclerView.swipeDown()
            Scroll.END -> recyclerView.swipeUp()
        }

        await(SCROLL_TIME)
    }

    fun onScrollThrough() = repeat(REPEAT_COUNT) {
        onScroll(Scroll.END)
        onScroll(Scroll.START)
    }

    companion object {
        private const val SCROLL_TIME = 200L
        private const val REPEAT_COUNT = 2
    }
}