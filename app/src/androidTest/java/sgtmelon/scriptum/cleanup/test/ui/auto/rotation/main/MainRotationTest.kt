package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest

/**
 * Test of [MainActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentUiRotationTest() {

    @Test fun rankPage() = launch {
        mainScreen {
            rankScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun notesPage() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun binPage() = launch {
        mainScreen {
            binScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun addDialog() = launch {
        mainScreen {
            openAddDialog {
                rotate.toSide()
                assert()
            }
        }
    }
}