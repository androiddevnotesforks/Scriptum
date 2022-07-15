package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import org.hamcrest.Description
import sgtmelon.scriptum.cleanup.extension.getColorAttr

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
        val expected = context.getDrawable(resourceId)?.mutate() ?: return false

        expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)

        return compare(expected, itemIcon)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        description?.appendText("\nView with itemId = $itemId")
        description?.appendText("\nView with attrColor = $attrColor")
    }

}