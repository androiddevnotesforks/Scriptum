package sgtmelon.scriptum.parent.ui.parts.recycler

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.getCount
import sgtmelon.test.cappuccino.utils.getRandomPosition
import sgtmelon.test.cappuccino.utils.swipeDown
import sgtmelon.test.cappuccino.utils.swipeUp

interface RecyclerPart<T, V : RecyclerItemPart<T>> {

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

    fun getItem(p: Int): V

    fun assertItem(item: T, p: Int? = random) {
        if (p == null) throw EmptyListException()

        getItem(p).assert(item)
    }

    /**
     * [withWait] parameter gives small period for checking UI (by eyes :D).
     */
    fun assertList(list: List<T>, withWait: Boolean = false) {
        for ((p, item) in list.withIndex()) {
            assertItem(item, p)

            if (withWait) {
                await(time = 250)
            }
        }
    }

    companion object {
        private const val SCROLL_TIME = 200L
        private const val REPEAT_COUNT = 2
    }
}