package sgtmelon.scriptum.basic.matcher

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatColor


/**
 * Matcher for check android:src which pass throw [resourceId].
 *
 * If [resourceId] is -1 => check what don't have drawable.
 * If [attrColor] and [colorId] is -1 => check without colorFilter.
 */
class DrawableMatcher(
        @IdRes resourceId: Int,
        @AttrRes private val attrColor: Int,
        @ColorRes private val colorId: Int
) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == -1) return item.drawable == null

        val context = item.context

        val expectedDrawable = ContextCompat.getDrawable(context, resourceId) ?: return false

        val colorFilter = when {
            attrColor != -1 -> context.getColorAttr(attrColor)
            colorId != -1 -> context.getCompatColor(colorId)
            else -> null
        }

        colorFilter?.let { expectedDrawable.setColorFilter(it, PorterDuff.Mode.SRC_ATOP) }

        return compare(item.drawable, expectedDrawable)
    }

}