package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test control panel for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNotePanelTest : ParentUiTest() {

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

    @Test fun actionOnCreateRank() = data.fillRank(count = 3).let {
        val item = data.createRoll()
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = data.fillRank(count = 3).let {
        val item = data.insertRoll()
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(item, isRankEmpty = false) {
                        controlPanel { onEdit().onRank(it) }
                    }
                }
            }
        }
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
                        toolbar { onEnterName(Random.nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(Random.nextString()) } }
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

                        toolbar { onEnterName(Random.nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(Random.nextString()) } }

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateLongSave() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(Random.nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(Random.nextString()) } }
                        onSwipe()

                        controlPanel { onLongSave() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditLongSave() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }

                        toolbar { onEnterName(Random.nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(Random.nextString()) } }

                        controlPanel { onLongSave() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}