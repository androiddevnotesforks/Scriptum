package sgtmelon.scriptum.basic.matcher.drawable

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Description
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.ColorData
import sgtmelon.scriptum.extension.getCompatDrawable
import sgtmelon.scriptum.extension.setColor

/**
 * Matcher for check android:background which gets with [resourceId].
 *
 * * If [resourceId] is -1 => check what don't have drawable.
 */
class ColorIndicatorMatcher(@IdRes resourceId: Int, @Theme theme: Int, @Color color: Int) :
        ParentImageMatcher(resourceId) {

    private val colorItem = ColorData.getColorItem(theme, color)

    override fun describeTo(description: Description?) {
        super.describeTo(description)
        description?.appendText("\nView with colorItem = $colorItem")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        setSize(item)

        if (resourceId == -1) return item.background == null

        val context = item.context ?: return false
        val expected = context.getCompatDrawable(resourceId) ?: return false

        expected.setColor(context, colorItem)

        return compare(item.background, expected)
    }

}