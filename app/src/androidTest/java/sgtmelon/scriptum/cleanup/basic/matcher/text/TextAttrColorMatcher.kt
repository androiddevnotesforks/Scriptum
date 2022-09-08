package sgtmelon.scriptum.cleanup.basic.matcher.text

import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.extensions.getColorAttr

/**
 * Matcher for check text color via attr
 */
class TextAttrColorMatcher(@AttrRes private val attrColor: Int):  TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextView) return false

        val context = item.context ?: return false
        val color = context.getColorAttr(attrColor)

        return color == item.currentTextColor
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with text attrColor = $attrColor")
    }

}