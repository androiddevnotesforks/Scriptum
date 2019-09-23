package sgtmelon.scriptum.basic.matcher

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for [RecyclerView] items and items children
 */
class RecyclerItemMatcher(val listMatcher: Matcher<View>) {

    // TODO assert position (write position of recyclerView item when match).

    private var recyclerView: RecyclerView? = null
    private var itemView: View? = null

    fun atItem(itemMatcher: Matcher<View>): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            if (recyclerView == null) {
                description?.appendNotMatching(listMatcher)
                return
            }

            description?.appendMatching(listMatcher)

            if (itemView == null) {
                description?.appendNotMatching(itemMatcher)
                return
            }
        }

        override fun matchesSafely(item: View?): Boolean {
            val itemView = itemView ?: findItemView(itemMatcher, item?.rootView) ?: return false

            return itemView == item
        }
    }

    fun atItemChild(itemMatcher: Matcher<View>,
                    childMatcher: Matcher<View>) = object : TypeSafeMatcher<View>() {
        var childView: View? = null

        override fun describeTo(description: Description?) {
            if (recyclerView == null) {
                description?.appendNotMatching(listMatcher)
                return
            }

            description?.appendMatching(listMatcher)

            if (itemView == null) {
                description?.appendNotMatching(itemMatcher)
                return
            }

            description?.appendMatching(itemMatcher)

            if (childView == null) {
                description?.appendNotMatching(childMatcher)
                return
            }
        }

        override fun matchesSafely(item: View?): Boolean {
            val itemView = itemView ?: findItemView(itemMatcher, item?.rootView) ?: return false

            for (childView in TreeIterables.breadthFirstViewTraversal(itemView)) {
                if (childMatcher.matches(childView)) {
                    this.childView = childView
                    break
                }
            }

            return childView?.let { it == item } ?: false
        }
    }

    private fun findItemView(itemMatcher: Matcher<View>, rootView: View?): View? {
        for (childView in TreeIterables.breadthFirstViewTraversal(rootView)) {
            if (!listMatcher.matches(childView)) continue

            val recyclerView = childView as? RecyclerView ?: continue

            this.recyclerView = recyclerView    // to describe the error

            val holderMatcher: Matcher<RecyclerView.ViewHolder> = getHolderMatcher(itemMatcher)

            return getMatchedItem(recyclerView, holderMatcher)?.let {
                recyclerView.findViewHolderForAdapterPosition(it.position)?.itemView
            }
        }

        return null
    }

    private fun Description.appendNotMatching(matcher: Matcher<View>) = apply {
        appendText("No found view with matcher = [$matcher]")
    }

    private fun Description.appendMatching(matcher: Matcher<View>) = apply {
        appendText("Found view with matcher = [$matcher]")
    }


    //region from RecyclerViewActions

    /**
     * Wrapper for matched items in recycler view which contains position and
     * description of matched view.
     */
    private class MatchedItem(val position: Int, val description: String) {
        override fun toString() = description
    }

    /**
     * Creates matcher for view holder with given item view matcher.
     *
     * [itemMatcher] - matcher which is used to match item.
     *
     * Return a matcher which matches a view holder containing item matching itemMatcher.
     */
    private fun <VH : RecyclerView.ViewHolder> getHolderMatcher(itemMatcher: Matcher<View>) =
            object : TypeSafeMatcher<VH>() {
                override fun matchesSafely(item: VH): Boolean = itemMatcher.matches(item.itemView)

                override fun describeTo(description: Description?) {
                    itemMatcher.describeTo(description?.appendText("holder with view: "))
                }
            }

    /**
     * Finds positions of items in [RecyclerView] which is matching given [holderMatcher].
     * This is similar to positionMatching(RecyclerView, Matcher<VH>), except that it returns list of
     * multiple positions if there are, rather than throwing Ambiguous view error exception.
     */
    private fun <VH : RecyclerView.ViewHolder> getMatchedItem(
            recyclerView: RecyclerView,
            holderMatcher: Matcher<VH>
    ): MatchedItem? {
        var matchedItem: MatchedItem? = null

        val adapter = recyclerView.adapter ?: return matchedItem

        val cacheHolderArray = SparseArray<RecyclerView.ViewHolder>()

        for (p in 0 until adapter.itemCount) {
            val itemType = adapter.getItemViewType(p)
            var cachedHolder = cacheHolderArray.get(itemType)

            // Create a view holder per type if not exists.
            if (cachedHolder == null) {
                cachedHolder = adapter.createViewHolder(recyclerView, itemType)
                cacheHolderArray.put(itemType, cachedHolder)
            }

            // Bind data to ViewHolder and apply matcher to view descendants.
            adapter.bindViewHolder(cachedHolder, p)

            if (holderMatcher.matches(cachedHolder)) {
                val errorHeader = "\n\n*** Matched ViewHolder item at position: $p ***"

                matchedItem = MatchedItem(p, HumanReadables.getViewHierarchyErrorMessage(
                        cachedHolder.itemView, null, errorHeader, null
                ))

                adapter.onViewRecycled(cachedHolder)
                break
            } else {
                adapter.onViewRecycled(cachedHolder)
            }
        }

        return matchedItem
    }

    //endregion

}