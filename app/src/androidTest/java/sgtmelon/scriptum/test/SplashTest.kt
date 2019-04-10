package sgtmelon.scriptum.test

import android.content.Intent
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

    @Test fun openIntroScreen() {
        preference.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen { assert { onDisplayContent() } }
    }

    @Test fun openMainScreen() {
        preference.firstStart = false
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun openStatusItemText() {
        preference.firstStart = false

        val noteItem = testData.apply { clearAllData() }.insertText()
        testRule.launchActivity(context.getSplashIntent(noteItem))

        TextNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun openStatusItemRoll() {
        preference.firstStart = false

        val noteItem = testData.apply { clearAllData() }.insertRoll()
        testRule.launchActivity(context.getSplashIntent(noteItem))

        RollNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

}