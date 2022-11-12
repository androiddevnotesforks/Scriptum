package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.ui.cases.ListContentCase

/**
 * Test of [BinFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class BinRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launch {
        mainScreen {
            openBin(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test override fun contentList() = db.fillBin().let {
        launch {
            mainScreen {
                openBin {
                    rotate.toSide()
                    assert(isEmpty = false)
                    assertList(it)
                }
                assert(isFabVisible = false)
            }
        }
    }

    @Test fun clearDialog() = launch({ db.fillBin() }) {
        mainScreen {
            openBin {
                openClearDialog {
                    rotate.toSide()
                    assert()
                }
            }
        }
    }

    @Test fun textNoteDialog() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
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
                openBin {
                    openNoteDialog(it) {
                        rotate.toSide()
                        assert()
                    }
                }
            }
        }
    }
}