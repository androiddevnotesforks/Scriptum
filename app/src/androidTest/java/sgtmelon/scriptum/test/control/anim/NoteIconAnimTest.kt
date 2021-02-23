package sgtmelon.scriptum.test.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of [NavigationIconControl] animations for [TextNoteFragment], [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class NoteIconAnimTest : ParentUiTest() {

    @Test fun arrowBackOnCreateTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) { onEnterText(data.textNote.text).controlPanel { onSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        enterPanel { onAdd(data.rollList.first().text) }
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onPressBack()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        onPressBack()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }
}