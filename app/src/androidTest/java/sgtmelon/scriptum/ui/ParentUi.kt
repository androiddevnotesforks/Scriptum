package sgtmelon.scriptum.ui

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
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

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val theme: Int get() = PreferenceRepo(context).theme

    protected fun getViewByName(name: String): Matcher<View> = withResourceName(name)

    protected fun getViewById(@IdRes viewId: Int): Matcher<View> = withId(viewId)

    protected fun getViewByText(@StringRes stringId: Int): Matcher<View> = withText(stringId)

    protected fun getViewByText(string: String): Matcher<View> = withText(string)

    protected fun getView(@IdRes viewId: Int, @StringRes stringId: Int): Matcher<View> {
        return allOf(getViewById(viewId), getViewByText(stringId))
    }

    protected fun getView(@IdRes viewId: Int, string: String): Matcher<View> {
        return allOf(getViewById(viewId), getViewByText(string))
    }


    protected fun getToolbar(@StringRes titleId: Int): Matcher<View> {
        return allOf(getViewById(R.id.toolbar_container), withChild(getViewByText(titleId)))
    }

    protected fun getToolbarButton(): Matcher<View> = allOf(
            withParent(withClassName(`is`(Toolbar::class.java.name))),
            withClassName(anyOf(
                    `is`(ImageButton::class.java.name),
                    `is`(AppCompatImageButton::class.java.name)
            ))
    )


    protected fun Matcher<View>.includeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, withParent(parentMatcher))
    }

    protected fun Matcher<View>.excludeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, not(withParent(parentMatcher)))
    }

}