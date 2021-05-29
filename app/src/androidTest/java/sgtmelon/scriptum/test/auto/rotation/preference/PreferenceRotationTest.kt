package sgtmelon.scriptum.test.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.auto.screen.preference.main.IPreferenceTest
import sgtmelon.scriptum.test.parent.ParentRotationTest

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
        val initValue = Theme.list.random()

        runTest({ preferenceRepo.theme = initValue }) {
            openThemeDialog {
                onClickItem(getThemeClick(initValue))
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }
    }

    @Theme private fun getThemeClick(@Theme initValue: Int): Int {
        val newValue = Theme.list.random()
        return if (newValue == initValue) getThemeClick(initValue) else newValue
    }

    @Test fun aboutDialog() = runTest({ preferenceRepo.isDeveloper = false }) {
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