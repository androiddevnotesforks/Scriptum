package sgtmelon.scriptum.cleanup.test.ui.control.anim.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test of change insets on keyboard show/hide (and change editMode) for [TextNoteFragmentImpl],
 * [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class NoteKeyboardAnimTest : ParentUiTest() {

    @Test fun insetsOnHideKeyboardInTextNote() = db.createText().let { note ->
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(note) {
                        repeat(REPEAT_COUNT) { onEnterText(it.getRepeatString()).closeKeyboard() }
                    }
                }
            }
        }
    }

    @Test fun insetsOnHideKeyboardInRollNote() = db.createRoll().let { note ->
        launchSplash {
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

    @Test fun insetsOnChangeModeInTextNote() = db.createText().let {
        launchSplash {
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

    @Test fun insetsOnChangeModeInRollNote() = db.createRoll().let {
        launchSplash {
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
    @Test fun differentChangeModeInTextNote() = db.createText().let { note ->
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(note) {
                        repeat(REPEAT_COUNT) {
                            onEnterText(it.getRepeatString())
                            if (it.isDivideEntirely()) {
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
    @Test fun differentChangeModeInRollNote() = db.createRoll().let { note ->
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(note) {
                        repeat(REPEAT_COUNT) {
                            enterPanel { onAdd(it.getRepeatString()) }
                            if (it.isDivideEntirely()) {
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