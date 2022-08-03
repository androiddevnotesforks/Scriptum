package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [BinFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class BinRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            binScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun contentList() = launch({ db.fillBin() }) {
        mainScreen {
            binScreen {
                automator.rotateSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun clearDialog() = launch({ db.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog {
                    automator.rotateSide()
                    assert()
                }
            }
        }
    }

    @Test fun textNoteDialog() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openNoteDialog(it) {
                        automator.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun rollNoteDialog() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openNoteDialog(it) {
                        automator.rotateSide()
                        assert()
                    }
                }
            }
        }
    }
}