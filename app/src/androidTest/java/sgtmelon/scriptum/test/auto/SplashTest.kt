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

    @Test fun bindTextNoteOpen() = data.insertText().let {
        launch(intent = SplashActivity.getBindInstance(context, it.noteEntity)) {
            openTextNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun bindRollNoteOpen() = data.insertRoll().let {
        launch(intent = SplashActivity.getBindInstance(context, it.noteEntity)) {
            openRollNoteBind(it) { onPressBack() }
            mainScreen()
        }
    }

    @Test fun alarmTextNoteOpen() = data.insertText().let {
        launch(intent = SplashActivity.getAlarmInstance(context, it.noteEntity)) { openAlarm(it) }
    }

    @Test fun alarmRollNoteOpen() = data.insertRoll().let {
        launch(intent = SplashActivity.getAlarmInstance(context, it.noteEntity)) { openAlarm(it) }
    }

}