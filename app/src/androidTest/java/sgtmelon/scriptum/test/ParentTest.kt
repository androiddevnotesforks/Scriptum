package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.dagger.module.base.ProviderModule
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Parent class for tests
 */
abstract class ParentTest {

    val context: Context = getInstrumentation().targetContext

    val preferenceRepo: IPreferenceRepo = PreferenceRepo(
            ProviderModule().providePreferenceKeyProvider(context.resources),
            ProviderModule().providePreferenceDefProvider(context.resources),
            ProviderModule().provideSharedPreferences(context)
    )

    val data = TestData(RoomProvider(context), preferenceRepo)

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