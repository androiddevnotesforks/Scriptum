package sgtmelon.scriptum.tests.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.screen.CloseScreenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference

/**
 * Test for [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceTest : ParentUiTest(),
    CloseScreenCase {

    @Test override fun closeScreen() = launchMain {
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

    @Test fun aboutDialogClose() = launchMenuPreference {
        openAboutDialog { softClose() }
        assert()
    }
}