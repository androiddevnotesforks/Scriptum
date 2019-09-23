package sgtmelon.scriptum.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.extension.scrollTo
import sgtmelon.scriptum.basic.matcher.RecyclerItemMatcher

/**
 * Parent class for children of [ParentRecyclerScreen] and for describes [RecyclerView] items
 */
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