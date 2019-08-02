package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Родительский класс для UI тестов
 *
 * @author SerjantArbuz
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
            SplashActivity::class.java, true, false
    )

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.apply {
            theme = if (Random.nextBoolean()) Theme.LIGHT else Theme.DARK
            firstStart = false

            autoSaveOn = false
            pauseSaveOn = false
        }

        testData.clear()
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

}