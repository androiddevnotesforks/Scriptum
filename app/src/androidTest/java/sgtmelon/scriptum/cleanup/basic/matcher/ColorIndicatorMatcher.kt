package sgtmelon.scriptum.cleanup.basic.matcher

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Description
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData
import sgtmelon.scriptum.cleanup.extension.setColor
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.test.cappuccino.matchers.drawable.ParentImageMatcher

/**
 * Matcher for check android:background which gets with [resourceId].
 *
 * * If [resourceId] is null => check what don't have drawable.
 */
class ColorIndicatorMatcher(
    @IdRes resourceId: Int?,
    theme: ThemeDisplayed,
    color: Color
) : ParentImageMatcher(resourceId) {

    private val colorItem = ColorData.getColorItem(theme, color)

    override fun describeTo(description: Description?) {
        super.describeTo(description)
        description?.appendText("\nView with colorItem = $colorItem")
    }

    override fun matchesSafely(item: View?): Boolean {
        val resourceId = resourceId

        if (item !is View) return false

        setSize(item)

        if (resourceId == null) return item.background == null

        val context = item.context ?: return false
        val expected = context.getDrawableCompat(resourceId)?.mutate() ?: return false

        expected.setColor(context, colorItem)

        return compare(expected, item.background)
    }
}