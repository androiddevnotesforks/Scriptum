package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

private fun matchOnView(viewMatcher: Matcher<View>, checkMatcher: Matcher<in View>) {
    onView(viewMatcher).check(matches(checkMatcher))
}


fun Matcher<View>.isDisplayed(visible: Boolean = true) = also {
    matchOnView(it, if (visible) ViewMatchers.isDisplayed() else not(ViewMatchers.isDisplayed()))
}

fun Matcher<View>.isEnabled(enabled: Boolean = true) = also {
    matchOnView(it, if (enabled) ViewMatchers.isEnabled() else not(ViewMatchers.isEnabled()))
}

fun Matcher<View>.isSelected(selected: Boolean = true) = also {
    matchOnView(it, if (selected) ViewMatchers.isSelected() else not(ViewMatchers.isSelected()))
}