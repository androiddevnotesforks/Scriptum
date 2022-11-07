package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openRollNote(it) { toolbar { clickBack() } } }.assert()
                binScreen { openRollNote(it) { pressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRoll(it) { toolbar { clickBack() } } }.assert()
                openAddDialog { createRoll(it) { pressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { toolbar { clickBack() } } }.assert()
                notesScreen { openRollNote(it) { pressBack() } }.assert()
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
                        pressBack()
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
                        pressBack()
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
                            clickBack()
                        }
                    }
                }
            }
        }
    }

}