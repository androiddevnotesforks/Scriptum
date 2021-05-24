package sgtmelon.scriptum.test.auto.screen.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.PreferenceScreen

/**
 * Test for [PreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceTest : ParentUiTest() {

    private fun runTest(before: () -> Unit = {}, func: PreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreference(func) } } }
    }

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertAllNotDeveloper() = runTest({ preferenceRepo.isDeveloper = false }) { assert() }

    @Test fun assertAllDeveloper() = runTest({ preferenceRepo.isDeveloper = true }) { assert() }

    @Test fun openBackup() = runTest { openBackup() }

    @Test fun openNote() = runTest { openNote() }

    @Test fun openAlarm() = runTest { openAlarm() }

    @Test fun openRate() = runTest { openRate() }

    @Test fun openHelp() = runTest { openHelp() }

    @Test fun aboutDialogWork() = runTest({ preferenceRepo.isDeveloper = false }) {
        openAboutDialog { unlockDeveloper() }
        openDeveloper { onClickClose() }
        openAboutDialog { unlockDeveloper() }
    }

    @Test fun aboutDialogClose() = runTest {
        openAboutDialog { onCloseSoft() }
        assert()
    }

    @Test fun openDeveloper() = runTest({ preferenceRepo.isDeveloper = true }) { openDeveloper() }

}