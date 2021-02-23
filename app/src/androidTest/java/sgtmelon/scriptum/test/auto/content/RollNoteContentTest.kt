package sgtmelon.scriptum.test.auto.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen

/**
 * Test for [RollNoteScreen.Item].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteContentTest : ParentUiTest() {

    @Test fun itemList_onNotes() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) { onAssertAll() } } } }
    }

    @Test fun itemList_onBin() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) { onAssertAll() } } } }
    }

    @Test fun item_onChangeText() = data.createRoll().let {
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

    @Test fun item_onClickCheck() = data.createRoll().let {
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

    @Test fun item_onLongClickCheck() = data.createRoll().let {
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

    @Test fun item_onEmptyDelete() = data.createRoll().let {
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