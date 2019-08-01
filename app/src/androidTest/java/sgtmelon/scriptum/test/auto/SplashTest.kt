package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [SplashActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun introScreenOpen() = launch({ iPreferenceRepo.firstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun bindTextNoteOpen() = testData.insertText().let {
        launch(intent = SplashActivity.getBindInstance(context, it.noteEntity)) {
            openTextNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun bindRollNoteOpen() = testData.insertRoll().let {
        launch(intent = SplashActivity.getBindInstance(context, it.noteEntity)) {
            openRollNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun alarmTextNoteOpen() = testData.insertText().let {
        launch(intent = SplashActivity.getAlarmInstance(context, it.noteEntity)) { openAlarm(it) }
    }

    @Test fun alarmRollNoteOpen() = testData.insertRoll().let {
        launch(intent = SplashActivity.getAlarmInstance(context, it.noteEntity)) { openAlarm(it) }
    }

}