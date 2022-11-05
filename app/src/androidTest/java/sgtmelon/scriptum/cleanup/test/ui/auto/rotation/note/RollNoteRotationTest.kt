package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test of [RollNoteFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RollNoteRotationTest : ParentUiRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRead() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    @Test fun dateDialog() = db.insertRoll().let {
        startDateDialogTest(it, isUpdateDate = false)
    }

    @Test fun dateDialogReset() = db.insertNotification(db.insertRoll()).let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        startDateDialogTest(it, isUpdateDate = true)
    }

    private fun startDateDialogTest(item: NoteItem.Roll, isUpdateDate: Boolean) {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(item) {
                        controlPanel {
                            onNotification(isUpdateDate) {
                                rotate.toSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun timeDialog() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onNotification {
                                onClickApply {
                                    rotate.toSide()
                                    assert()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun convertDialog() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onConvert {
                                rotate.toSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun convertDialogResult() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onConvert { onClickYes() } }
                        rotate.toSide()
                        afterConvert()
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        controlPanel {
                            onColor {
                                onClickItem()
                                rotate.toSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun rankDialog() = db.fillRank(count = 3).let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(db.createRoll(), isRankEmpty = false) {
                        controlPanel {
                            onRank(it) {
                                onClickItem()
                                rotate.toSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

}