package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help.IHelpPreferenceTest
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [HelpPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class HelpPreferenceRotationTest : ParentUiRotationTest(), IHelpPreferenceTest {

    @Test fun content() = runTest {
        automator.rotateSide()
        assert()
    }
}