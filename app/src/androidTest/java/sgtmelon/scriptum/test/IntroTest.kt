package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.screen.intro.IntroActivity
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.screen.IntroScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    @Test fun showActivityOnFirstStart() {
        prefUtils.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen { assert { onDisplayContent() } }
    }

    @Test fun notShowActivityAfterFirstStart() {
        prefUtils.firstStart = false
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun rightContentPlacement() {
        prefUtils.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen {
            for (position in 0 until count - 1) {
                assert { onDisplayContent(position) }
                onSwipe(Scroll.END)
            }
        }
    }

    @Test fun endButtonEnable() {
        prefUtils.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen {
            for (p in 0 until count - 1) {
                assert { isEnableEndButton(p) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }

            for (p in count - 1 downTo 0) {
                assert { isEnableEndButton(p) }
                onSwipe(Scroll.START)
            }

            for (p in 0 until count - 1) {
                assert { isEnableEndButton(p) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }
        }
    }

    @Test fun endButtonWork() {
        prefUtils.firstStart = true
        testRule.launchActivity(Intent())

        IntroScreen {
            (0 until count - 1).forEach { _ -> onSwipe(Scroll.END) }

            assert { onDisplayEndButton() }
            onClickEndButton()
        }

        MainScreen { assert { onDisplayContent() } }
    }

}