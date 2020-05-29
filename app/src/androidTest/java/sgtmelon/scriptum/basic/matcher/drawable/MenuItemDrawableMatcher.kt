package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatDrawable

/**
 * Matcher for check toolbar menuItem icon.
 */
class MenuItemDrawableMatcher(
        @IdRes private val itemId: Int,
        @IdRes resourceId: Int,
        @AttrRes private val attrColor: Int
) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is Toolbar) return false

        val itemIcon = item.menu.findItem(itemId).icon

        val context = item.context ?: return false
        val expected = context.getCompatDrawable(resourceId) ?: return false

        expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)

        return compare(itemIcon, expected)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        description?.appendText("\nView with itemId = $itemId")
        description?.appendText("\nView with attrColor = $attrColor")
    }

}