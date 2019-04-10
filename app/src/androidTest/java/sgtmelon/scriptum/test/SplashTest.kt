package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.SplashActivity
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

    @Test fun showIntroOnFirstStart() {
        preference.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen { assert { onDisplayContent() } }
    }

    @Test fun notShowIntroAfterFirstStart() {
        preference.firstStart = false
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun openTextNoteFromStatusBar() {
        preference.firstStart = false

        val noteItem = testData.apply { clearAllData() }.insertText()
        testRule.launchActivity(SplashActivity.getIntent(context, noteItem.type, noteItem.id))

        TextNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun openRollNoteFromStatusBar() {
        preference.firstStart = false

        val noteItem = testData.apply { clearAllData() }.insertRoll()
        testRule.launchActivity(SplashActivity.getIntent(context, noteItem.type, noteItem.id))

        RollNoteScreen {
            assert { onDisplayContent(State.READ) }
            pressBack()
        }
        MainScreen { assert { onDisplayContent() } }
    }

}