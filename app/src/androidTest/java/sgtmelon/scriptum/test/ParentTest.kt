package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before

/**
 * Родительский класс для UI/Integration тестов
 *
 * @author SerjantArbuz
 */
abstract class ParentTest {

    val context: Context = getInstrumentation().targetContext

    @Before @CallSuper open fun setUp() {}

    @After @CallSuper open fun tearDown() {}

}