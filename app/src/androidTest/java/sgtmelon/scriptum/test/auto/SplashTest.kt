package sgtmelon.scriptum.test.auto

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashBindIntent
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [SplashActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun introScreenOpen() = launch({ iPreferenceRepo.firstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch({ iPreferenceRepo.firstStart = false }) { mainScreen() }

    @Test fun bindTextNoteOpen() {
        iPreferenceRepo.firstStart = false
        val noteEntity = testData.clear().insertText()

        launch(intent = context.getSplashBindIntent(noteEntity)) {
            openTextNoteBind(noteEntity) { pressBack() }
            mainScreen()
        }
    }

    @Test fun bindRollNoteOpen() {
        iPreferenceRepo.firstStart = false
        val noteEntity = testData.clear().insertRoll()

        launch(intent = context.getSplashBindIntent(noteEntity)) {
            openRollNoteBind(noteEntity) { pressBack() }
            mainScreen()
        }
    }

    @Test fun alarmTextNoteOpen() {
        iPreferenceRepo.firstStart = false

        val noteEntity = testData.clear().insertText()
        launch(intent = context.getSplashAlarmIntent(noteEntity)) {
            openAlarm(noteEntity)
        }
    }

    @Test fun alarmRollNoteOpen() {
        iPreferenceRepo.firstStart = false

        val noteEntity = testData.clear().insertRoll()
        launch(intent = context.getSplashAlarmIntent(noteEntity)) {
            openAlarm(noteEntity)
        }
    }

}