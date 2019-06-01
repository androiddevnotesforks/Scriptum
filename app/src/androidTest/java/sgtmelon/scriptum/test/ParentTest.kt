package sgtmelon.scriptum.test

import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.repository.Preference
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 *
 * @author SerjantArbuz
 */
abstract class ParentTest {

    @get:Rule val testRule = ActivityTestRule(
            SplashActivity::class.java, true, false
    )

    protected val context: Context = getInstrumentation().targetContext

    val preference = Preference(context)

    val testData = TestData(context)

    @Before @CallSuper open fun setUp() {
        preference.theme = if (Random.nextBoolean()) ThemeDef.light else ThemeDef.dark
    }

    @After @CallSuper open fun tearDown() {}

    protected fun launch(before: () -> Unit = {}, intent: Intent = Intent(), after: SplashScreen.() -> Unit) {
        before()

        testRule.launchActivity(intent)

        SplashScreen().apply(after)
    }

    protected fun wait(time: Long, func: () -> Unit = {}) = Thread.sleep(time)

    protected fun waitBefore(time: Long, func: () -> Unit = {}) {
        Thread.sleep(time)
        func()
    }

    protected fun waitAfter(time: Long, func: () -> Unit = {}) {
        func()
        Thread.sleep(time)
    }

}