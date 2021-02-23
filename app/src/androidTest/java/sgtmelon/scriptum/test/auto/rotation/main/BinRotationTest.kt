package sgtmelon.scriptum.test.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [BinFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class BinRotationTest : ParentRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            binScreen(isEmpty = true) {
                automator?.rotateSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun contentList() = launch({ data.fillBin() }) {
        mainScreen {
            binScreen {
                automator?.rotateSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun clearDialog() = launch({ data.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog {
                    automator?.rotateSide()
                    assert()
                }
            }
        }
    }

    @Test fun textNoteDialog() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openNoteDialog(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun rollNoteDialog() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openNoteDialog(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }
}