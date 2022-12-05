package sgtmelon.scriptum.parent.di

import android.app.Instrumentation
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import sgtmelon.scriptum.cleanup.dagger.module.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.data.RepositoryModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.RoomModule
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.testData.DbWeightDelegator
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.database.Database
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

    fun provideDbDelegator(): DbDelegator {
        return DbDelegator(provideDatabase(), providePreferencesRepo())
    }

    fun provideDbWeightDelegator(): DbWeightDelegator {
        return DbWeightDelegator(provideContext(), provideDatabase())
    }

    fun provideDatabase(): Database {
        return RoomModule().provideDatabase(provideContext())
    }

    fun provideUiDevice(): UiDevice = UiDevice.getInstance(provideInstrumentation())
}