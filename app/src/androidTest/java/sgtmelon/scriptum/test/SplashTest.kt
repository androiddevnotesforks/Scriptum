package sgtmelon.scriptum.test

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashIntent
import sgtmelon.scriptum.ui.screen.IntroScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Тест для [SplashActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentTest() {

    @Test fun introScreenOpen() {
        beforeLaunch { preference.firstStart = true }

        IntroScreen { assert { onDisplayContent() } }
    }

    @Test fun mainScreenOpen() {
        beforeLaunch { preference.firstStart = false }

        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun statusTextNoteOpen() {
        val noteItem = testData.apply { clearAllData() }.insertText()
        beforeLaunch(context.getSplashIntent(noteItem)) {
            preference.firstStart = false
        }

        TextNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun statusRollNoteOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()
        beforeLaunch(context.getSplashIntent(noteItem)) {
            preference.firstStart = false
        }

        RollNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

}