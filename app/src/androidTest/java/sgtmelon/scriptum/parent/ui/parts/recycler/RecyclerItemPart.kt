package sgtmelon.scriptum.parent.ui.parts.recycler

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.parent.ui.model.exception.IllegalConstructorException
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.matchers.RecyclerItemMatcher
import sgtmelon.test.cappuccino.utils.scrollTo

/**
 * Parent class for describe list elements.
 *
 * [itemMatcher] or [position] must be provided.
 */
abstract class RecyclerItemPart<T> protected constructor(
    private val listMatcher: Matcher<View>,
    private val itemMatcher: Matcher<View>?,
    private val position: Int?
) : UiPart() {

    constructor(listMatcher: Matcher<View>, position: Int) :
            this(listMatcher, null, position)

    constructor(listMatcher: Matcher<View>, itemMatcher: Matcher<View>) :
            this(listMatcher, itemMatcher, null)


    private val recyclerItemMatcher = RecyclerItemMatcher(listMatcher)

    val view: Matcher<View> = itemMatcher?.let { recyclerItemMatcher.atItem(it) }
        ?: position?.let { recyclerItemMatcher.atItem(it) }
        ?: throw IllegalConstructorException()

    init {
        if (!PREVENT_SCROLL) scrollToItem() else PREVENT_SCROLL = false
    }

    abstract fun assert(item: T)

    private fun scrollToItem(): RecyclerItemPart<T> = apply {
        itemMatcher?.let { listMatcher.scrollTo(it) }
            ?: position?.let { listMatcher.scrollTo(it) }
            ?: throw IllegalConstructorException()
    }

    fun getChild(childMatcher: Matcher<View>): Matcher<View> {
        return itemMatcher?.let { recyclerItemMatcher.atItemChild(it, childMatcher) }
            ?: position?.let { recyclerItemMatcher.atItemChild(it, childMatcher) }
            ?: throw IllegalConstructorException()
    }

    companion object {
        /** Variable for preventing auto-scroll to item position (during class initialization). */
        var PREVENT_SCROLL = false

        const val SCROLL_TIME = 300L
    }
}