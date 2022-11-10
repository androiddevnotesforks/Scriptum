package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [TextNoteFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class TextNoteRotationTest : ParentUiRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = db.createText().let {
        launch {
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
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        rotate.toSide()
                        fullAssert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(item) {
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
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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


    @Test fun convertDialog() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
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