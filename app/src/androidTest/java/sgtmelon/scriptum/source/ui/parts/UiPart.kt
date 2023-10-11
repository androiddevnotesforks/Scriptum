package sgtmelon.scriptum.source.ui.parts

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.source.di.TestInjector
import sgtmelon.test.cappuccino.automator.CommandAutomator

/**
 * Basic UI element for tests.
 */
abstract class UiPart {

    protected val context = TestInjector.provideContext()
    protected val preferencesRepo = TestInjector.providePreferencesRepo()

    protected val theme
        get() = when (preferencesRepo.theme) {
            Theme.LIGHT -> ThemeDisplayed.LIGHT
            Theme.DARK -> ThemeDisplayed.DARK
            Theme.SYSTEM -> throw IllegalStateException("Not available theme")
        }

    protected val commandAutomator = CommandAutomator(TestInjector.provideUiDevice())

    protected open fun getView(@IdRes viewId: Int): Matcher<View> = withId(viewId)

    protected fun getViewByText(@StringRes stringId: Int): Matcher<View> = withText(stringId)

    protected fun getViewByText(string: String): Matcher<View> = withText(string)

    protected fun getView(@IdRes viewId: Int, @StringRes stringId: Int): Matcher<View> {
        return allOf(getView(viewId), getViewByText(stringId))
    }
}