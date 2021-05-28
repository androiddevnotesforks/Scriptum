package sgtmelon.scriptum.ui.logic.parent

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import sgtmelon.scriptum.dagger.module.base.ProviderModule
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Parent class for UI tests simple logic.
 */
abstract class ParentLogic {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val preferenceRepo: IPreferenceRepo = PreferenceRepo(
        ProviderModule().providePreferenceKeyProvider(context.resources),
        ProviderModule().providePreferenceDefProvider(context.resources),
        ProviderModule().provideSharedPreferences(context)
    )

}