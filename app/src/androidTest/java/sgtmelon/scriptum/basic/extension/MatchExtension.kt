package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.matcher.ContentDescriptionMatcher
import sgtmelon.scriptum.basic.matcher.CursorMatcher
import sgtmelon.scriptum.basic.matcher.FocusMather
import sgtmelon.scriptum.basic.matcher.ProgressMatcher
import sgtmelon.scriptum.basic.matcher.TagMatcher
import sgtmelon.scriptum.basic.matcher.card.CardBackgroundAttrMatcher
import sgtmelon.scriptum.basic.matcher.card.CardBackgroundColorMatcher
import sgtmelon.scriptum.basic.matcher.card.CardElevationMatcher
import sgtmelon.scriptum.basic.matcher.card.CardRadiusMatcher
import sgtmelon.scriptum.basic.matcher.drawable.BackgroundAppColorMatcher
import sgtmelon.scriptum.basic.matcher.drawable.BackgroundColorMatcher
import sgtmelon.scriptum.basic.matcher.drawable.BackgroundMatcher
import sgtmelon.scriptum.basic.matcher.drawable.ColorIndicatorMatcher
import sgtmelon.scriptum.basic.matcher.drawable.DrawableMatcher
import sgtmelon.scriptum.basic.matcher.drawable.MenuItemDrawableMatcher
import sgtmelon.scriptum.basic.matcher.drawable.NavigationDrawableMatcher
import sgtmelon.scriptum.basic.matcher.text.HintAttrColorMatcher
import sgtmelon.scriptum.basic.matcher.text.MenuItemTitleMatcher
import sgtmelon.scriptum.basic.matcher.text.TextAttrColorMatcher
import sgtmelon.scriptum.basic.matcher.text.TextSizeMatcher
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

fun matchOnView(viewMatcher: Matcher<View>, checkMatcher: Matcher<in View>) {
    onView(viewMatcher).check(matches(checkMatcher))
}


fun Matcher<View>.isDisplayed(
    isVisible: Boolean = true,
    onVisible: Matcher<View>.() -> Unit = {}
) = also {
    matchOnView(it, if (isVisible) ViewMatchers.isDisplayed() else not(ViewMatchers.isDisplayed()))

    if (isVisible) apply(onVisible)
}

fun Matcher<View>.isEnabled(
    isEnabled: Boolean = true,
    onEnabled: Matcher<View>.() -> Unit = {}
) = also {
    matchOnView(it, if (isEnabled) ViewMatchers.isEnabled() else not(ViewMatchers.isEnabled()))

    if (isEnabled) apply(onEnabled)
}

fun Matcher<View>.isSelected(isSelected: Boolean = true) = also {
    matchOnView(it, if (isSelected) ViewMatchers.isSelected() else not(ViewMatchers.isSelected()))
}

fun Matcher<View>.isChecked(isChecked: Boolean = true) = also {
    matchOnView(it, if (isChecked) ViewMatchers.isChecked() else not(ViewMatchers.isChecked()))
}

fun Matcher<View>.isFocused(isFocused: Boolean = true) = also {
    matchOnView(it, FocusMather(isFocused))
}

fun Matcher<View>.withText(
    @StringRes stringId: Int,
    @AttrRes attrColor: Int? = null,
    @DimenRes dimenId: Int? = null
) = also {
    matchOnView(it, ViewMatchers.withText(stringId))

    if (attrColor != null) withTextColor(attrColor)
    if (dimenId != null) withTextSize(dimenId)
}

fun Matcher<View>.withText(
    string: String,
    @AttrRes attrColor: Int? = null,
    @DimenRes dimenId: Int? = null
) = also {
    matchOnView(it, ViewMatchers.withText(string))

    if (attrColor != null) withTextColor(attrColor)
    if (dimenId != null) withTextSize(dimenId)
}

fun Matcher<View>.withImeAction(action: Int) = also {
    matchOnView(it, ViewMatchers.hasImeAction(action))
}

fun Matcher<View>.withCursor(p: Int) = also {
    matchOnView(it, CursorMatcher(p))
}


fun Matcher<View>.withTextColor(@AttrRes attrColor: Int) = also {
    matchOnView(it, TextAttrColorMatcher(attrColor))
}

