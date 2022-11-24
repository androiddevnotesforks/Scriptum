package sgtmelon.scriptum.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.ThemeDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.DialogCloseCase
import sgtmelon.scriptum.ui.cases.value.ThemeCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [MenuPreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceThemeTest : ParentUiTest(),
    ThemeCase,
    DialogCloseCase {

    @Test override fun close() = startMenuPreferenceTest({
        preferencesRepo.theme = Theme.LIGHT
    }) {
        openThemeDialog { cancel() }
        assert()
        openThemeDialog { softClose() }
        assert()
    }

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(value: Theme) {
        TODO("BUG: After switch theme app will be restarted")

        val (setValue, initValue) = Theme.values().getDifferentValues(value)
        preferencesRepo.theme = initValue

        startMenuPreferenceTest {
            openThemeDialog {
                click(setValue)
                click(initValue)
                click(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.theme)
    }
}