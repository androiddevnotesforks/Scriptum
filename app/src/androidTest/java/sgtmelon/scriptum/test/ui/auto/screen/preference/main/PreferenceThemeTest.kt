package sgtmelon.scriptum.test.ui.auto.screen.preference.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IThemeTest
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi

/**
 * Test for [PreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceThemeTest : ParentUiTest(),
    IPreferenceTest,
    IThemeTest {

    private val converter = ThemeConverter()

    @Test fun dialogClose() = runTest({ preferences.theme = converter.toInt(Theme.LIGHT) }) {
        openThemeDialog { onClickCancel() }
        assert()
        openThemeDialog { onCloseSoft() }
        assert()
    }

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(value: Theme) {
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openThemeDialog {
                onClickItem(value).onClickItem(initValue).onClickItem(value).onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.theme)
    }

    /**
     * Switch [Theme] to another one. Setup theme for application which not equals [value].
     */
    private fun switchValue(value: Theme): Theme {
        val list = Theme.values()
        var initValue: Theme

        do {
            initValue = list.random()
            preferences.theme = converter.toInt(initValue)
        } while (value == initValue)

        return initValue
    }
}