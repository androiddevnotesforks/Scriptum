package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    /**
     * Content
     */

    private fun contentOnBinWithoutName() = data.insertRollToBin(data.rollNote.copy(name = "")).let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    private fun contentOnBinWithName() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    private fun contentOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    private fun contentOnReadWithoutName() = data.insertRoll(data.rollNote.copy(name = "")).let {
        launch { mainScreen { openNotesPage { openRollNote(it) } } }
    }

    private fun contentOnReadWithName() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openRollNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    private fun closeOnBin() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    private fun closeOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    private fun closeOnRead() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    private fun saveOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        enterPanel { onAddRoll(data.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }

    private fun saveOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        enterPanel { onAddRoll(data.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }

    private fun cancelOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        enterPanel { onAddRoll(data.uniqueString) }
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

    private fun actionOnBinRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) { controlPanel { onRestore() } }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    private fun actionOnBinRestoreOpen() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    private fun actionOnBinClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) { controlPanel { onClear() } }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }


    // TODO write test
    private fun actionOnReadNotification() {}

    private fun actionOnReadBind() = bindTestPrototype(isStatus = false)

    private fun actionOnReadUnbind() = bindTestPrototype(isStatus = true)

    private fun bindTestPrototype(isStatus: Boolean) {
        val model = data.insertRoll(data.rollNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(model) {
                        controlPanel { onBind() }
                        onPressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    private fun actionOnReadConvert() = data.insertRoll().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onConvert() } } } }
        }
    }

    private fun actionOnReadDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                openBinPage(empty = true)

                openNotesPage {
                    openRollNote(it) { controlPanel { onDelete() } }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }

    private fun actionOnReadEdit() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openRollNote(it) { controlPanel { onEdit() } } } } }
    }


    // TODO write test
    private fun actionOnEditUndoRedo() {}

    // TODO write test
    private fun actionOnEditRank() {}

    private fun actionOnCreateColor() = data.createRoll().let {
        launch {
            mainScreen { openAddDialog { createRollNote(it) { controlPanel { onColor() } } } }
        }
    }

    private fun actionOnEditColor() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { controlPanel { onEdit().onColor() } } }
            }
        }
    }

    private fun actionOnCreateSave() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        repeat(times = 3) { enterPanel { onAddRoll(data.uniqueString) } }
                        onSwipe()
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    private fun actionOnEditSave() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAddRoll(data.uniqueString) } }
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    // TODO end assert
    @Test fun convertDialogCloseAndWork() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
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

    // TODO add note to rank and check it hide
    private fun rankDialogCloseAndWork() {}

    private fun colorDialogCloseAndWork() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        controlPanel { onColor { onCloseSoft() } }
                        assert()
                        controlPanel { onColor { onClickCancel() } }
                        assert()
                        controlPanel {
                            onColor { onClickEveryItem().onClickItem().onClickAccept() }
                        }
                    }
                }
            }
        }
    }

}