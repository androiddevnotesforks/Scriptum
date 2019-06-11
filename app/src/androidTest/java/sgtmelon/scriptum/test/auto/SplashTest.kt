package sgtmelon.scriptum.test.auto

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashBindIntent
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

    @Test fun bindTextNoteOpen() {
        preference.firstStart = false

        launch(intent = context.getSplashBindIntent(testData.clear().insertText())) {
            openTextNoteBind { pressBack() }
            mainScreen()
        }
    }

    @Test fun bindRollNoteOpen() {
        preference.firstStart = false

        launch(intent = context.getSplashBindIntent(testData.clear().insertRoll())) {
            openRollNoteBind { pressBack() }
            mainScreen()
        }
    }

    @Test fun alarmTextNoteOpen() {
        preference.firstStart = false

        val noteItem = testData.clear().insertText()
        launch(intent = context.getSplashAlarmIntent(noteItem)) {
            openAlarm(noteItem)
        }
    }

    @Test fun alarmRollNoteOpen() {
        preference.firstStart = false

        val noteItem = testData.clear().insertRoll()
        launch(intent = context.getSplashAlarmIntent(noteItem)) {
            openAlarm(noteItem)
        }
    }

}