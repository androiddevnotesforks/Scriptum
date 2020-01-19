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

    var width: Int? = null
    var height: Int? = null
    var actualWidth: Int = -1
    var actualHeight: Int = -1

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false
        val resources = context.resources

        actualHeight = item.height
        actualWidth = item.width

        width = when {
            widthId != -1 -> resources.getDimensionPixelSize(widthId)
            widthAttr != -1 -> resources.getDimensionPixelSize(context.getDimenAttr(widthAttr))
            else -> null
        }

        height = when {
            heightId != -1 -> resources.getDimensionPixelSize(heightId)
            heightAttr != -1 -> resources.getDimensionPixelSize(context.getDimenAttr(heightAttr))
            else -> null
        }

        return when {
            width != null && height != null -> width == actualWidth && height == actualHeight
            width == null && height != null -> height == actualHeight
            width != null && height == null -> width == actualWidth
            else -> false
        }
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with widthId = $widthId | heightId = $heightId")
        description?.appendText(" | widthAttr = $widthAttr | heightAttr = $heightAttr")

        description?.appendText("\nExpected: width = $width, height = $height")
        description?.appendText(" | Actual: width = $actualWidth, height = $actualHeight")
    }

}