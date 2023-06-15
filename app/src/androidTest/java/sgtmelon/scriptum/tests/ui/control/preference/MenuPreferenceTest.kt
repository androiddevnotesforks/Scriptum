package sgtmelon.scriptum.tests.ui.control.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference

/**
 * Test for [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceTest : ParentUiTest() {

    @Test fun openPrivacyPolicy() = launchMenuPreference { openPrivacyPolicy() }

    @Test fun openRate() = launchMenuPreference { openRate() }
}
