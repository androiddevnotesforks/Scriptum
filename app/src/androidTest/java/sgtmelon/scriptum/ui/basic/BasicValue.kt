package sgtmelon.scriptum.ui.basic

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun Matcher<View>.getCount(): Int = let {
    var count = 0

    val recyclerMatcher = object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View): Boolean {
            if (item !is RecyclerView) return false

            count = item.adapter?.itemCount ?: return false

            return true
        }

        override fun describeTo(description: Description) {}
    }

    onView(it).check(ViewAssertions.matches(recyclerMatcher))

    return count
}

fun Matcher<View>.getRandomPosition(): Int = let { (0 until it.getCount()).random() }