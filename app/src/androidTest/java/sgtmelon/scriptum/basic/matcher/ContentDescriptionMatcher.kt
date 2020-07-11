package sgtmelon.scriptum.basic.matcher

import android.view.View
import androidx.annotation.StringRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for check contentDescription.
 */
class ContentDescriptionMatcher(
        @StringRes private val stringId: Int?,
        private val string: String?
) : TypeSafeMatcher<View>() {

    init {
        if (stringId == null && string == null) throw IllegalAccessException()
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        val context = item.context ?: return false

        val expected = when {
            stringId != null -> context.getString(stringId)
            string != null -> string
            else -> return false
        }

        return expected == item.contentDescription
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with contentDescription stringId = $stringId | string = $string")
    }

}