package sgtmelon.scriptum.test.control.notification.bind

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test for bind notification inside [NoteActivity].
 */
@RunWith(AndroidJUnit4::class)
class BindNoteTest : ParentNotificationTest() {

    /**
     * Notify on start is implied
     */

    @Test fun textNoteBindUnbind() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onBind().apply { onSee() }.onBind().apply { onSee() }
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteBindUnbind() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onBind().apply { onSee() }.onBind().apply { onSee() }
                        }
                    }
                }
            }
        }
    }

    @Test fun textNoteUpdateOnConvert() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onConvert { onSee { onClickYes() } }
                            onSee { onBind() }
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteUpdateOnConvert() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel {
                            onConvert { onSee { onClickYes() } }
                            onSee { onBind() }
                        }
                    }
                }
            }
        }
    }

    @Test fun textNoteUnbindOnDelete() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }
                    onSee { assert(empty = true) }
                }
            }
        }
    }

    @Test fun rollNoteUnbindOnDelete() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { onDelete() } }
                    onSee { assert(empty = true) }
                }
            }
        }
    }

}