package sgtmelon.scriptum.cleanup.basic.matcher.drawable

import android.view.View
import androidx.annotation.IdRes

/**
 * Matcher for check android:background which gets with [resourceId].
 */
class BackgroundMatcher(@IdRes resourceId: Int) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        if (resourceId == null) return item.background == null

        setSize(item)

        val context = item.context ?: return false
        val expected = context.getDrawable(resourceId)?.mutate() ?: return false

        return compare(expected, item.background)
    }
}