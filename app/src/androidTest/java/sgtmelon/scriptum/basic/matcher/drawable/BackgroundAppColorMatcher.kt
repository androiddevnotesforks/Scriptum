package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.drawable.ColorDrawable
import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getAppThemeColor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Matcher for check background color.
 */
class BackgroundAppColorMatcher(
        @Theme private val theme: Int,
        @Color private val color: Int,
        private val needDark: Boolean
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        val context = item.context ?: return false
        val background = item.background as? ColorDrawable ?: return false

        val color = context.getAppThemeColor(theme, color, needDark)

        return color == background.color
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with theme = $theme | color = $color | needDark = $needDark")
    }

}