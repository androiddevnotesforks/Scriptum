package sgtmelon.test.cappuccino.matchers.layout

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.extensions.getDimen
import sgtmelon.extensions.getDimenAttr

/**
 * Matcher for check view size.
 *
 * If [widthId]/[widthAttr] or [heightId]/[heightAttr] is null => skip size check.
 */
class SizeMatcher(
    @DimenRes private val widthId: Int?,
    @DimenRes private val heightId: Int?,
    @AttrRes private val widthAttr: Int?,
    @AttrRes private val heightAttr: Int?
) : TypeSafeMatcher<View>() {

    private var width: Int? = null
    private var height: Int? = null
    private var actualWidth: Int? = null
    private var actualHeight: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false

        actualHeight = item.height
        actualWidth = item.width

        width = when {
            widthId != null -> context.getDimen(widthId)
            widthAttr != null -> context.getDimen(context.getDimenAttr(widthAttr))
            else -> null
        }

        height = when {
            heightId != null -> context.getDimen(heightId)
            heightAttr != null -> context.getDimen(context.getDimenAttr(heightAttr))
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
        description?.appendText("\nView: widthId = $widthId | heightId = $heightId")
        description?.appendText(" | widthAttr = $widthAttr | heightAttr = $heightAttr")

        description?.appendText("\nExpected: width = $width, height = $height")
        description?.appendText(" | Actual: width = $actualWidth, height = $actualHeight")
    }

}