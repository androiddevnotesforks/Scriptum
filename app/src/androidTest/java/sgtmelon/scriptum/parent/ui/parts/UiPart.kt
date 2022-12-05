package sgtmelon.scriptum.parent.ui.parts

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Basic UI element for tests.
 */
abstract class UiPart {

    protected val context = ParentInjector.provideContext()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()

    protected val theme
        get() = when (preferencesRepo.theme) {
            Theme.LIGHT -> ThemeDisplayed.LIGHT
            Theme.DARK -> ThemeDisplayed.DARK
            Theme.SYSTEM -> throw IllegalStateException("Not available theme")
        }

    protected open fun getView(@IdRes viewId: Int): Matcher<View> = withId(viewId)

    protected fun getViewByText(@StringRes stringId: Int): Matcher<View> = withText(stringId)

    protected fun getViewByText(string: String): Matcher<View> = withText(string)

    protected fun getView(@IdRes viewId: Int, @StringRes stringId: Int): Matcher<View> {
        return allOf(getView(viewId), getViewByText(stringId))
    }
}