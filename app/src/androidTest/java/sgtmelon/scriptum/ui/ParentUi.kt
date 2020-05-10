package sgtmelon.scriptum.ui

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import sgtmelon.scriptum.dagger.module.base.ManagerModule
import sgtmelon.scriptum.dagger.module.base.ProviderModule
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Parent class for access standard UI functions.
 */
abstract class ParentUi {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val preferenceRepo: IPreferenceRepo = PreferenceRepo(
            ProviderModule().providePreferenceKeyProvider(context.resources),
            ProviderModule().providePreferenceDefProvider(context.resources),
            ManagerModule().provideSharedPreferences(context)
    )

    protected val theme: Int get() = preferenceRepo.theme ?: throw NullPointerException()
    protected val repeat: Int get() = preferenceRepo.repeat ?: throw NullPointerException()

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

}