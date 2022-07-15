package sgtmelon.scriptum.ui.logic.parent

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import sgtmelon.scriptum.cleanup.dagger.module.base.ProviderModule
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.infrastructure.preferences.AppPreferencesImpl

/**
 * Parent class for UI tests simple logic.
 */
abstract class ParentLogic {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val appPreferences: AppPreferences = AppPreferencesImpl(
        ProviderModule().providePreferenceKeyProvider(context.resources),
        ProviderModule().providePreferenceDefProvider(context.resources),
        ProviderModule().provideSharedPreferences(context)
    )

}