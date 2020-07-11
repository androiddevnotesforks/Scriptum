package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import org.hamcrest.Description
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatColor
import sgtmelon.scriptum.extension.getCompatDrawable

/**
 * Matcher for check android:src which gets with [resourceId].
 *
 * If [resourceId] is null => check what don't have drawable.
 * If [colorId]/[attrColor] is null => check without colorFilter.
 */
class DrawableMatcher(
        @IdRes resourceId: Int?,
        @ColorRes private val colorId: Int?,
        @AttrRes private val attrColor: Int?
) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == null) return item.drawable == null

        val context = item.context ?: return false
        val expected = context.getCompatDrawable(resourceId) ?: return false

        when {
            colorId != null -> {
                expected.setColorFilter(context.getCompatColor(colorId), PorterDuff.Mode.SRC_ATOP)
            }
            attrColor != null -> {
                expected.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)
            }
        }

        return compare(item.drawable, expected)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        if (colorId != null) {
            description?.appendText("\nView with colorId = $colorId")
        }

        if (attrColor != null) {
            description?.appendText("\nView with attrColor = $attrColor")
        }
    }

}