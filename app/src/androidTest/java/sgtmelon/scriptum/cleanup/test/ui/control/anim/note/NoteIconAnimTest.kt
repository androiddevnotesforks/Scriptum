package sgtmelon.scriptum.cleanup.test.ui.control.anim.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test of [NavigationIconControl] and visibleRollIcon animations for [TextNoteFragment],
 * [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteIconAnimTest : ParentUiTest() {

    @Test fun arrowBackOnCreateTextNote() = db.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = db.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) { onEnterText(nextString()).controlPanel { onSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = db.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        enterPanel { onAdd(db.rollList.first().text) }
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = db.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = db.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = db.insertText().let {
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

    @Test fun animateOnEditToCancelRollNote() = db.insertRoll().let {
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

    @Test fun visibleClick() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { repeat(REPEAT_COUNT) { onClickVisible() } } }
            }
        }
    }


    companion object {
        private const val REPEAT_COUNT = 5
    }
}