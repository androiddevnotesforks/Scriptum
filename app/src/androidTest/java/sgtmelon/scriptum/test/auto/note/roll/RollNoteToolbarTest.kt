package sgtmelon.scriptum.test.auto.note.roll

import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentUiTest() {

    private fun closeByToolbarOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    private fun closeByBackPressOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    private fun closeByToolbarOnOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    private fun closeByBackPressOnOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }


    private fun contentEmptyOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    private fun contentEmptyOnOpen() = data.insertRoll(
            data.rollNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    private fun contentFillOnOpen() = data.insertRoll().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }


    private fun saveByControlOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        enterPanel { onAddRoll(data.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    private fun saveByBackPressOnCreate() = data.createRoll().let {
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

    private fun saveByControlOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    private fun saveByBackPressOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }


    private fun cancelOnEdit() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

}