package sgtmelon.scriptum.source.ui.basic

import android.view.View
import androidx.cardview.widget.CardView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.extensions.getColorAttr
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getNoteCardColor
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Matcher for check app:cardBackground which gets with [theme] and [color].
 */
class CardBackgroundColorMatcher(
    private val theme: ThemeDisplayed,
    private val color: Color
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is CardView) return false

        val context = item.context ?: return false
        val backgroundColor = if (theme == ThemeDisplayed.LIGHT) {
            context.getNoteCardColor(color)
        } else {
            context.getColorAttr(R.attr.clBackgroundView)
        }

        return backgroundColor == item.cardBackgroundColor.defaultColor
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with theme = $theme | color = $color")
    }
}