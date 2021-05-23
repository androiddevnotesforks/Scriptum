package sgtmelon.scriptum.test.auto.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test for [IntroActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class IntroRotationTest : ParentRotationTest() {

    /**
     * Was bug, when end button disappear after rotation on last page.
     */
    @Test fun endButton() = launch({ preferenceRepo.firstStart = true }) {
        introScreen {
            onPassThrough(Scroll.END)
            automator.rotateSide()
            onSwipe(Scroll.START)
            onSwipe(Scroll.END)
            automator.rotateNatural()
            onClickEndButton()
        }
    }
}