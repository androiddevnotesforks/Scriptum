package sgtmelon.scriptum.basic.matcher

import android.view.View
import android.widget.EditText
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check cursor position.
 */
class CursorMatcher(private val position: Int) : TypeSafeMatcher<View>() {

    private var positionActual: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null || item !is EditText) return false

        return position == item.selectionEnd.also { positionActual = it }
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nExpected: cursor = $position")
        description?.appendText(" | Actual: cursor = $positionActual")
    }

}