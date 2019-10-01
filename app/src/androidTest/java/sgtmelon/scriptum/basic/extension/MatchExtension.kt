package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.matcher.ColorIndicatorMatcher
import sgtmelon.scriptum.basic.matcher.DrawableMatcher
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme


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

fun Matcher<View>.haveText(string: String) = also { matchOnView(it, withText(string)) }

fun Matcher<View>.haveHint(@StringRes stringId: Int) = also {
    matchOnView(it, allOf(withHint(stringId), withText("")))
}

fun Matcher<View>.withDrawable(resourceId: Int = -1, @AttrRes attrColor: Int = -1) = also {
    matchOnView(it, DrawableMatcher(resourceId, attrColor))
}

fun Matcher<View>.withColorIndicator(resourceId: Int = -1,
                                     @Theme theme: Int,
                                     @Color color: Int) = also {
    matchOnView(it, ColorIndicatorMatcher(resourceId, theme, color))
}