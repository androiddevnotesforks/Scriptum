package sgtmelon.scriptum.source.ui.basic

import android.view.View
import androidx.annotation.DimenRes
import org.hamcrest.Matcher
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.widgets.SmoothProgressBar
import sgtmelon.test.cappuccino.matchers.card.CardElevationMatcher
import sgtmelon.test.cappuccino.matchers.card.CardRadiusMatcher
import sgtmelon.test.cappuccino.utils.matchOnView
import sgtmelon.test.cappuccino.utils.withProgress

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

fun Matcher<View>.withSmoothProgress(progress: Int, max: Int) = also {
    val scale = SmoothProgressBar.ANIM_SCALE
    withProgress(progress = progress * scale, max = max * scale)
}