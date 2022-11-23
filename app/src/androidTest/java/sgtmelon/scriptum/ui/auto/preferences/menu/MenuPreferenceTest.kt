package sgtmelon.scriptum.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceTest : ParentUiTest() {

    // TODO hide scriptum service notification via adb?

    @Test fun close() = startMenuPreferenceTest { clickClose() }

    @Test fun assertList() = startMenuPreferenceTest { assert() }

    @Test fun assertListDeveloper() = startMenuPreferenceTest({ preferences.isDeveloper = true }) {
        assert()
    }

    @Test fun openBackup() = startMenuPreferenceTest { openBackup() }

    @Test fun openNote() = startMenuPreferenceTest { openNote() }

    @Test fun openAlarm() = startMenuPreferenceTest { openAlarm() }

    @Test fun openPrivacyPolicy() = startMenuPreferenceTest { openPrivacyPolicy() }

    @Test fun openRate() = startMenuPreferenceTest {
        TODO(reason = "it's not working due to select app appears")
        openRate()
    }

    @Test fun aboutDialogClose() = startMenuPreferenceTest {
        openAboutDialog { softClose() }
        assert()
    }
}