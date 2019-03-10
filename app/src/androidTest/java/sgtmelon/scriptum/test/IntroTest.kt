package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.screen.intro.IntroActivity
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.screen.IntroScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = true

        testRule.launchActivity(Intent())
    }

    @Test fun testContent() {
        IntroScreen {
            for (position in 0 until count - 1) {
                assert { onDisplayContent(position) }
                onSwipe(Scroll.END)
            }
        }
    }

    @Test fun testEndButton() {
        IntroScreen {
            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }

            for (position in count - 1 downTo 0) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.START)
            }

            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }

            onClickEndButton()
        }

        MainScreen {
            assert {
                onDisplayContent()
                onDisplayContent(MainPage.Name.NOTES)
            }
        }
    }

}