package sgtmelon.scriptum.test.control.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [TextNoteFragment] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class TextNoteRotationTest : ParentRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openTextNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRestoreOpen() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        onRotate { assert() }
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { addDialog { createText(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRead() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { onRotate({ controlPanel { onEdit() } }) { assert() } }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    @Test fun dateDialog() = data.insertText().let {
        startDateDialogTest(it, updateDate = false)
    }

    @Test fun dateDialogReset() = data.insertNotification(data.insertText()).let {
        startDateDialogTest(it, updateDate = true)
    }

    private fun startDateDialogTest(noteModel: NoteModel, updateDate: Boolean) {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(noteModel) {
                        controlPanel { onNotification(updateDate) { onRotate { assert() } } }
                    }
                }
            }
        }
    }


    @Test fun convertDialog() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onConvert { onRotate { assert() } } } }
                }
            }
        }
    }

    @Test fun convertDialogResult() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onConvert { onClickYes() }
                            onRotate {
                                // TODO #TEST convert assert
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = data.createText().let {
        launch {
            mainScreen {
                addDialog {
                    createText(it) {
                        controlPanel {
                            onColor {
                                onClickItem()
                                onRotate { assert() }
                            }
                        }
                    }
                }
            }
        }
    }

}