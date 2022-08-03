package sgtmelon.scriptum.cleanup.test.ui.auto.screen.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.testData.Scroll
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl

/**
 * Test for [PreferencesImpl.isFirstStart] logic
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FirstStartTest : ParentUiTest() {

    @Before override fun setUp() = Unit

    @Test fun order0NotFinishIntro() = launch({ preferences.isFirstStart = true }) {
        introScreen()
    }

    @Test fun order1FinishIntro() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            onClickEndButton()
        }
    }

    @Test fun order2OpenAfterIntro() = launch { mainScreen() }

}