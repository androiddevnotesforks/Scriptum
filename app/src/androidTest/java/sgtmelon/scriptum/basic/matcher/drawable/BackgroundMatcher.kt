package sgtmelon.scriptum.basic.matcher.drawable

import android.view.View
import androidx.annotation.IdRes
import sgtmelon.scriptum.extension.getCompatDrawable

/**
 * Matcher for check android:background which gets with [resourceId].
 */
class BackgroundMatcher(@IdRes resourceId: Int) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        if (resourceId == null) return item.background == null

        val context = item.context ?: return false
        val expected = context.getCompatDrawable(resourceId) ?: return false

        return compare(item.background, expected)
    }

}