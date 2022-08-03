package sgtmelon.scriptum.cleanup.test.ui.auto.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentRotationTest
import sgtmelon.scriptum.cleanup.testData.Scroll

/**
 * Test for [IntroActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class IntroRotationTest : ParentRotationTest() {

    /**
     * Was bug, when end button disappear after rotation on last page.
     */
    @Test fun endButton() = launch({ preferences.isFirstStart = true }) {
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