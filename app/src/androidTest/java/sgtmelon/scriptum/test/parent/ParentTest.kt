package sgtmelon.scriptum.test.parent

import android.app.Instrumentation
import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.dagger.module.base.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.base.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.RepositoryModule
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Parent class for tests
 */
abstract class ParentTest {

    protected val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    protected val context: Context = instrumentation.targetContext

    // TODO make common injection
    protected val preferences: Preferences = PreferencesModule().providePreferences(
        PreferencesModule().providePreferenceKeyProvider(context.resources),
        PreferencesModule().providePreferenceDefProvider(context.resources),
        PreferencesModule().provideSharedPreferences(context)
    )

    /**
     * It's needed for get enum values (already converted from [preferences]).
     */
    // TODO make common injection
    protected val preferencesRepo: PreferencesRepo = RepositoryModule().providePreferencesRepo(
        DataSourceModule().providePreferencesDataSource(preferences),
        ConverterModule().provideThemeConverter(),
        ConverterModule().provideSortConverter(),
        ConverterModule().provideColorConverter(),
        ConverterModule().provideSavePeriodConverter(),
        ConverterModule().provideRepeatConverter(),
        ConverterModule().provideSignalConverter()
    )

    protected val data = TestData(RoomProvider(context), preferences)

    protected val dateList = listOf(DATE_1, DATE_2, DATE_3, DATE_4, DATE_5)

    @Before @CallSuper open fun setUp() = Unit

    @After @CallSuper open fun tearDown() = Unit

    protected companion object {
        const val DATE_1 = "1234-01-02 03:04:05"
        const val DATE_2 = "1345-02-03 04:05:06"
        const val DATE_3 = "1456-03-04 05:06:07"
        const val DATE_4 = "1567-04-05 06:07:08"
        const val DATE_5 = "1998-08-25 07:08:09"
    }
}