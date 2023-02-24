package sgtmelon.scriptum.parent.ui.basic

import android.view.View
import androidx.annotation.DimenRes
import org.hamcrest.Matcher
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.test.cappuccino.matchers.card.CardElevationMatcher
import sgtmelon.test.cappuccino.matchers.card.CardRadiusMatcher
import sgtmelon.test.cappuccino.utils.matchOnView

fun Matcher<View>.withBackgroundAppColor(
    theme: ThemeDisplayed,
    color: Color,
    needDark: Boolean
) = also {
    matchOnView(it, BackgroundAppColorMatcher(theme, color, needDark))
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