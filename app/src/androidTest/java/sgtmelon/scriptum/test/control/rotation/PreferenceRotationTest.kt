package sgtmelon.scriptum.test.control.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [PreferenceFragment] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRotationTest : ParentRotationTest() {

    @Test fun content() = launch {
        mainScreen { notesScreen(empty = true) { openPreference { onRotate { assert() } } } }
    }

    @Test fun colorDialog() = launch {
        val color = preferenceRepo.defaultColor
        mainScreen {
            notesScreen(empty = true) {
                openPreference { openColorDialog(color) { onRotate { assert() } } }
            }
        }
    }

}