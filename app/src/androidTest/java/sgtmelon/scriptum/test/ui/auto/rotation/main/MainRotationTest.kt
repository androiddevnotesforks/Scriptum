package sgtmelon.scriptum.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test of [MainActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentRotationTest() {

    @Test fun rankPage() = launch {
        mainScreen {
            rankScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun notesPage() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun binPage() = launch {
        mainScreen {
            binScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun addDialog() = launch {
        mainScreen {
            openAddDialog {
                automator.rotateSide()
                assert()
            }
        }
    }
}