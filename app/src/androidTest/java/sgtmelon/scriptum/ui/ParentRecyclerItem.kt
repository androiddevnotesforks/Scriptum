package sgtmelon.scriptum.ui

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.matcher.RecyclerItemMatcher
import sgtmelon.scriptum.basic.scrollTo

open class ParentRecyclerItem(
        private val listMatcher: Matcher<View>,
        private val itemMatcher: Matcher<View>
) {

    private val recyclerItemMatcher = RecyclerItemMatcher(listMatcher)

    init {
        scrollToItem()
    }

    private fun scrollToItem(): ParentRecyclerItem = apply { listMatcher.scrollTo(itemMatcher) }

    fun getItem(): Matcher<View> = recyclerItemMatcher.atItem(itemMatcher)

    fun getChild(childMatcher: Matcher<View>): Matcher<View> {
        return recyclerItemMatcher.atItemChild(itemMatcher, childMatcher)
    }

}