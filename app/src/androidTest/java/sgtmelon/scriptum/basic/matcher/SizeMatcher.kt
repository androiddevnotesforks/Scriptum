package sgtmelon.scriptum.basic.matcher

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getDimenAttr

/**
 * Matcher for check view size.
 *
 * If [widthId]/[widthAttr] or [heightId]/[heightAttr] is -1 => skip size check.
 */
class SizeMatcher(
        @DimenRes private val widthId: Int,
        @DimenRes private val heightId: Int,
        @AttrRes private val widthAttr: Int,
        @AttrRes private val heightAttr: Int
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false
        val resources = context.resources

        val width = when {
            widthId != -1 -> resources.getDimensionPixelOffset(widthId)
            widthAttr != -1 -> resources.getDimensionPixelOffset(context.getDimenAttr(widthAttr))
            else -> null
        }

        val height = when {
            heightId != -1 -> resources.getDimensionPixelOffset(heightId)
            heightAttr != -1 -> resources.getDimensionPixelOffset(context.getDimenAttr(heightAttr))
            else -> null
        }

        return when {
            width != null && height != null -> width == item.width && height == item.height
            width == null && height != null -> height == item.height
            width != null && height == null -> width == item.width
            else -> false
        }
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with widthId = $widthId | heightId = $heightId")
        description?.appendText(" | widthAttr = $widthAttr | heightAttr = $heightAttr")
    }
}