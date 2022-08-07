package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.parent.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openRollNote(it) { toolbar { onClickBack() } } }.assert()
                binScreen { openRollNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRoll(it) { toolbar { onClickBack() } } }.assert()
                openAddDialog { createRoll(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { toolbar { onClickBack() } } }.assert()
                notesScreen { openRollNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun saveOnCreate() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        enterPanel { onAdd(nextString()) }
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(nextString()) }
                        enterPanel { onAdd(nextString()) }
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun cancelOnEdit() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        enterPanel { onAdd(nextString()) }
                        toolbar {
                            onEnterName(nextString())
                            onClickBack()
                        }
                    }
                }
            }
        }
    }

}