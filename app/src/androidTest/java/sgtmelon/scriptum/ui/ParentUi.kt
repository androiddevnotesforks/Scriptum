package sgtmelon.scriptum.ui

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import sgtmelon.scriptum.basic.exception.ThemeException
import sgtmelon.scriptum.cleanup.dagger.module.base.ProviderModule
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme

/**
 * Parent class for access standard UI functions.
 */
abstract class ParentUi {

    // TODO get all this data from ParentTest class

    protected val context: Context = getInstrumentation().targetContext

    protected val preferences: Preferences = PreferencesImpl(
        ProviderModule().providePreferenceKeyProvider(context.resources),
        ProviderModule().providePreferenceDefProvider(context.resources),
        ProviderModule().provideSharedPreferences(context)
    )

    protected val appTheme: Int get() = theme ?: throw ThemeException()

    //region getView func

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

    protected fun Matcher<View>.includeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, withParent(parentMatcher))
    }

    protected fun Matcher<View>.excludeParent(parentMatcher: Matcher<View>): Matcher<View> = let {
        allOf(it, not(withParent(parentMatcher)))
    }

    //endregion

    companion object {
        @Theme var theme: Int? = null
    }
}