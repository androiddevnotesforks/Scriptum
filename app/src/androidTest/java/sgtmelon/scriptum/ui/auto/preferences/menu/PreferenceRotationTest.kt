package sgtmelon.scriptum.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.test.common.getDifferentValues

/**
 * Test of [MenuPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRotationTest : ParentUiRotationTest(), IPreferenceTest {

    @Test fun content() = runTest {
        rotate.toSide()
        assert()
    }

    @Test fun themeDialog() {
        val (initValue, value) = Theme.values().getDifferentValues()

        runTest({ preferencesRepo.theme = initValue }) {
            openThemeDialog {
                onClickItem(value)
                rotate.toSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.theme)
    }

    @Test fun aboutDialog() = runTest {
        openAboutDialog {
            rotate.toSide()
            assert()
        }
    }
}