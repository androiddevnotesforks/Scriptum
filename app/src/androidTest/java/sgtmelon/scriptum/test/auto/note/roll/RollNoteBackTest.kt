package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test toolbar arrow and back press for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteBackTest : ParentUiTest() {

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
                        toolbar { onEnterName(Random.nextString()) }
                        enterPanel { onAdd(Random.nextString()) }
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
                        toolbar { onEnterName(Random.nextString()) }
                        enterPanel { onAdd(Random.nextString()) }
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
                        enterPanel { onAdd(Random.nextString()) }
                        toolbar {
                            onEnterName(Random.nextString())
                            onClickBack()
                        }
                    }
                }
            }
        }
    }

}