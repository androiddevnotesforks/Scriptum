package sgtmelon.test.cappuccino.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import kotlin.random.Random
import org.hamcrest.Matcher
import sgtmelon.test.cappuccino.actions.SeekBarAction


private fun actionOnView(viewMatcher: Matcher<View>, vararg action: ViewAction) {
    onView(viewMatcher).perform(*action)
}


fun Matcher<View>.click() = also { actionOnView(it, ViewActions.click()) }

fun Matcher<View>.longClick() = also { actionOnView(it, ViewActions.longClick()) }

fun Matcher<View>.typeText(text: String) = also {
    actionOnView(it, clearText(), ViewActions.typeText(text))
}

fun Matcher<View>.imeOption() = also { actionOnView(it, ViewActions.pressImeActionButton()) }


fun Matcher<View>.swipeLeft() = also { actionOnView(it, ViewActions.swipeLeft()) }

fun Matcher<View>.swipeRight() = also { actionOnView(it, ViewActions.swipeRight()) }

fun Matcher<View>.swipeUp() = also { actionOnView(it, ViewActions.swipeUp()) }

fun Matcher<View>.swipeDown() = also { actionOnView(it, ViewActions.swipeDown()) }

//region RecyclerView

fun Matcher<View>.scrollTo(itemMatcher: Matcher<View>) = also {
    actionOnView(it, RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(itemMatcher))
}

fun Matcher<View>.scrollTo(p: Int) = also {
    actionOnView(it, actionOnItemAtPosition<RecyclerView.ViewHolder>(p, ViewActions.scrollTo()))
}


fun Matcher<View>.click(p: Int) = also {
    actionOnView(it, actionOnItemAtPosition<RecyclerView.ViewHolder>(p, ViewActions.click()))
}

fun Matcher<View>.longClick(p: Int) = also {
    actionOnView(it, actionOnItemAtPosition<RecyclerView.ViewHolder>(p, ViewActions.longClick()))
}

fun Matcher<View>.swipeItem(p: Int) = also {
    val action = if (Random.nextBoolean()) ViewActions.swipeLeft() else ViewActions.swipeRight()

    onView(it).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(p, action))
}

//endregion

fun Matcher<View>.setProgress(progress: Int) = also { actionOnView(it, SeekBarAction(progress)) }