package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatColor

/**
 * Matcher for check background color.
 */
class BackgroundColorMatcher(
        @ColorRes private val colorId: Int?,
        @AttrRes private val attrColor: Int?
): TypeSafeMatcher<View>() {

    init {
        if (colorId == null && attrColor == null) throw IllegalAccessException()
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false
        val background = item.background as? ColorDrawable ?: return false

        val color = when {
            colorId != null -> context.getCompatColor(colorId)
            attrColor != null -> context.getColorAttr(attrColor)
            else -> throw IllegalAccessException()
        }

        return color == background.color
    }

    override fun describeTo(description: Description?) {
        if (colorId != null) {
            description?.appendText("\nView with background colorId = $colorId")
        }

        if (attrColor != null) {
            description?.appendText("\nView with background attrColor = $attrColor")
        }
    }

}