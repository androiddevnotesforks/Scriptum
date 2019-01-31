package sgtmelon.scriptum.basic

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import sgtmelon.scriptum.R

open class BasicMatch {

    protected fun onDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(ViewAssertions.matches(isDisplayed()))

    protected fun notOnDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(ViewAssertions.matches(not(isDisplayed())))

    protected fun onDisplayText(@StringRes stringId: Int): ViewInteraction =
            onView(withText(stringId)).check(ViewAssertions.matches(isDisplayed()))

    protected fun isChecked(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(ViewAssertions.matches(isChecked()))

    protected fun onDisplay(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf<View>(withId(viewId), withText(stringId))).check(matches(isDisplayed()))

    protected fun onDisplay(@IdRes viewId: Int, string: String): ViewInteraction =
            onView(allOf<View>(withId(viewId), withText(string))).check(matches(isDisplayed()))

    protected fun isEnable(@IdRes viewId: Int, enable: Boolean) {
        if (enable) {
            onView(withId(viewId)).check(matches(isEnabled()))
        } else {
            onView(withId(viewId)).check(matches(not<View>(isEnabled())))
        }
    }

}