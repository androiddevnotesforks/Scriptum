package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ParentUiRotationTest

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
                        automator.rotateSide()
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
                        automator.rotateSide()
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
                        automator.rotateSide()
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
                        automator.rotateSide()
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
                        automator.rotateSide()
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
                                automator.rotateSide()
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
                                    automator.rotateSide()
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
                                automator.rotateSide()
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
                            automator.rotateSide()
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
                                automator.rotateSide()
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
                                automator.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

}