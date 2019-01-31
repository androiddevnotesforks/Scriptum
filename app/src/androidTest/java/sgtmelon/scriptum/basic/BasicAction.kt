package sgtmelon.scriptum.basic

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import sgtmelon.scriptum.R

class BasicAction {

    fun onEnter(@IdRes viewId: Int, text: String): ViewInteraction =
            onView(withId(viewId)).perform(clearText(), typeText(text))

    fun onClick(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(click())

    fun onClickText(@StringRes stringId: Int): ViewInteraction =
            onView(withText(stringId)).perform(click())

    fun onClick(@IdRes recyclerId: Int, position: Int): ViewInteraction =
            onView(withId(recyclerId))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))

    fun onScroll(@IdRes recyclerId: Int, position: Int): ViewInteraction =
            onView(withId(recyclerId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position))

    fun onSwipeLeft(@IdRes viewId: Int):
            ViewInteraction = onView(withId(viewId)).perform(swipeLeft())

    fun onSwipeRight(@IdRes viewId: Int):
            ViewInteraction = onView(withId(viewId)).perform(swipeRight())

}