package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test of [BinFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class BinRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            binScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun contentList() = launch({ db.fillBin() }) {
        mainScreen {
            binScreen {
                rotate.toSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun clearDialog() = launch({ db.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog {
                    rotate.toSide()
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
                        rotate.toSide()
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
                        rotate.toSide()
                        assert()
                    }
                }
            }
        }
    }
}