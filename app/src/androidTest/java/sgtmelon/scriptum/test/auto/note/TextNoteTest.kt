package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentUiTest() {

    /**
     * Content
     */

    @Test fun contentOnBinWithoutName() = data.insertTextToBin(data.textNote.copy(name = "")).let {
        launch { mainScreen { binScreen { openTextNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openTextNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertText(data.textNote.copy(name = "")).let {
        launch { mainScreen { notesScreen { openTextNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    @Test fun closeOnBin()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                binScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createText(it) { toolbar { onClickBack() } } }.assert()
                openAddDialog { createText(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                notesScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun saveOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun cancelOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(data.uniqueString)
                        toolbar { onEnterName(data.uniqueString).onClickBack() }
                    }
                }
            }
        }
    }

    /**
     * Panel action
     */

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

    @Test fun actionOnReadBind() = bindTestPrototype(isStatus = false)

    @Test fun actionOnReadUnbind() = bindTestPrototype(isStatus = true)

    private fun bindTestPrototype(isStatus: Boolean) {
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

    @Test fun actionOnEditRank() {
        TODO(reason = "#TEST write test")
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

    /**
     * Dialogs
     */

    @Test fun dateDialogCloseAndWork() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onNotification { onCloseSoft() } }.assert()
                        controlPanel { onNotification { onClickCancel() } }.assert()
                        controlPanel { onNotification { onClickApply() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onConvert { onCloseSoft() } }.assert()
                        controlPanel { onConvert { onClickNo() } }.assert()
                        controlPanel { onConvert { onClickYes() } }
                        TODO(reason = "#TEST write test")
                    }
                }
            }
        }
    }

    @Test fun rankDialogCloseAndWork() {
        TODO(reason = "#TEST write test")
    }

    @Test fun colorDialogCloseAndWork() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        controlPanel { onColor { onCloseSoft() } }.assert()
                        controlPanel { onColor { onClickCancel() } }.assert()
                        controlPanel { onColor { onClickAll().onClickItem().onClickAccept() } }
                    }
                }
            }
        }
    }

}