package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest

/**
 * Test for [HelpDisappearActivity].
 */
@RunWith(AndroidJUnit4::class)
class HelpDisappearTest : ParentUiTest(), IHelpDisappearTest {

    @Test fun close() = runTest { clickClose() }

    @Test fun assert() = runTest { assert() }

    @Test fun openVideo() = runTest { openVideo() }

    // TODO it's not start next test correctly
    @Test fun openSettings() = runTest { openSettings() }
}