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
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertText(data.textNote.copy(name = "")).let {
        launch { mainScreen { openNotesPage { openTextNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertText().let {
        launch { mainScreen { openNotesPage { openTextNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    @Test fun closeOnBin()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun saveOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
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
                openNotesPage {
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
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(data.uniqueString)
                        toolbar {
                            onEnterName(data.uniqueString)
                            onClickBack()
                        }
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
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onRestore() } }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) {
                        controlPanel { onRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun actionOnBinClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onClear() } }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() {}

    @Test fun actionOnReadBind() = bindTestPrototype(isStatus = false)

    @Test fun actionOnReadUnbind() = bindTestPrototype(isStatus = true)

    private fun bindTestPrototype(isStatus: Boolean) {
        val model = data.insertText(data.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(model) {
                        controlPanel { onBind() }
                        onPressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = data.insertText().let {
        launch {
            mainScreen {
                openBinPage(empty = true)

                openNotesPage {
                    openTextNote(it) { controlPanel { onDelete() } }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }

    @Test fun actionOnReadEdit() {}

    /**
     * Dialogs
     */

    // TODO end assert
    @Test fun convertDialogCloseAndWork() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onConvert { onCloseSoft() } }
                        assert()
                        controlPanel { onConvert { onClickNo() } }
                        assert()
                        controlPanel { onConvert { onClickYes() } }
                    }
                }
            }
        }
    }

}