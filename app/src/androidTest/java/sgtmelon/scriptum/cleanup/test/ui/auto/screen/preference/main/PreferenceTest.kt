package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test for [PreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceTest : ParentUiTest(), IPreferenceTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertAllNotDeveloper() = runTest({ preferences.isDeveloper = false }) { assert() }

    @Test fun assertAllDeveloper() = runTest({ preferences.isDeveloper = true }) { assert() }

    @Test fun openBackup() = runTest { openBackup() }

    @Test fun openNote() = runTest { openNote() }

    @Test fun openAlarm() = runTest { openAlarm() }

    @Test fun openRate() = runTest { openRate() }

    @Test fun openHelp() = runTest { openHelp() }

    @Test fun aboutDialogWork() = runTest({ preferences.isDeveloper = false }) {
        openAboutDialog { unlockDeveloper() }
        openDeveloper { onClickClose() }
        openAboutDialog { unlockDeveloper() }
    }

    @Test fun aboutDialogClose() = runTest {
        openAboutDialog { onCloseSoft() }
        assert()
    }

    @Test fun openDeveloper() = runTest({ preferences.isDeveloper = true }) { openDeveloper() }

}