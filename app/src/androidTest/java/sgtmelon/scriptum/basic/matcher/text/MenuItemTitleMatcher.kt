package sgtmelon.scriptum.basic.matcher.text

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check toolbar menuItem title.
 */
class MenuItemTitleMatcher(
        @IdRes private val itemId: Int,
        @StringRes private val stringId: Int
) : TypeSafeMatcher<View>() {

    private var title: String? = null
    private var actualTitle: String? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item !is Toolbar) return false

        val context = item.context ?: return false

        title = context.getString(stringId)
        actualTitle = item.menu.findItem(itemId).title.toString()

        return title.equals(actualTitle)
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with itemId = $itemId | titleId = $stringId")

        description?.appendText("\nExpected: title = $title | Actual: title = $actualTitle")
    }

}