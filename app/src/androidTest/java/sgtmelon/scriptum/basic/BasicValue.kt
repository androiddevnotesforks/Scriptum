package sgtmelon.scriptum.basic

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class BasicValue {

    fun getCount(@IdRes recyclerId: Int): Int {
        var count = 0

        val recyclerMatcher = object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View): Boolean {
                if (item !is RecyclerView) return false

                count = item.adapter!!.itemCount
                return true
            }

            override fun describeTo(description: Description) {}
        }

        onView(withId(recyclerId)).check(matches(recyclerMatcher))

        return count
    }

}