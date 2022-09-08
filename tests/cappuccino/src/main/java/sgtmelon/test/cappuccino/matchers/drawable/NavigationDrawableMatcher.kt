package sgtmelon.test.cappuccino.matchers.drawable

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat

/**
 * Matcher for check toolbar navigation icon.
 *
 * If [resourceId] is null => check what don't have drawable.
 * If [attrColor] is null => check without colorFilter.
 */
class NavigationDrawableMatcher(
    @IdRes resourceId: Int?,
    @AttrRes private val attrColor: Int?
) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is Toolbar) return false

        val navigationIcon = item.navigationIcon

        if (resourceId == null) return navigationIcon == null

        if (navigationIcon == null) return false

        val context = item.context ?: return false
        val expected = context.getDrawableCompat(resourceId)?.mutate() ?: return false

        if (attrColor != null) {
            expected.setColorFilterCompat(context.getColorAttr(attrColor))
        }

        return compare(expected, navigationIcon)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        if (attrColor != null) {
            description?.appendText("\nView with attrColor = $attrColor")
        }
    }

}