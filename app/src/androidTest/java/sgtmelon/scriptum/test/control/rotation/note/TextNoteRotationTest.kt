package sgtmelon.scriptum.test.control.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
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
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRestoreOpen() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnRead() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        automator?.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun contentOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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

    @Test fun dateDialog() = data.insertText().let {
        startDateDialogTest(it, isUpdateDate = false)
    }

    @Test fun dateDialogReset() = data.insertNotification(data.insertText()).let {
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
                                automator?.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun timeDialog() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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


    @Test fun convertDialog() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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

    @Test fun convertDialogResult() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onConvert { onClickYes() }
                            automator?.rotateSide()
                            afterConvert()
                        }
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
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
                    createText(data.createText(), isRankEmpty = false) {
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