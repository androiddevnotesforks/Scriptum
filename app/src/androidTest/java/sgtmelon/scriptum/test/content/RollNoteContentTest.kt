package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
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
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel { onAdd(data.uniqueString) }
                            onAssertAll()

                            onEnterText().onEnterText(data.uniqueString)
                        }
                    }
                }
            }
        }
    }

    @Test fun item_onClickCheck() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 3) { enterPanel { onAdd(data.uniqueString) } }
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
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(data.uniqueString) } }
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

}