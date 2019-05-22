package sgtmelon.scriptum.test.auto

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashIntent
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест для [SplashActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentTest() {

    @Test fun introScreenOpen() = launch({ preference.firstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch({ preference.firstStart = false }) { mainScreen() }

    @Test fun statusTextNoteOpen() {
        preference.firstStart = false

        launch(intent = context.getSplashIntent(testData.clear().insertText())) {
            openTextNoteNotification { pressBack() }
            mainScreen()
        }
    }

    @Test fun statusRollNoteOpen() {
        preference.firstStart = false

        launch(intent = context.getSplashIntent(testData.clear().insertRoll())) {
            openRollNoteNotification { pressBack() }
            mainScreen()
        }
    }

}