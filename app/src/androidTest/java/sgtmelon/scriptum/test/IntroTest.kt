package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.ui.screen.IntroScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [IntroActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        beforeLaunch { preference.firstStart = true }
    }

    @Test fun contentPlacement() {
        IntroScreen { passThrough(Scroll.END) }
    }

    @Test fun endButtonEnable() {
        IntroScreen {
            passThrough(Scroll.END)
            assert { onDisplayEndButton() }

            passThrough(Scroll.START)

            passThrough(Scroll.END)
            assert { onDisplayEndButton() }
        }
    }

    @Test fun endButtonWork() {
        IntroScreen {
            repeat(times = count - 1) { onSwipe(Scroll.END) }

            assert { onDisplayEndButton() }
            onClickEndButton()
        }

        MainScreen { assert { onDisplayContent() } }
    }

}