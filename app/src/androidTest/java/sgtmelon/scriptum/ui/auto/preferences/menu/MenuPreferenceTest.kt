package sgtmelon.scriptum.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMenuPreference

/**
 * Test for [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceTest : ParentUiTest() {

    @Test fun close() = launchMain {
        openNotes(isEmpty = true) {
            openPreferences { clickClose() }
            assert(isEmpty = true)
        }
    }

    @Test fun assertList() = launchMenuPreference { assert() }

    @Test fun assertListDeveloper() = launchMenuPreference({
        preferencesRepo.isDeveloper = true
    }) {
        assert()
    }

    @Test fun openBackup() = launchMenuPreference { openBackup() }

    @Test fun openNotes() = launchMenuPreference { openNotes() }

    @Test fun openAlarm() = launchMenuPreference { openAlarm() }

    @Test fun openPrivacyPolicy() = launchMenuPreference {
        TODO(reason = "it's not working due to select app appears")
        openPrivacyPolicy()
    }

    @Test fun openRate() = launchMenuPreference {
        TODO(reason = "it's not working due to select app appears")
        openRate()
    }

    @Test fun aboutDialogClose() = launchMenuPreference {
        openAboutDialog { softClose() }
        assert()
    }
}