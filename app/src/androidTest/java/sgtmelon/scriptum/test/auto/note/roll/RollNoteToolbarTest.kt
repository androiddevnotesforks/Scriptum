package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentUiTest() {

    @Test fun closeByToolbarOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }


    @Test fun contentEmptyOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = data.insertRoll(
            data.rollNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentFillOnOpen() = data.insertRoll().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }


    @Test fun saveByControlOnCreate() = data.createRoll().let {
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

    @Test fun saveByBackPressOnCreate() = data.createRoll().let {
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

    @Test fun saveByControlOnEdit() = data.insertRoll().let {
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

    @Test fun saveByBackPressOnEdit() = data.insertRoll().let {
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


    @Test fun cancelOnEdit() = data.createRoll().let {
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