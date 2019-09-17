package sgtmelon.scriptum.test.auto.note.roll

import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentUiTest() {

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

}