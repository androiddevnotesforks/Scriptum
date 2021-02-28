package sgtmelon.scriptum.basic.matcher.card

import android.view.View
import androidx.cardview.widget.CardView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getNoteCardColor

/**
 * Matcher for check app:cardBackground which gets with [theme] and [color].
 */
class CardBackgroundColorMatcher(
    @Theme private val theme: Int,
    @Color private val color: Int
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is CardView) return false

        val context = item.context ?: return false
        val backgroundColor = if (theme == Theme.LIGHT) {
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