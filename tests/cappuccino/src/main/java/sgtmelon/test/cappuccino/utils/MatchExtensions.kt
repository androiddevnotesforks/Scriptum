package sgtmelon.test.cappuccino.utils

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import org.hamcrest.Matcher


fun matchOnView(viewMatcher: Matcher<View>, checkMatcher: Matcher<in View>) {
    Espresso.onView(viewMatcher).check(ViewAssertions.matches(checkMatcher))
}
