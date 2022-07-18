package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Matcher for check background color.
 */
class BackgroundAppColorMatcher(
    private val theme: ThemeDisplayed,
    @Color private val color: Int,
    private val needDark: Boolean
) : TypeSafeMatcher<View>() {

    @ColorInt private var expectedColor: Int? = null
    @ColorInt private var actualColor: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        expectedColor = item.context?.getNoteToolbarColor(theme, color, needDark) ?: return false
        actualColor = (item.background as? ColorDrawable)?.color ?: return false

        return expectedColor == actualColor
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nData: theme = $theme | color = $color | needDark = $needDark")
        description?.appendText("\nExpected: color = $expectedColor")
        description?.appendText(" | Actual: color = $actualColor")
    }
}