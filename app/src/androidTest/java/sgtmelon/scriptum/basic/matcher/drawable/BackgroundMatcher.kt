package sgtmelon.scriptum.basic.matcher.drawable

import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat

/**
 * Matcher for check android:background which gets with [resourceId].
 */
class BackgroundMatcher(@IdRes resourceId: Int) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is View) return false

        val context = item.context ?: return false
        val expected = ContextCompat.getDrawable(context, resourceId) ?: return false

        return compare(item.background, expected)
    }

}