package sgtmelon.scriptum.basic.matcher

import android.view.View
import androidx.annotation.StringRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check contentDescription.
 */
class ContentDescriptionMatcher(@StringRes private val stringId: Int): TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false

        return context.getString(stringId) == item.contentDescription
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with contentDescription stringId = $stringId")
    }

}