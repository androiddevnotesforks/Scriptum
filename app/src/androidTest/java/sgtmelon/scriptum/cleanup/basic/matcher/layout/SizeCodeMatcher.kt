package sgtmelon.scriptum.cleanup.basic.matcher.layout

import android.view.View
import android.view.ViewGroup.LayoutParams
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check with size with default [LayoutParams.WRAP_CONTENT]/[LayoutParams.MATCH_PARENT].
 *
 * if [width]/[height] is null -> skip size check.
 */
class SizeCodeMatcher(
        private val width: Int?,
        private val height: Int?
) : TypeSafeMatcher<View>() {

    private var actualWidth: Int? = null
    private var actualHeight: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        with(item.layoutParams) {
            actualWidth = width
            actualHeight = height
        }

        return when {
            width != null && height != null -> width == actualWidth && height == actualHeight
            width == null && height != null -> height == actualHeight
            width != null && height == null -> width == actualWidth
            else -> false
        }
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nExpected: width = $width, height = $height")
        description?.appendText(" | Actual: width = $actualWidth, height = $actualHeight")
    }
}