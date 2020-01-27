package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Parent class for tests
 */
abstract class ParentTest {

    val context: Context = getInstrumentation().targetContext

    val preferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    val data = TestData(context, preferenceRepo)

    @Before @CallSuper open fun setUp() {}

    @After @CallSuper open fun tearDown() {}

    protected companion object {
        const val DATE_1 = "1234-01-02 03:04:05"
        const val DATE_2 = "1345-02-03 04:05:06"
        const val DATE_3 = "1456-03-04 05:06:07"
        const val DATE_4 = "1567-04-05 06:07:08"
        const val DATE_5 = "1998-08-25 07:08:09"
    }

}