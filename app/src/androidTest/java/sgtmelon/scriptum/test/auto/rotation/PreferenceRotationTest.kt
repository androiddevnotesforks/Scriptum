package sgtmelon.scriptum.test.auto.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [PreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRotationTest : ParentRotationTest() {

    // TODO finish

    @Test fun content() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    automator?.rotateSide()
                    assert()
                }
            }
        }
    }

    @Test fun colorDialog() = launch {
        val color = preferenceRepo.defaultColor

        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    openColorDialog(color) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

}