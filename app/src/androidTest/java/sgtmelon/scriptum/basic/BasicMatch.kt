package sgtmelon.scriptum.basic

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

open class BasicMatch {

    protected fun onDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(isDisplayed()))

    protected fun doesNotDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(not(isDisplayed())))

    protected fun onDisplay(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withId(viewId), withText(stringId))).check(matches(isDisplayed()))

    protected fun doesNotDisplay(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withId(viewId), withText(stringId))).check(matches(not(isDisplayed())))

    protected fun onDisplayToolbar(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withParent(withId(viewId)), withText(stringId))).check(matches(isDisplayed()));

    protected fun isSelected(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(isSelected()))

    protected fun isEnable(@IdRes viewId: Int, enable: Boolean) {
        if (enable) {
            onView(withId(viewId)).check(matches(isEnabled()))
        } else {
            onView(withId(viewId)).check(matches(not(isEnabled())))
        }
    }



    protected fun onDisplayText(@StringRes stringId: Int): ViewInteraction =
            onView(withText(stringId)).check(matches(isDisplayed()))

    protected fun onDisplay(@IdRes viewId: Int, string: String): ViewInteraction =
            onView(allOf(withId(viewId), withText(string))).check(matches(isDisplayed()))

}