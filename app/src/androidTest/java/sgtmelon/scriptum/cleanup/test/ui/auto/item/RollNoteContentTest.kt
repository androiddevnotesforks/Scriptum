package sgtmelon.scriptum.cleanup.test.ui.auto.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.ui.item.RollItemUi
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test for [RollItemUi].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteContentTest : ParentUiTest() {

    @Test fun itemListOnNotes() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) { onAssertAll() } } } }
    }

    @Test fun itemListOnBin() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) { onAssertAll() } } } }
    }

    @Test fun itemOnChangeText() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel { onAdd(nextString()) }
                            onAssertAll()

                            onEnterText().onEnterText(nextString())
                        }
                    }
                }
            }
        }
    }

    @Test fun itemOnClickCheck() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                            onAssertAll()

                            controlPanel { onSave() }
                            repeat(times = 5) { onClickCheck() }
                        }
                    }
                }
            }
        }
    }

    @Test fun itemOnLongClickCheck() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(nextString()) } }
                            onAssertAll()

                            controlPanel { onSave() }
                            repeat(times = 3) {
                                onLongClickCheck().onClickCheck().onLongClickCheck()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun itemOnEmptyDelete() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(nextString()) } }
                            onEnterText(p = 0).onEnterText(p = 3)

                            controlPanel { onSave() }
                            onAssertAll()
                        }
                    }
                }
            }
        }
    }

}