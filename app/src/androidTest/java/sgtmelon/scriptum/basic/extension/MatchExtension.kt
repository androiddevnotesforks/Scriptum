package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.matcher.*
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme


private fun matchOnView(viewMatcher: Matcher<View>, checkMatcher: Matcher<in View>) {
    onView(viewMatcher).check(matches(checkMatcher))
}


fun Matcher<View>.isDisplayed(visible: Boolean = true,
                              onVisible: Matcher<View>.() -> Unit = {}) = also {
    matchOnView(it, if (visible) ViewMatchers.isDisplayed() else not(ViewMatchers.isDisplayed()))

    if (visible) apply(onVisible)
}

fun Matcher<View>.isEnabled(enabled: Boolean = true) = also {
    matchOnView(it, if (enabled) ViewMatchers.isEnabled() else not(ViewMatchers.isEnabled()))
}

fun Matcher<View>.isSelected(selected: Boolean = true) = also {
    matchOnView(it, if (selected) ViewMatchers.isSelected() else not(ViewMatchers.isSelected()))
}

fun Matcher<View>.withText(@StringRes stringId: Int,
                           @AttrRes attrColor: Int = -1,
                           @DimenRes dimenId: Int = -1) = also {
    matchOnView(it, ViewMatchers.withText(stringId))

    if (attrColor != -1) withTextColor(attrColor)
    if (dimenId != -1) withTextSize(dimenId)
}

fun Matcher<View>.withText(string: String,
                           @AttrRes attrColor: Int = -1,
                           @DimenRes dimenId: Int = -1) = also {
    matchOnView(it, ViewMatchers.withText(string))

    if (attrColor != -1) withTextColor(attrColor)
    if (dimenId != -1) withTextSize(dimenId)
}


fun Matcher<View>.withTextColor(@AttrRes attrColor: Int) = also {
    matchOnView(it, TextAttrColorMatcher(attrColor))
}

fun Matcher<View>.withTextSize(@DimenRes dimenId: Int) = also {
    matchOnView(it, TextSizeMatcher(dimenId))
}

fun Matcher<View>.withHint(@StringRes stringId: Int,
                           @AttrRes attrColor: Int = -1,
                           @DimenRes dimenId: Int = -1) = also {
    matchOnView(it, allOf(ViewMatchers.withHint(stringId), ViewMatchers.withText("")))

    if (attrColor != -1) withHintColor(attrColor)
    if (dimenId != -1) withTextSize(dimenId)
}

fun Matcher<View>.withHintColor(@AttrRes attrColor: Int) = also {
    matchOnView(it, HintAttrColorMatcher(attrColor))
}

fun Matcher<View>.withDrawableColor(resourceId: Int = -1, @ColorRes colorId: Int = -1) = also {
    matchOnView(it, DrawableColorMatcher(resourceId, colorId))
}

fun Matcher<View>.withDrawableAttr(resourceId: Int, @AttrRes attrColor: Int = -1) = also {
    matchOnView(it, DrawableAttrMatcher(resourceId, attrColor))
}

fun Matcher<View>.withColorIndicator(resourceId: Int = -1,
                                     @Theme theme: Int,
                                     @Color color: Int) = also {
    matchOnView(it, ColorIndicatorMatcher(resourceId, theme, color))
}