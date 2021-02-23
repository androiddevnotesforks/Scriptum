package sgtmelon.scriptum.test.control.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [MainActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentRotationTest() {

    @Test fun addDialog() = launch {
        mainScreen {
            openAddDialog {
                automator?.rotateSide()
                assert()
            }
        }
    }
}