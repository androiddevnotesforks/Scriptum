package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getColorAttr

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
        val expected = context.getDrawable(resourceId)?.mutate() ?: return false

        if (attrColor != null) {
            expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)
        }

        return compare(navigationIcon, expected)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        if (attrColor != null) {
            description?.appendText("\nView with attrColor = $attrColor")
        }
    }

}