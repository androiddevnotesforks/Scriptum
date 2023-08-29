package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest

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
                        rotate.switch()
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
                        rotate.switch()
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
                        rotate.switch()
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
                        rotate.switch()
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
                        rotate.switch()
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
                                rotate.switch()
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
                                    rotate.switch()
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
                                rotate.switch()
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
                        controlPanel { onConvert { positive() } }
                        rotate.switch()
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
                                select()
                                rotate.switch()
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
                                rotate.switch()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }
}