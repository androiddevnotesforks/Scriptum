package sgtmelon.scriptum.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.infrastructure.converter.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.test.parent.ParentRotationTest
import sgtmelon.scriptum.test.ui.auto.screen.preference.main.IPreferenceTest

/**
 * Test of [PreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRotationTest : ParentRotationTest(), IPreferenceTest {

    @Test fun content() = runTest {
        automator.rotateSide()
        assert()
    }

    @Test fun themeDialog() {
        val initValue = Theme.values().random()
        val value = getThemeClick(initValue)

        assertNotEquals(initValue, value)

        runTest({ preferences.theme = ThemeConverter().toInt(initValue) }) {
            openThemeDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.theme)
    }

    private fun getThemeClick(initValue: Theme): Theme {
        val newValue = Theme.values().random()
        return if (newValue == initValue) getThemeClick(initValue) else newValue
    }

    @Test fun aboutDialog() = runTest({ preferences.isDeveloper = false }) {
        val clickTimes = (1..3).random()

        openAboutDialog {
            repeat(clickTimes) { clickLogo() }
            automator.rotateSide()
            assertEquals(clickTimes, clickCount)
            unlockDeveloper()
        }
        openDeveloper()
    }
}