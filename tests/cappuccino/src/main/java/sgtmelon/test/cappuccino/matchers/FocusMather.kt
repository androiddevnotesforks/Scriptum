package sgtmelon.test.cappuccino.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check view focus.
 */
class FocusMather(private val isFocused: Boolean) : TypeSafeMatcher<View>(){

    private var isFocusedActual: Boolean? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null || !item.isFocusable) return false

        return isFocused == item.isFocused.also { isFocusedActual = it }
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nExpected: focus = $isFocused")
        description?.appendText(" | Actual: focus = $isFocusedActual")
    }
}