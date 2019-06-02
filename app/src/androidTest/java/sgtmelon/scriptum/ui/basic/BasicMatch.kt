package sgtmelon.scriptum.ui.basic

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Класс содержащий стандартные проверки для ui
 *
 * @author SerjantArbuz
 */
abstract class BasicMatch {

    private val context: Context = getInstrumentation().targetContext
    private val preference = PreferenceRepo(context)

    protected val theme: Int get() = preference.getTheme()

    protected fun onDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(isDisplayed()))

    protected fun notDisplay(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(not(isDisplayed())))

    protected fun onDisplay(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withId(viewId), withText(stringId))).check(matches(isDisplayed()))

    protected fun notDisplay(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withId(viewId), withText(stringId))).check(matches(not(isDisplayed())))

    protected fun onDisplayToolbar(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withParent(withId(viewId)), withText(stringId))).check(matches(isDisplayed()))

    protected fun isSelected(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).check(matches(isSelected()))

    protected fun isEnabled(@IdRes viewId: Int, enabled: Boolean): ViewInteraction =
            onView(withId(viewId)).check(matches(if (enabled) isEnabled() else not(isEnabled())))

    protected fun isEnabledText(@StringRes stringId: Int, enabled: Boolean): ViewInteraction =
            onView(withText(stringId)).check(matches(if (enabled) isEnabled() else not(isEnabled())))

    protected fun onDisplayText(@StringRes stringId: Int): ViewInteraction =
            onView(withText(stringId)).check(matches(isDisplayed()))

    protected fun onDisplayText(string: String): ViewInteraction =
            onView(withText(string)).check(matches(isDisplayed()))

    protected fun onDisplay(@IdRes viewId: Int, string: String): ViewInteraction =
            onView(allOf(withId(viewId), withText(string))).check(matches(isDisplayed()))

    protected fun onDisplayHint(@IdRes viewId: Int, @StringRes stringId: Int): ViewInteraction =
            onView(allOf(withId(viewId), withHint(stringId), withText(""))).check(matches(isDisplayed()))

}