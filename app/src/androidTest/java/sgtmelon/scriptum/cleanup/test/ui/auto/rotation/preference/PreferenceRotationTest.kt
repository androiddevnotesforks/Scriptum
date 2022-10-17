package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.extension.getDifferentValues
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.main.IPreferenceTest
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [PreferenceFragment] work with phone rotation.
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