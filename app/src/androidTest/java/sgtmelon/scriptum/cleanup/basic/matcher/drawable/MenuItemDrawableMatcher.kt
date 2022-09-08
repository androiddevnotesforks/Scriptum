package sgtmelon.scriptum.cleanup.basic.matcher.drawable

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat

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

        if (resourceId == null) return itemIcon == null

        val context = item.context ?: return false
        val expected = context.getDrawableCompat(resourceId)?.mutate() ?: return false

        expected.setColorFilterCompat(context.getColorAttr(attrColor))

        return compare(expected, itemIcon)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        description?.appendText("\nView with itemId = $itemId")
        description?.appendText("\nView with attrColor = $attrColor")
    }

}