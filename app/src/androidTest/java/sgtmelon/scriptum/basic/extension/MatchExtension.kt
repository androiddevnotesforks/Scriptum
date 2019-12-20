package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.annotation.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.matcher.ContentDescriptionMatcher
import sgtmelon.scriptum.basic.matcher.SizeMatcher
import sgtmelon.scriptum.basic.matcher.card.CardBackgroundAttrMatcher
import sgtmelon.scriptum.basic.matcher.card.CardBackgroundColorMatcher
import sgtmelon.scriptum.basic.matcher.drawable.*
import sgtmelon.scriptum.basic.matcher.text.HintAttrColorMatcher
import sgtmelon.scriptum.basic.matcher.text.TextAttrColorMatcher
import sgtmelon.scriptum.basic.matcher.text.TextSizeMatcher
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

fun Matcher<View>.isEnabled(enabled: Boolean = true,
                            onEnabled: Matcher<View>.() -> Unit = {}) = also {
    matchOnView(it, if (enabled) ViewMatchers.isEnabled() else not(ViewMatchers.isEnabled()))

    if (enabled) apply(onEnabled)
}

fun Matcher<View>.isSelected(selected: Boolean = true) = also {
    matchOnView(it, if (selected) ViewMatchers.isSelected() else not(ViewMatchers.isSelected()))
}

fun Matcher<View>.isChecked(checked: Boolean = true) = also {
    matchOnView(it, if (checked) ViewMatchers.isChecked() else not(ViewMatchers.isChecked()))
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

fun Matcher<View>.withContentDescription(@StringRes stringId: Int) = also {
    matchOnView(it, ContentDescriptionMatcher(stringId, null))
}

fun Matcher<View>.withContentDescription(string: String) = also {
    matchOnView(it, ContentDescriptionMatcher(-1, string))
}


fun Matcher<View>.withBackground(resourceId: Int) = also {
    matchOnView(it, BackgroundMatcher(resourceId))
}

fun Matcher<View>.withBackgroundColor(@ColorRes colorId: Int) = also {
    matchOnView(it, BackgroundColorMatcher(colorId, -1, -1))
}

fun Matcher<View>.withBackgroundAppColor(@Theme theme: Int, @Color color: Int,
                                         needDark: Boolean) = also {
    matchOnView(it, BackgroundAppColorMatcher(theme, color, needDark))
}

fun Matcher<View>.withBackgroundAttr(@AttrRes attrId: Int) = also {
    matchOnView(it, BackgroundColorMatcher(-1, attrId, -1))
}


fun Matcher<View>.withDrawableColor(resourceId: Int = -1, @ColorRes colorId: Int = -1) = also {
    matchOnView(it, DrawableMatcher(resourceId, colorId, -1))
}

fun Matcher<View>.withDrawableAttr(resourceId: Int, @AttrRes attrColor: Int = -1) = also {
    matchOnView(it, DrawableMatcher(resourceId, -1, attrColor))
}


fun Matcher<View>.withColorIndicator(resourceId: Int = -1,
                                     @Theme theme: Int,
                                     @Color color: Int) = also {
    matchOnView(it, ColorIndicatorMatcher(resourceId, theme, color))
}

fun Matcher<View>.withCardBackground(@Theme theme: Int, @Color color: Int) = also {
    matchOnView(it, CardBackgroundColorMatcher(theme, color))
}

fun Matcher<View>.withCardBackground(@AttrRes attrColor: Int) = also {
    matchOnView(it, CardBackgroundAttrMatcher(attrColor))
}

fun Matcher<View>.withSize(@DimenRes widthId: Int = -1, @DimenRes heightId: Int = -1) = also {
    matchOnView(it, SizeMatcher(widthId, heightId))
}