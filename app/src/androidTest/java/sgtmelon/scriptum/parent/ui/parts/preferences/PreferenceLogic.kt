package sgtmelon.scriptum.parent.ui.parts.preferences

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import sgtmelon.scriptum.cleanup.dagger.module.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.data.RepositoryModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.PreferencesModule
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.system.dataSource.SummaryDataSourceImpl

/**
 * Parent class for preference screen/test logic.
 */
abstract class PreferenceLogic() {


    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val preferences: Preferences = PreferencesModule().providePreferences(
        PreferencesModule().providePreferenceKeyProvider(context.resources),
        PreferencesModule().providePreferenceDefProvider(context.resources),
        PreferencesModule().provideSharedPreferences(context)
    )

    protected val preferencesRepo: PreferencesRepo = RepositoryModule().providePreferencesRepo(
        DataSourceModule().providePreferencesDataSource(preferences),
        ConverterModule().provideThemeConverter(),
        ConverterModule().provideSortConverter(),
        ConverterModule().provideColorConverter(),
        ConverterModule().provideSavePeriodConverter(),
        ConverterModule().provideRepeatConverter(),
        ConverterModule().provideSignalConverter()
    )

    protected val summary = SummaryDataSourceImpl(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}