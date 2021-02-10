package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test visible button and list for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteVisibleTest : ParentUiTest() {

    @Test fun changeOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()
                        onPressBack()
                    }

                    openRollNote(it) {
                        onClickVisible()
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun changeCreate() {
        var item = data.createRoll()

        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(item) {
                            onClickVisible()

                            enterPanel { onAdd(nextString()) }
                            controlPanel { onSave() }

                            item = this.item

                            onPressBack()
                        }
                    }

                    openRollNote(item) { onClickVisible() }
                }
            }
        }
    }

    @Test fun switch() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            onClickVisible().onClickVisible()

                            repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                            controlPanel { onSave() }
                            onClickCheck()
                            onClickVisible()

                            controlPanel { onEdit() }
                            onClickVisible()
                        }
                    }
                }
            }
        }
    }

    @Test fun itemCheck() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickCheck()
                        onClickVisible()

                        repeat(times = 2) { onClickCheck() }
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemLongCheck() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        repeat(times = 2) { onLongClickCheck() }
                        onClickVisible()

                        onLongClickCheck()
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemSwipe() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()

                        controlPanel { onEdit() }
                        onSwipe()
                        controlPanel { onSave() }

                        onClickVisible()
                        controlPanel { onEdit() }
                    }
                }
            }
        }
    }

    @Test fun itemAdd() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            onClickVisible()

                            enterPanel { repeat(times = 5) { onAdd(nextString()) } }
                            controlPanel { onSave() }

                            onClickVisible()
                        }
                    }
                }
            }
        }
    }

    @Test fun itemEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()

                        controlPanel { onEdit() }
                        onEnterText(nextString())
                        controlPanel { onSave() }

                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemUndoRedo() {
        TODO()
    }
}