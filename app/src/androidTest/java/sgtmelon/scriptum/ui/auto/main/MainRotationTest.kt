package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [MainActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentUiRotationTest() {

    @Test fun rankPage() = launch {
        mainScreen {
            openRank(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun notesPage() = launch {
        mainScreen {
            openNotes(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert()
        }
    }

    @Test fun binPage() = launch {
        mainScreen {
            openBin(isEmpty = true) {
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