package sgtmelon.scriptum.test.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [RollNoteFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RollNoteRotationTest : ParentRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRead() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    @Test fun dateDialog() = data.insertRoll().let {
        startDateDialogTest(it, isUpdateDate = false)
    }

    @Test fun dateDialogReset() = data.insertNotification(data.insertRoll()).let {
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
                                automator?.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun timeDialog() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onNotification {
                                onClickApply {
                                    automator?.rotateSide()
                                    assert()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun convertDialog() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onConvert {
                                automator?.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun convertDialogResult() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onConvert { onClickYes() } }
                        automator?.rotateSide()
                        afterConvert()
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        controlPanel {
                            onColor {
                                onClickItem()
                                automator?.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun rankDialog() = data.fillRank(count = 3).let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(data.createRoll(), isRankEmpty = false) {
                        controlPanel {
                            onRank(it) {
                                onClickItem()
                                automator?.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

}