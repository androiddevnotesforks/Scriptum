package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.PorterDuff
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
        @ColorRes private val colorId: Int,
        @AttrRes private val attrColor: Int
): TypeSafeMatcher<View>() {

    init {
        if (colorId == -1 && attrColor == -1) throw IllegalAccessException()
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false
        val background = item.background as? ColorDrawable ?: return false

        val color = when {
            colorId != -1 -> context.getCompatColor(colorId)
            attrColor != -1 -> context.getColorAttr(attrColor)
            else -> throw IllegalAccessException()
        }

        return color == background.color
    }

    override fun describeTo(description: Description?) {
        if (colorId != -1) {
            description?.appendText("\nView with background colorId = $colorId")
        }

        if (attrColor != -1) {
            description?.appendText("\nView with background attrColor = $attrColor")
        }
    }

}