package sgtmelon.scriptum.basic.matcher

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TagMatcher(private val tag: Any) : TypeSafeMatcher<View>() {

    private var actualTag: Any? = null

    override fun matchesSafely(item: View?) = tag == item?.tag?.also { actualTag = it }

    override fun describeTo(description: Description?) {
        description?.appendText("Expected: tag = $tag | Actual tag = $actualTag")
    }

}