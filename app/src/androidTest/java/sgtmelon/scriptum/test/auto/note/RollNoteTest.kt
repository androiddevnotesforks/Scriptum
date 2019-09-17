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
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertRoll(data.rollNote.copy(name = "")).let {
        launch { mainScreen { openNotesPage { openRollNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openRollNote(it) } } }
    }

    /**
     * ToolbarArrow / BackPress
     */

    @Test fun closeOnBin()  = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun saveOnCreate() = data.createRoll().let {
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

    @Test fun saveOnEdit() = data.insertRoll().let {
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

    @Test fun cancelOnEdit() = data.insertRoll().let {
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

    @Test fun actionOnBinRestore()  = data.insertRollToBin().let {
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

    @Test fun actionOnBinRestoreOpen()  = data.insertRollToBin().let {
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

    @Test fun actionOnBinClear() = data.insertRollToBin().let {
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


    @Test fun actionOnReadNotification() {}

    @Test fun actionOnReadBind() = bindTestPrototype(isStatus = false)

    @Test fun actionOnReadUnbind() = bindTestPrototype(isStatus = true)

    @Test fun bindTestPrototype(isStatus: Boolean) {
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

    @Test fun actionOnReadConvert() = data.insertRoll().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = data.insertRoll().let {
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

    @Test fun actionOnReadEdit() {}

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

}