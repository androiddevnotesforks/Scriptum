package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест для [IntroActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.setFirstStart(true)
        testData.clear()
    }

    @Test fun contentPlacement() = launch { introScreen { onPassThrough(Scroll.END) } }

    @Test fun endButtonEnabled() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            assert { onDisplayEndButton() }

            onPassThrough(Scroll.START)

            onPassThrough(Scroll.END)
            assert { onDisplayEndButton() }
        }
    }

    @Test fun endButtonWork() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            assert { onDisplayEndButton() }
            onClickEndButton()
        }

        mainScreen()
    }

}