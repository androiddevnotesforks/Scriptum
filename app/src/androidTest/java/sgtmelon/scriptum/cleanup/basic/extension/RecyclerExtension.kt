package sgtmelon.scriptum.cleanup.basic.extension

import android.view.View
import android.widget.ListView
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
            count = when (item) {
                is RecyclerView -> item.adapter?.itemCount ?: return false
                is ListView -> item.adapter?.count ?: return false
                else -> return false
            }

            return true
        }

        override fun describeTo(description: Description) = Unit
    }

    onView(it).check(ViewAssertions.matches(recyclerMatcher))

    return count
}

fun Matcher<View>.getRandomPosition(): Int? = let {
    val count = it.getCount()
    return@let if (count != 0) (0 until count).random() else null
}