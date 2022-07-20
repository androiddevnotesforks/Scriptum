package sgtmelon.scriptum.ui

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import sgtmelon.scriptum.basic.exception.ThemeException
import sgtmelon.scriptum.cleanup.dagger.module.base.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.base.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.RepositoryModule
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Parent class for access standard UI functions.
 */
abstract class ParentUi {

    // TODO get all this data from ParentTest class

    protected val context: Context = getInstrumentation().targetContext

    protected val preferences: Preferences = PreferencesModule().providePreferences(
        PreferencesModule().providePreferenceKeyProvider(context.resources),
        PreferencesModule().providePreferenceDefProvider(context.resources),
        PreferencesModule().provideSharedPreferences(context)
    )

    /**
     * It's needed for get enum values (already converted from [preferences]).
     */
    protected val preferencesRepo: PreferencesRepo = RepositoryModule().providePreferencesRepo(
        DataSourceModule().providePreferencesDataSource(preferences),
        ConverterModule().provideThemeConverter(),
        ConverterModule().provideSortConverter(),
        ConverterModule().provideColorConverter(),
        ConverterModule().provideSavePeriodConverter(),
        ConverterModule().provideRepeatConverter(),
        ConverterModule().provideSignalConverter()
    )

    protected val appTheme: ThemeDisplayed get() = theme ?: throw ThemeException()

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
        var theme: ThemeDisplayed? = null
    }
}