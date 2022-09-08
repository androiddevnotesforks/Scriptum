package sgtmelon.test.cappuccino.matchers.text

import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for text size inside [TextView]..
 */
class TextSizeMatcher(@DimenRes private val dimenId: Int) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextView) return false

        val context = item.context ?: return false

        return context.resources.getDimension(dimenId) == item.textSize
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with dimenId = $dimenId")
    }
}