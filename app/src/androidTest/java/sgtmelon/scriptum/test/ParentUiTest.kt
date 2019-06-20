package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.rule.ActivityTestRule
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
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
            SplashActivity::class.java, true, false
    )

    val iPreferenceRepo = PreferenceRepo(context)

    val testData = TestData(context)

    override fun setUp() {
        super.setUp()
        iPreferenceRepo.theme = if (Random.nextBoolean()) Theme.light else Theme.dark
    }

    protected fun launch(before: () -> Unit = {}, intent: Intent = Intent(), after: SplashScreen.() -> Unit) {
        before()

        testRule.launchActivity(intent)

        SplashScreen().apply(after)
    }

    // TODO переделать before
    protected fun wait(time: Long, after: () -> Unit = {}) {
        Thread.sleep(time)
        after()
    }

    protected fun beforeWait(time: Long, before: () -> Unit = {}) {
        before()
        Thread.sleep(time)
    }

}