package sgtmelon.test.cappuccino.matchers

import android.view.View
import android.widget.EditText
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check selection positions inside [EditText].
 */
class SelectionMatcher(
    private val from: Int,
    private val to: Int
) : TypeSafeMatcher<View>() {

    private var fromActual: Int? = null
    private var toActual: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null || item !is EditText) return false

        fromActual = item.selectionStart
        toActual = item.selectionEnd

        return from == fromActual && to == toActual
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nExpected: from = $from, to = $to")
        description?.appendText(" | Actual: from = $fromActual, to = $toActual")
    }
}