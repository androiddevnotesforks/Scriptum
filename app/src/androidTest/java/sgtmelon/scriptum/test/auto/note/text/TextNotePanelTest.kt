package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test control panel for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openTextNote(it) { controlPanel { onRestore() } }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                binScreen {
                    openTextNote(it) { controlPanel { onRestoreOpen() }.onPressBack() }
                    assert(empty = true)
                }

                notesScreen()
            }
        }
    }

    @Test fun actionOnBinClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openTextNote(it) { controlPanel { onClear() } }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = data.insertText(data.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                notesScreen {
                    openTextNote(model) { controlPanel { onBind() }.onPressBack() }
                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onConvert() } } } } }
    }

    @Test fun actionOnReadDelete() = data.insertText().let {
        launch {
            mainScreen {
                binScreen(empty = true)

                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }.assert(empty = true)
                }

                binScreen()
            }
        }
    }

    @Test fun actionOnReadEdit() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit() } } } } }
    }


    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateRank() = data.fillRank(count = 3).let {
        val item = data.createText()
        launch {
            mainScreen {
                openAddDialog {
                    createText(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = data.fillRank(count = 3).let {
        val item = data.insertText()
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnCreateColor() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit().onColor() } } } }
        }
    }

    @Test fun actionOnCreateSave() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString).onEnterText().onEnterText(data.uniqueString)
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText()
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

}