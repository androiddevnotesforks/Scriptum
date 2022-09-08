package sgtmelon.test.cappuccino.matchers.drawable

import android.view.View
import androidx.annotation.IdRes
import sgtmelon.extensions.getDrawableCompat

/**
 * Matcher for check android:background which gets with [resourceId].
 */
class BackgroundDrawableMatcher(@IdRes resourceId: Int) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        if (resourceId == null) return item.background == null

        setSize(item)

        val context = item.context ?: return false
        val expected = context.getDrawableCompat(resourceId)?.mutate() ?: return false

        return compare(expected, item.background)
    }
}