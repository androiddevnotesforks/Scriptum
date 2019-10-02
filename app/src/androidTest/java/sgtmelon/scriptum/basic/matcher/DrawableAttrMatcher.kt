package sgtmelon.scriptum.basic.matcher

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getColorAttr

/**
 * Matcher for check android:src which pass throw [resourceId].
 *
 * If [resourceId] is -1 => check what don't have drawable.
 * If [attrColor] is -1 => check without colorFilter.
 */
class DrawableAttrMatcher(@IdRes resourceId: Int, @AttrRes private val attrColor: Int) :
        ParentImageMatcher(resourceId) {

    override fun describeTo(description: Description?) {
        super.describeTo(description)
        description?.appendText("\nWith attrColor: [$attrColor]")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == -1) return item.drawable == null

        val context = item.context

        val expected = ContextCompat.getDrawable(context, resourceId) ?: return false

        if (attrColor != -1) {
            expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)
        }

        return compare(item.drawable, expected)
    }

}