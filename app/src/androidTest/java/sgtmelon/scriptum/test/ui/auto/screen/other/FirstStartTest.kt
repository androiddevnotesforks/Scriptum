package sgtmelon.scriptum.test.ui.auto.screen.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [PreferencesImpl.isFirstStart] logic
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FirstStartTest : ParentUiTest() {

    @Before override fun setup() = Unit

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