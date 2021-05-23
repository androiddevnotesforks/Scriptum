package sgtmelon.scriptum.test.auto.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.ParentUiTest
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

    @Test fun themeDialogWork() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog { onClickItem(Theme.DARK).onClickApply() }
        assert()
        openThemeDialog { onClickItem(Theme.SYSTEM).onClickApply() }
        assert()
        openThemeDialog { onClickItem(Theme.LIGHT).onClickApply() }
    }

    @Test fun themeDialogClose() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog { onClickCancel() }
        assert()
        openThemeDialog { onCloseSoft() }
        assert()
    }

    @Test fun openBackup() = runTest { openBackup() }

    @Test fun openNote() = runTest { openNote() }

    @Test fun openNotification() = runTest { openNotification() }

    @Test fun openRate() = runTest { openRate() }

    @Test fun openHelp() = runTest { openHelp() }

    @Test fun aboutDialogWork() = runTest {
        TODO()
    }

    @Test fun aboutDialogClose() = runTest {
        TODO()
    }

    @Test fun openDeveloper() = runTest({ preferenceRepo.isDeveloper = true }) { openDeveloper() }

}