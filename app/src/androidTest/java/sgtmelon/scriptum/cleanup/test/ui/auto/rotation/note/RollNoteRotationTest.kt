package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [RollNoteFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RollNoteRotationTest : ParentUiRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openBin {
                    openRoll(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openBin {
                    openRoll(it) {
                        controlPanel { onRestoreOpen() }
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = db.createRoll().let {
        launchSplash {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(item) {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel {
                            onNotification {
                                applyDate {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onConvert { onClickYes() } }
                        rotate.toSide()
                        afterConvert()
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = db.createRoll().let {
        launchSplash {
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
        launchSplash {
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