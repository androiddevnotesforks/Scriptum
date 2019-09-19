package sgtmelon.scriptum.ui.basic

import android.view.View
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import kotlin.random.Random

/**
 * Class contains standard actions with UI elements
 */
class BasicAction {

    fun onEnter(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).perform(clearText(), typeText(text))
    }

    fun onClick(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(click())
    }

    fun onLongClick(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(longClick())
    }

    fun onClickText(@StringRes stringId: Int) {
        onView(withText(stringId)).perform(click())
    }

    fun onClick(@IdRes recyclerId: Int, position: Int) {
        onView(withId(recyclerId)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    fun onLongClick(@IdRes recyclerId: Int, position: Int) {
        onView(withId(recyclerId)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, longClick()))
    }

    fun onScroll(@IdRes recyclerId: Int, position: Int) {
        onView(withId(recyclerId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position))
    }


    fun onSwipeLeft(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(swipeLeft())
    }

    fun onSwipeRight(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(swipeRight())
    }

    fun onSwipeUp(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(swipeUp())
    }

    fun onSwipeDown(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(swipeDown())
    }


    fun onSwipeItem(@IdRes recyclerId: Int, position: Int) {
        onView(withId(recyclerId))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
                        if (Random.nextBoolean()) swipeLeft() else swipeRight()
                ))
    }


    fun onClickToolbarButton() {
        onView(toolbarButtonMatcher).perform(click())
    }

    private val toolbarButtonMatcher: Matcher<View>
        get() = allOf(withParent(withClassName(`is`(Toolbar::class.java.name))), withClassName(anyOf(
                `is`(ImageButton::class.java.name), `is`(AppCompatImageButton::class.java.name)
        )))

}

private fun actionOnView(viewMatcher: Matcher<View>, vararg action: ViewAction) {
    onView(viewMatcher).perform(*action)
}

fun Matcher<View>.click() = also { actionOnView(it, ViewActions.click()) }

fun Matcher<View>.click(p: Int) = also {
    actionOnView(it, actionOnItemAtPosition<RecyclerView.ViewHolder>(p, ViewActions.click()))
}


fun Matcher<View>.typeText(text:String) = also {
    actionOnView(it, clearText(), ViewActions.typeText(text))
}


fun Matcher<View>.swipeLeft() = also { actionOnView(it, ViewActions.swipeLeft()) }

fun Matcher<View>.swipeRight() = also { actionOnView(it, ViewActions.swipeRight()) }

fun Matcher<View>.swipeUp() = also { actionOnView(it, ViewActions.swipeUp()) }

fun Matcher<View>.swipeDown() = also { actionOnView(it, ViewActions.swipeDown()) }