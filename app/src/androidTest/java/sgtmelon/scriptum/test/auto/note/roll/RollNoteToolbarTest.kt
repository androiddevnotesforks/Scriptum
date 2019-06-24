package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentUiTest() {

    @Test fun closeByToolbarOnCreate() = testData.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = testData.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }


    @Test fun contentEmptyOnCreate() = testData.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = testData.insertRoll(
            testData.rollNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentFillOnOpen() = testData.insertRoll().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }


    @Test fun saveByControlOnCreate() = testData.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        enterPanel { onAddRoll(testData.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = testData.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        enterPanel { onAddRoll(testData.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(testData.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnEdit() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(testData.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = testData.createRoll().let {
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