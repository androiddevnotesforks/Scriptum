package sgtmelon.scriptum.test

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashIntent
import sgtmelon.scriptum.ui.screen.SplashScreen

/**
 * Тест для [SplashActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentTest() {

    @Test fun introScreenOpen() {
        beforeLaunch { preference.firstStart = true }

        SplashScreen { introScreen() }
    }

    @Test fun mainScreenOpen() {
        beforeLaunch { preference.firstStart = false }

        SplashScreen { mainScreen() }
    }

    @Test fun statusTextNoteOpen() {
        val noteItem = testData.apply { clearAllData() }.insertText()
        beforeLaunch(context.getSplashIntent(noteItem)) {
            preference.firstStart = false
        }

        SplashScreen {
            textNoteScreen(State.READ) { pressBack() }
            mainScreen()
        }
    }

    @Test fun statusRollNoteOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()
        beforeLaunch(context.getSplashIntent(noteItem)) {
            preference.firstStart = false
        }

        SplashScreen {
            rollNoteScreen(State.READ) { pressBack() }
            mainScreen()
        }
    }

}