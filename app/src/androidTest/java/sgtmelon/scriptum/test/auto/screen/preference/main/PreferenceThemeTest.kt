package sgtmelon.scriptum.test.auto.screen.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IThemeTest
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi
import sgtmelon.scriptum.ui.screen.preference.PreferenceScreen

/**
 * Test for [PreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceThemeTest : ParentUiTest(), IThemeTest {

    private fun runTest(before: () -> Unit = {}, func: PreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreference(func) } } }
    }

    @Test fun dialogClose() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog { onClickCancel() }
        assert()
        openThemeDialog { onCloseSoft() }
        assert()
    }

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(@Theme theme: Int) = runTest({ switchValue(theme) }) {
        openThemeDialog { onClickItem(theme).onClickApply() }
        assert()
    }

    /**
     * Switch [Theme] to another one.
     */
    private fun switchValue(@Theme theme: Int) {
        val list = Theme.list

        while (preferenceRepo.theme == theme) {
            preferenceRepo.theme = list.random()
        }
    }
}