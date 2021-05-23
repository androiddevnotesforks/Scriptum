package sgtmelon.scriptum.test.auto.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi
import sgtmelon.scriptum.ui.screen.preference.PreferenceScreen

/**
 * Test for [PreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceThemeTest : ParentUiTest() {

    private fun runTest(before: () -> Unit = {}, func: PreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreference(func) } } }
    }

    @Test fun themeSelectLight() = startThemeSelect(Theme.LIGHT)

    @Test fun themeSelectDark() = startThemeSelect(Theme.DARK)

    @Test fun themeSelectSystem() = startThemeSelect(Theme.SYSTEM)

    private fun startThemeSelect(@Theme theme: Int) = runTest({ turnTheme(theme) }) {
        openThemeDialog { onClickItem(theme).onClickApply() }
        assert()
    }

    /**
     * Switch [Theme] to another one.
     */
    private fun turnTheme(@Theme theme: Int) {
        val list = Theme.list

        while (preferenceRepo.theme == theme) {
            preferenceRepo.theme = list.random()
        }
    }

    @Test fun themeDialogClose() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog { onClickCancel() }
        assert()
        openThemeDialog { onCloseSoft() }
        assert()
    }

}