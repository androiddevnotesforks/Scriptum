package sgtmelon.scriptum.basic.matcher

import android.view.View
import androidx.annotation.DimenRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check view size
 *
 * If [widthId] or [heightId] is -1 => skip size check.
 */
class SizeMatcher(
        @DimenRes private val widthId: Int,
        @DimenRes private val heightId: Int
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false

        val width = if (widthId != -1) context.resources.getDimensionPixelOffset(widthId) else null
        val height = if (heightId != -1) context.resources.getDimensionPixelOffset(heightId) else null

        if (width == null && height != null) return height == item.height
        if (width != null && height == null) return width == item.width

        return width == item.width && height == item.height
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with widthId = $widthId and heightId = $heightId")
    }
}