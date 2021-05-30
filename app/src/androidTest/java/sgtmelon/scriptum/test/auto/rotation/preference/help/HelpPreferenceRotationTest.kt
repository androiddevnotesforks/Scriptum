package sgtmelon.scriptum.test.auto.rotation.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.test.auto.screen.preference.help.IHelpPreferenceTest
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test of [HelpPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class HelpPreferenceRotationTest : ParentRotationTest(), IHelpPreferenceTest {

    @Test fun content() = runTest {
        automator.rotateSide()
        assert()
    }
}