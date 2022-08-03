package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest

/**
 * Test for bind notification inside [NoteActivity].
 */
@RunWith(AndroidJUnit4::class)
class BindNoteTest : ParentNotificationTest() {

    // TODO fix all


    /**
     * Notify on start is implied
     */

    @Test fun textNoteBindUnbind() = db.insertText().let {
        TODO()

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

    @Test fun rollNoteBindUnbind() = db.insertRoll().let {
        TODO()
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

    @Test fun textNoteUpdateOnConvert() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        TODO()
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

    @Test fun rollNoteUpdateOnConvert() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        TODO()
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

    @Test fun textNoteUnbindOnDelete() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        TODO()
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }
                    onSee { assert(isEmpty = true) }
                }
            }
        }
    }

    @Test fun rollNoteUnbindOnDelete() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        TODO()
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { onDelete() } }
                    onSee { assert(isEmpty = true) }
                }
            }
        }
    }

}