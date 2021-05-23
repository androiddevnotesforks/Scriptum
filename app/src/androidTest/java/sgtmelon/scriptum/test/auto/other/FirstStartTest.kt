package sgtmelon.scriptum.test.auto.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [PreferenceRepo.firstStart] logic
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FirstStartTest : ParentUiTest() {

    @Before override fun setup() = Unit

    @Test fun order0_notFinishIntro() = launch({ preferenceRepo.firstStart = true }) {
        introScreen()
    }

    @Test fun order1_finishIntro() = launch {
        introScreen {
            onPassThrough(Scroll.END)
            onClickEndButton()
        }
    }

    @Test fun order2_openAfterIntro() = launch { mainScreen() }

}