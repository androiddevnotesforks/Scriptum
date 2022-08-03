package sgtmelon.scriptum.cleanup.test.ui.auto.screen.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.testData.Scroll

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentUiTest() {

    @Before override fun setUp() {
        super.setUp()
        preferences.isFirstStart = true
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