package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest

/**
 * Test for [HelpPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class HelpPreferenceTest : ParentUiTest(), IHelpPreferenceTest {

    @Test fun close() = runTest { clickClose() }

    @Test fun assert() = runTest { assert() }

    @Test fun openNotificationDisappear() = runTest { openNotificationDisappear() }

    @Test fun openPrivacyPolicy() = runTest { openPrivacyPolicy() }
}