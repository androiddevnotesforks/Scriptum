package sgtmelon.scriptum.di

import android.app.Instrumentation
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import sgtmelon.scriptum.cleanup.dagger.module.base.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.base.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.RepositoryModule
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Injector for clear code and simple providing needed classes.
 */
object ParentInjector {

    fun provideInstrumentation(): Instrumentation = InstrumentationRegistry.getInstrumentation()

    fun provideContext(): Context = provideInstrumentation().targetContext

    fun providePreferences(): Preferences {
        val context = provideContext()

        return PreferencesModule().providePreferences(
            PreferencesModule().providePreferenceKeyProvider(context.resources),
            PreferencesModule().providePreferenceDefProvider(context.resources),
            PreferencesModule().provideSharedPreferences(context)
        )
    }

    fun providePreferencesRepo(): PreferencesRepo {
        return RepositoryModule().providePreferencesRepo(
            DataSourceModule().providePreferencesDataSource(providePreferences()),
            ConverterModule().provideThemeConverter(),
            ConverterModule().provideSortConverter(),
            ConverterModule().provideColorConverter(),
            ConverterModule().provideSavePeriodConverter(),
            ConverterModule().provideRepeatConverter(),
            ConverterModule().provideSignalConverter()
        )
    }
}