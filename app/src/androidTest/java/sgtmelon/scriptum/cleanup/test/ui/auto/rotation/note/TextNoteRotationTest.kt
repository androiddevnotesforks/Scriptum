package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [TextNoteFragmentImpl] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class TextNoteRotationTest : ParentUiRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = db.insertTextToBin().let {
        launchSplash {
            mainScreen {
                openBin {
                    openText(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = db.insertTextToBin().let {
        launchSplash {
            mainScreen {
                openBin {
                    openText(it) {
                        controlPanel { onRestoreOpen() }
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRead() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
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

    @Test fun dateDialog() = db.insertText().let {
        startDateDialogTest(it, isUpdateDate = false)
    }

    @Test fun dateDialogReset() = db.insertNotification(db.insertText()).let {
        if (it !is NoteItem.Text) throw NoteCastException()

        startDateDialogTest(it, isUpdateDate = true)
    }

    private fun startDateDialogTest(item: NoteItem.Text, isUpdateDate: Boolean) {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(item) {
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

    @Test fun timeDialog() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
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


    @Test fun convertDialog() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
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

    @Test fun convertDialogResult() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel {
                            onConvert { onClickYes() }
                            rotate.toSide()
                            afterConvert()
                        }
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        controlPanel {
                            onColor {
                                select()
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
                    createText(db.createText(), isRankEmpty = false) {
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