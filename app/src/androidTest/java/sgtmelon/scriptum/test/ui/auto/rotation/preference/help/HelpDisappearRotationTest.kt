package sgtmelon.scriptum.test.ui.auto.rotation.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.test.ui.auto.screen.preference.help.IHelpDisappearTest
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test of [HelpDisappearActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class HelpDisappearRotationTest : ParentRotationTest(), IHelpDisappearTest {

    @Test fun content() = runTest {
        automator.rotateSide()
        assert()
    }
}