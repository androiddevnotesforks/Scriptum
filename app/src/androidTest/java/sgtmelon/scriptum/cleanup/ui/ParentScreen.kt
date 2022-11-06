package sgtmelon.scriptum.cleanup.ui

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import sgtmelon.scriptum.cleanup.basic.exception.ThemeException
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Parent class for access standard UI functions.
 */
@Deprecated("Use UiPart/UiParentPart")
abstract class ParentScreen {

    protected val context = ParentInjector.provideContext()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()

    protected val appTheme: ThemeDisplayed get() = theme ?: throw ThemeException()

    //region getView func

    protected fun getViewById(@IdRes viewId: Int): Matcher<View> = withId(viewId)

    protected fun getViewByText(@StringRes stringId: Int): Matcher<View> = withText(stringId)

    protected fun getViewByText(string: String): Matcher<View> = withText(string)

    protected fun getView(@IdRes viewId: Int, @StringRes stringId: Int): Matcher<View> {
        return allOf(getViewById(viewId), getViewByText(stringId))
    }

    protected fun getView(@IdRes viewId: Int, string: String): Matcher<View> {
        return allOf(getViewById(viewId), getViewByText(string))
    }

    protected fun Matcher<View>.includeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, withParent(parentMatcher))
    }

    protected fun Matcher<View>.excludeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, not(withParent(parentMatcher)))
    }

    //endregion

    companion object {
        @Deprecated("Try use PreferencesRepo.theme")
        var theme: ThemeDisplayed? = null
    }
}