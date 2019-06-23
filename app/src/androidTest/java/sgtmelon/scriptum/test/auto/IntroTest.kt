package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [IntroActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = true
        testData.clear()
    }

    @Test fun contentPlacement() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            onPassThrough(Scroll.START)
            onPassThrough(Scroll.END)
        }
    }

    @Test fun endButtonWork() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            onClickEndButton()
        }
    }

}