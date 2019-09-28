package sgtmelon.scriptum.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.extension.scrollTo
import sgtmelon.scriptum.basic.matcher.RecyclerItemMatcher

/**
 * Parent class for children of [ParentRecyclerScreen] and for describes [RecyclerView] items.
 *
 * [itemMatcher] or [position] must be provided.
 */
abstract class ParentRecyclerItem private constructor(
        private val listMatcher: Matcher<View>,
        private val itemMatcher: Matcher<View>?,
        private val position: Int?
) {

    constructor(listMatcher: Matcher<View>, position: Int) :
            this(listMatcher, null, position)

    constructor(listMatcher: Matcher<View>, itemMatcher: Matcher<View>) :
            this(listMatcher, itemMatcher, null)


    private val recyclerItemMatcher = RecyclerItemMatcher(listMatcher)

    val view: Matcher<View> = itemMatcher?.let { recyclerItemMatcher.atItem(it) }
            ?: position?.let { recyclerItemMatcher.atItem(it) }
            ?: throw NullPointerException(PROVIDE_ERROR_TEXT)

    init {
        scrollToItem()
    }

    private fun scrollToItem(): ParentRecyclerItem = apply {
        itemMatcher?.let { listMatcher.scrollTo(it) }
                ?: position?.let { listMatcher.scrollTo(it) }
                ?: throw NullPointerException(PROVIDE_ERROR_TEXT)
    }

    fun getChild(childMatcher: Matcher<View>): Matcher<View> {
        return itemMatcher?.let { recyclerItemMatcher.atItemChild(it, childMatcher) }
                ?: position?.let { recyclerItemMatcher.atItemChild(it, childMatcher) }
                ?: throw NullPointerException(PROVIDE_ERROR_TEXT)
    }

    companion object {
        private const val PROVIDE_ERROR_TEXT = "You didn't provide itemMatcher OR position."
    }

}