fun Matcher<View>.withTextSize(@DimenRes dimenId: Int) = also {
    matchOnView(it, TextSizeMatcher(dimenId))
}

fun Matcher<View>.withHint(
    @StringRes stringId: Int,
    @AttrRes attrColor: Int? = null,
    @DimenRes dimenId: Int? = null
) = also {
    matchOnView(it, allOf(ViewMatchers.withHint(stringId), ViewMatchers.withText("")))

    if (attrColor != null) withHintColor(attrColor)
    if (dimenId != null) withTextSize(dimenId)
}

fun Matcher<View>.withMenuTitle(@IdRes itemId: Int, @StringRes stringId: Int) = also {
    matchOnView(it, MenuItemTitleMatcher(itemId, stringId))
}


fun Matcher<View>.withHintColor(@AttrRes attrColor: Int) = also {
    matchOnView(it, HintAttrColorMatcher(attrColor))
}

fun Matcher<View>.withContentDescription(@StringRes stringId: Int) = also {
    matchOnView(it, ContentDescriptionMatcher(stringId, null))
}

fun Matcher<View>.withContentDescription(string: String) = also {
    matchOnView(it, ContentDescriptionMatcher(null, string))
}


fun Matcher<View>.withBackground(@DrawableRes resourceId: Int) = also {
    matchOnView(it, BackgroundMatcher(resourceId))
}

fun Matcher<View>.withBackgroundColor(@ColorRes colorId: Int) = also {
    matchOnView(it, BackgroundColorMatcher(colorId, null))
}

fun Matcher<View>.withBackgroundAppColor(
    theme: ThemeDisplayed,
    color: Color,
    needDark: Boolean
) = also {
    matchOnView(it, BackgroundAppColorMatcher(theme, color, needDark))
}

fun Matcher<View>.withBackgroundAttr(@AttrRes attrId: Int) = also {
    matchOnView(it, BackgroundColorMatcher(null, attrId))
}


fun Matcher<View>.withDrawable(
    @IdRes resourceId: Int? = null
) = also {
    matchOnView(it, DrawableMatcher(resourceId, null, null))
}

fun Matcher<View>.withDrawableColor(
    @IdRes resourceId: Int? = null,
    @ColorRes colorId: Int? = null
) = also {
    matchOnView(it, DrawableMatcher(resourceId, colorId, null))
}

fun Matcher<View>.withDrawableAttr(
    @IdRes resourceId: Int?,
    @AttrRes attrColor: Int? = null
) = also {
    matchOnView(it, DrawableMatcher(resourceId, null, attrColor))
}

fun Matcher<View>.withNavigationDrawable(
    @IdRes resourceId: Int?,
    @AttrRes attrColor: Int? = null
) = also {
    matchOnView(it, NavigationDrawableMatcher(resourceId, attrColor))
}

fun Matcher<View>.withMenuDrawable(
    @IdRes itemId: Int,
    @IdRes resourceId: Int,
    @AttrRes attrColor: Int = R.attr.clContent
) = also {
    matchOnView(it, MenuItemDrawableMatcher(itemId, resourceId, attrColor))
}


fun Matcher<View>.withColorIndicator(
    resourceId: Int? = null,
    theme: ThemeDisplayed,
    color: Color
) = also {
    matchOnView(it, ColorIndicatorMatcher(resourceId, theme, color))
}

fun Matcher<View>.withCardBackground(
    theme: ThemeDisplayed,
    color: Color,
    @DimenRes radiusId: Int,
    @DimenRes elevationId: Int
) = also {
    matchOnView(it, CardBackgroundColorMatcher(theme, color))
    matchOnView(it, CardRadiusMatcher(radiusId))
    matchOnView(it, CardElevationMatcher(elevationId))
}

fun Matcher<View>.withCardBackground(
    @AttrRes attrColor: Int,
    @DimenRes radiusId: Int,
    @DimenRes elevationId: Int
) = also {
    matchOnView(it, CardBackgroundAttrMatcher(attrColor))
    matchOnView(it, CardRadiusMatcher(radiusId))
    matchOnView(it, CardElevationMatcher(elevationId))
}


fun Matcher<View>.withProgress(progress: Int, max: Int) = also {
    matchOnView(it, ProgressMatcher(progress, max))
}


fun Matcher<View>.withTag(tag: Any) = also { matchOnView(it, TagMatcher(tag)) }