package sgtmelon.scriptum.test.auto

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

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun introScreenOpen() = launch({ iPreferenceRepo.firstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun bindTextNoteOpen() = testData.insertTextNote().let {
        launch(intent = context.getSplashBindIntent(it.noteEntity)) {
            openTextNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun bindRollNoteOpen() = testData.insertRollNote().let {
        launch(intent = context.getSplashBindIntent(it.noteEntity)) {
            openRollNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun alarmTextNoteOpen() = testData.insertTextNote().let {
        launch(intent = context.getSplashAlarmIntent(it.noteEntity)) { openAlarm(it) }
    }

    @Test fun alarmRollNoteOpen() = testData.insertRollNote().let {
        launch(intent = context.getSplashAlarmIntent(it.noteEntity)) { openAlarm(it) }
    }

}