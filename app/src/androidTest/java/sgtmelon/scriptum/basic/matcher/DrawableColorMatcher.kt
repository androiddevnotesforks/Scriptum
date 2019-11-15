package sgtmelon.scriptum.basic.matcher

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getCompatColor


/**
 * Matcher for check android:src which pass throw [resourceId].
 *
 * If [resourceId] is -1 => check what don't have drawable.
 * If [colorId] is -1 => check without colorFilter.
 */
class DrawableColorMatcher(@IdRes resourceId: Int, @ColorRes private val colorId: Int) :
        ParentImageMatcher(resourceId) {

    override fun describeTo(description: Description?) {
        super.describeTo(description)
        description?.appendText("\nWith colorId: [$colorId]")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == -1) return item.drawable == null

        val context = item.context ?: return false

        val expected = ContextCompat.getDrawable(context, resourceId) ?: return false

        if (colorId != -1) {
            expected.setColorFilter(context.getCompatColor(colorId), PorterDuff.Mode.SRC_ATOP)
        }

        return compare(item.drawable, expected)
    }

}