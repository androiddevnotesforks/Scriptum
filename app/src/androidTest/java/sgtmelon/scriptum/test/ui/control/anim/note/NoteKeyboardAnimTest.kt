package sgtmelon.scriptum.test.ui.control.anim.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test of change insets on keyboard show/hide (and change editMode) for [TextNoteFragment],
 * [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteKeyboardAnimTest : ParentUiTest() {

    @Test fun insetsOnHideKeyboardInTextNote() = data.createText().let { note ->
        launch {
            mainScreen {
                openAddDialog {
                    createText(note) {
                        repeat(REPEAT_COUNT) { onEnterText(it.getRepeatString()).closeKeyboard() }
                    }
                }
            }
        }
    }

    @Test fun insetsOnHideKeyboardInRollNote() = data.createRoll().let { note ->
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(note) {
                        repeat(REPEAT_COUNT) {
                            enterPanel { onAdd(it.getRepeatString()) }.closeKeyboard()
                        }
                    }
                }
            }
        }
    }

    @Test fun insetsOnChangeModeInTextNote() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        onEnterText(nextString())
                        repeat(REPEAT_COUNT) { controlPanel { onSave().onEdit() } }
                    }
                }
            }
        }
    }

    @Test fun insetsOnChangeModeInRollNote() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        enterPanel { onAdd(nextString()) }
                        repeat(REPEAT_COUNT) { controlPanel { onSave().onEdit() } }
                    }
                }
            }
        }
    }

    /**
     * Test of change note mode with opened/closed keyboard.
     */
    @Test fun differentChangeModeInTextNote() = data.createText().let { note ->
        launch {
            mainScreen {
                openAddDialog {
                    createText(note) {
                        repeat(REPEAT_COUNT) {
                            onEnterText(it.getRepeatString())
                            if (it % 2 == 0) {
                                closeKeyboard()
                            }
                            controlPanel { onSave().onEdit() }
                        }
                    }
                }
            }
        }
    }

    /**
     * Test of change note mode with opened/closed keyboard.
     */
    @Test fun differentChangeModeInRollNote() = data.createRoll().let { note ->
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(note) {
                        repeat(REPEAT_COUNT) {
                            enterPanel { onAdd(it.getRepeatString()) }
                            if (it % 2 == 0) {
                                closeKeyboard()
                            }
                            controlPanel { onSave().onEdit() }
                        }
                    }
                }
            }
        }
    }

    private fun Int.getRepeatString() = "$this | ".plus(nextString())

    companion object {
        private const val REPEAT_COUNT = 7
    }
}