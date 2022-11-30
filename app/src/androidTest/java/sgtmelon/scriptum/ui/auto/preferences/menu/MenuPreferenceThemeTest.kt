package sgtmelon.scriptum.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.ThemeDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.parent.ui.tests.launchMenuPreference
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.value.ThemeCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [MenuPreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceThemeTest : ParentUiTest(),
    ThemeCase,
    DialogCloseCase {

    @Test override fun close() = launchMenuPreference({
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
        val (setValue, initValue) = Theme.values().getDifferentValues(value)
        preferencesRepo.theme = initValue

        /** It's necessary check theme change from application start. */
        launchMain {
            openNotes(isEmpty = true) {
                openPreferences {
                    openThemeDialog {
                        click(setValue)
                        click(initValue)
                        click(setValue)
                        apply()
                    }
                    assert()
                    clickClose()
                }
                assert(isEmpty = true)
            }
            assert(isFabVisible = true)
        }

        assertEquals(setValue, preferencesRepo.theme)
    }
}