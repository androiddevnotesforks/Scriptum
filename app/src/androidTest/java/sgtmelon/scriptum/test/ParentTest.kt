package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Родительский класс для UI/Integration тестов
 *
 * @author SerjantArbuz
 */
abstract class ParentTest {

    val context: Context = getInstrumentation().targetContext

    val iPreferenceRepo = PreferenceRepo.getInstance(context)

    val data = TestData(context, iPreferenceRepo)

    @Before @CallSuper open fun setUp() {}

    @After @CallSuper open fun tearDown() {}

    protected companion object {
        const val DATE_0 = "1998-08-25 02:03:04"
        const val DATE_1 = "1234-01-02 03:04:05"
        const val DATE_2 = "1345-02-03 04:05:06"
        const val DATE_3 = "1456-03-04 05:06:07"
        const val DATE_4 = "1567-04-05 06:07:08"
    }

}