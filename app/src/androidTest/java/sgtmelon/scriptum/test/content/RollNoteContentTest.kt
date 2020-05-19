package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import kotlin.random.Random

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
                            enterPanel { onAdd(Random.nextString()) }
                            onAssertAll()

                            onEnterText().onEnterText(Random.nextString())
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
                            repeat(times = 3) { enterPanel { onAdd(Random.nextString()) } }
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
                            repeat(times = 5) { enterPanel { onAdd(Random.nextString()) } }
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
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(Random.nextString()) } }
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