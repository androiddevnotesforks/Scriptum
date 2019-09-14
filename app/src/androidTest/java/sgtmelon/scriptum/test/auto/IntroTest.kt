package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()
        iPreferenceRepo.firstStart = true
    }

    override fun tearDown() {
        super.tearDown()
        iPreferenceRepo.firstStart = false
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