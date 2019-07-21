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

    val testData = TestData(context, iPreferenceRepo)

    @Before @CallSuper open fun setUp() {}

    @After @CallSuper open fun tearDown() {}

}