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
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.PreferenceRepo
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

    val preference = PreferenceRepo(context)

    val testData = TestData(context)

    @Before @CallSuper open fun setUp() {
        preference.theme = if (Random.nextBoolean()) Theme.light else Theme.dark
    }

    @After @CallSuper open fun tearDown() {}

    protected fun launch(before: () -> Unit = {}, intent: Intent = Intent(), after: SplashScreen.() -> Unit) {
        before()

        testRule.launchActivity(intent)

        SplashScreen().apply(after)
    }

    // TODO переделать в одну функцию

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