package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatColor

/**
 * Matcher for check android:src which gets with [resourceId].
 *
 * If [resourceId] is -1 => check what don't have drawable.
 * If [colorId]/[attrColor] is -1 => check without colorFilter.
 */
class DrawableMatcher(
        @IdRes resourceId: Int,
        @ColorRes private val colorId: Int,
        @AttrRes private val attrColor: Int
) : ParentImageMatcher(resourceId) {

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        if (colorId != -1) {
            description?.appendText("\nView with colorId = $colorId")
        }

        if (attrColor != -1) {
            description?.appendText("\nView with attrColor = $attrColor")
        }
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == -1) return item.drawable == null

        val context = item.context ?: return false
        val expected = ContextCompat.getDrawable(context, resourceId) ?: return false

        when {
            colorId != -1 -> {
                expected.setColorFilter(context.getCompatColor(colorId), PorterDuff.Mode.SRC_ATOP)
            }
            attrColor != -1 -> {
                expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)
            }
        }

        return compare(item.drawable, expected)
    }

}