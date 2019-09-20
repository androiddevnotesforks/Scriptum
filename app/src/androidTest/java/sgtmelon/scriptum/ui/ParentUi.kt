package sgtmelon.scriptum.ui

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import sgtmelon.scriptum.R
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Родительский класс для доступа к стандартному функционалу ui
 */
abstract class ParentUi {

    // TODO remove
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val iPreferenceRepo = PreferenceRepo(context)
    val theme: Int get() = iPreferenceRepo.theme


    protected fun getViewById(@IdRes viewId: Int): Matcher<View> = withId(viewId)

    protected fun getViewByText(@StringRes stringId: Int): Matcher<View> = withText(stringId)

    protected fun getViewByText(text: String): Matcher<View> = withText(text)

    protected fun getToolbar(@StringRes titleId: Int): Matcher<View> {
        return getViewById(R.id.toolbar_container).includeChild(getViewByText(titleId))
    }

    protected fun getToolbarButton(): Matcher<View> = allOf(
            withParent(withClassName(`is`(Toolbar::class.java.name))),
            withClassName(anyOf(
                    `is`(ImageButton::class.java.name),
                    `is`(AppCompatImageButton::class.java.name)
            ))
    )



    protected fun Matcher<View>.withText(text: String): Matcher<View> = let {
        allOf(it, ViewMatchers.withText(text))
    }

    protected fun Matcher<View>.withText(@StringRes stringId: Int): Matcher<View> = let {
        allOf(it, ViewMatchers.withText(stringId))
    }

    protected fun Matcher<View>.withHint(@StringRes stringId: Int): Matcher<View> = let {
        allOf(it, ViewMatchers.withHint(stringId), ViewMatchers.withText(""))
    }

    protected fun Matcher<View>.excludeParent(parentView: Matcher<View>): Matcher<View> = let {
        allOf(it, not(withParent(parentView)))
    }

    private fun Matcher<View>.includeChild(childView: Matcher<View>): Matcher<View> = let {
        allOf(it, withChild(childView))
    }

}