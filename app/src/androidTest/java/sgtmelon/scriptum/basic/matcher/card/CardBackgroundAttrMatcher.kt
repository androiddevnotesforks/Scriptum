package sgtmelon.scriptum.basic.matcher.card

import android.view.View
import androidx.annotation.AttrRes
import androidx.cardview.widget.CardView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getColorAttr

/**
 * Matcher for check app:cardBackground which gets with [attrColor].
 */
class CardBackgroundAttrMatcher(@AttrRes private val attrColor: Int) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is CardView) return false

        val context = item.context ?: return false
        val backgroundColor = context.getColorAttr(attrColor)

        return backgroundColor == item.cardBackgroundColor.defaultColor
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with attrColor = $attrColor")
    }

}