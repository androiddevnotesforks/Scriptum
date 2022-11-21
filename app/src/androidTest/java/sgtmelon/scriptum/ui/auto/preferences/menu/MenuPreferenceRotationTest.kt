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
class MenuPreferenceRotationTest : ParentUiRotationTest() {

    @Test fun content() = startMenuPreferenceTest {
        rotate.toSide()
        assert()
    }

    @Test fun themeDialog() {
        val (initValue, value) = Theme.values().getDifferentValues()

        startMenuPreferenceTest({ preferencesRepo.theme = initValue }) {
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

    @Test fun aboutDialog() = startMenuPreferenceTest {
        openAboutDialog {
            rotate.toSide()
            assert()
        }
    }
}