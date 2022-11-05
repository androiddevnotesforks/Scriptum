package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help.IHelpDisappearTest
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest

/**
 * Test of [HelpDisappearActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class HelpDisappearRotationTest : ParentUiRotationTest(), IHelpDisappearTest {

    @Test fun content() = runTest {
        rotate.toSide()
        assert()
    }
}