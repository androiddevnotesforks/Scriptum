package sgtmelon.scriptum.tests.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference

/**
 * Test of [MenuPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceRotationTest : ParentUiRotationTest() {

    @Test fun content() = launchMenuPreference {
        rotate.switch()
        assert()
    }

    @Test fun themeDialog() {
        val initValue = Theme.DARK
        val value = Theme.LIGHT
        launchMenuPreference({ preferencesRepo.theme = initValue }) {
            openThemeDialog {
                click(value)
                rotate.switch()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.theme)
    }

    @Test fun aboutDialog() = launchMenuPreference {
        openAboutDialog {
            rotate.switch()
            assert()
        }
    }
}