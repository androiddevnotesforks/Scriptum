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

    @Test fun contentOnBinWithoutName() = data.insertRollToBin(data.rollNote.copy(name = "")).let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertRoll(data.rollNote.copy(name = "")).let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    @Test fun closeOnBin() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openRollNote(it) { toolbar { onClickBack() } } }.assert()
                binScreen { openRollNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRoll(it) { toolbar { onClickBack() } } }.assert()
                openAddDialog { createRoll(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { toolbar { onClickBack() } } }.assert()
                notesScreen { openRollNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun saveOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        enterPanel { onAddRoll(data.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
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

    @Test fun cancelOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
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

    @Test fun actionOnBinRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openRollNote(it) { controlPanel { onRestore() } }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                binScreen {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                notesScreen()
            }
        }
    }

    @Test fun actionOnBinClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openRollNote(it) { controlPanel { onClear() } }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = data.insertRoll(data.rollNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                notesScreen {
                    openRollNote(model) {
                        controlPanel { onBind() }
                        onPressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                binScreen(empty = true)

                notesScreen {
                    openRollNote(it) { controlPanel { onDelete() } }.assert(empty = true)
                }

                binScreen()
            }
        }
    }

    @Test fun actionOnReadEdit() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) { controlPanel { onEdit() } } } } }
    }


    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnEditRank() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateColor() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { controlPanel { onEdit().onColor() } } }
            }
        }
    }

    @Test fun actionOnCreateSave() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        repeat(times = 3) { enterPanel { onAddRoll(data.uniqueString) } }
                        onSwipe()
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
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

    @Test fun dateDialogCloseAndWork() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onNotification { onCloseSoft() } }.assert()
                        controlPanel { onNotification { onClickCancel() } }.assert()
                        controlPanel { onNotification { onClickApply() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
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

    @Test fun colorDialogCloseAndWork() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        controlPanel { onColor { onCloseSoft() } }.assert()
                        controlPanel { onColor { onClickCancel() } }.assert()
                        controlPanel { onColor { onClickAll().onClickItem().onClickAccept() } }
                    }
                }
            }
        }
    }

}