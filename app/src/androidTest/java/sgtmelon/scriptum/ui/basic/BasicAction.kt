package sgtmelon.scriptum.ui.basic

import android.view.View
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * Class contains standard actions with UI elements
 */
class BasicAction {

    fun onEnter(@IdRes viewId: Int, text: String): ViewInteraction =
            onView(withId(viewId)).perform(clearText(), typeText(text))

    fun onClick(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(click())

    fun onLongClick(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(longClick())

    fun onClickText(@StringRes stringId: Int): ViewInteraction =
            onView(withText(stringId)).perform(click())

    fun onClick(@IdRes recyclerId: Int, position: Int): ViewInteraction =
            onView(withId(recyclerId))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))

    fun onLongClick(@IdRes recyclerId: Int, position: Int): ViewInteraction =
            onView(withId(recyclerId))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, longClick()))

    fun onScroll(@IdRes recyclerId: Int, position: Int): ViewInteraction =
            onView(withId(recyclerId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position))

    fun onSwipeLeft(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(swipeLeft())

    fun onSwipeRight(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(swipeRight())

    fun onSwipeUp(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(swipeUp())

    fun onSwipeDown(@IdRes viewId: Int): ViewInteraction =
            onView(withId(viewId)).perform(swipeDown())

    fun onClickToolbarButton(): ViewInteraction = onView(toolbarButtonMatcher).perform(click())

    private val toolbarButtonMatcher: Matcher<View>
        get() = allOf(withParent(withClassName(`is`(Toolbar::class.java.name))), withClassName(anyOf(
                `is`(ImageButton::class.java.name), `is`(AppCompatImageButton::class.java.name)
        )))
}