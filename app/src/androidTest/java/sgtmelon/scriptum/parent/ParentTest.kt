package sgtmelon.scriptum.parent

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.testData.TestData
import sgtmelon.scriptum.di.ParentInjector

/**
 * Parent class for tests
 */
abstract class ParentTest {

    protected val instrumentation = ParentInjector.provideInstrumentation()
    protected val context = ParentInjector.provideContext()
    protected val preferences = ParentInjector.providePreferences()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()

    protected val data = TestData(RoomProvider(context), preferencesRepo)

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