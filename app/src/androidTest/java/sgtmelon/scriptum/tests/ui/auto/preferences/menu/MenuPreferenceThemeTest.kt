package sgtmelon.scriptum.tests.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.value.ThemeCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.ThemeDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.menu.MenuPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [MenuPreferenceFragment] and [ThemeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceThemeTest : ParentUiRotationTest(),
    DialogCloseCase,
    ThemeCase,
    DialogRotateCase {

    @Test override fun close() = launchMenuPreference {
        openThemeDialog { softClose() }
        assert()
        openThemeDialog { cancel() }
        assert()
    }

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(value: Theme) = runWorkTest(value) { click(it) }

    @Test override fun rotateClose() = launchMenuPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun MenuPreferenceScreen.assertRotationClose(closeDialog: ThemeDialogUi.() -> Unit) {
        openThemeDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun ThemeDialogUi.assertRotationClick(value: Theme) {
        click(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(value: Theme? = null, action: ThemeDialogUi.(value: Theme) -> Unit) {
        val (setValue, initValue) = Theme.values().getDifferentValues(value)
        preferencesRepo.theme = initValue

        /**
         * It's necessary check theme change from application start - the reason why should
         * use [launchMain].
         */
        launchMain {
            openNotes(isEmpty = true) {
                openPreferences {
                    openThemeDialog {
                        action(setValue)
                        action(initValue)
                        action(setValue)
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