package sgtmelon.scriptum.test.auto.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentUiTest() {

    @Before override fun setup() {
        super.setup()
        preferenceRepo.firstStart = true
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