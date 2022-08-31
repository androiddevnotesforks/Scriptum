package sgtmelon.scriptum.cleanup.ui.logic.parent

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.RepositoryModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.PreferencesModule
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Parent class for UI tests simple logic.
 */
abstract class ParentLogic {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val preferences: Preferences = PreferencesModule().providePreferences(
        PreferencesModule().providePreferenceKeyProvider(context.resources),
        PreferencesModule().providePreferenceDefProvider(context.resources),
        PreferencesModule().provideSharedPreferences(context)
    )

    /**
     * It's needed for get enum values (already converted from [preferences]).
     */
    val preferencesRepo: PreferencesRepo = RepositoryModule().providePreferencesRepo(
        DataSourceModule().providePreferencesDataSource(preferences),
        ConverterModule().provideThemeConverter(),
        ConverterModule().provideSortConverter(),
        ConverterModule().provideColorConverter(),
        ConverterModule().provideSavePeriodConverter(),
        ConverterModule().provideRepeatConverter(),
        ConverterModule().provideSignalConverter()
    )
}