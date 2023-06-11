package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity


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

        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
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
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel {
                            onConvert { onSee { positive() } }
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
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel {
                            onConvert { onSee { positive() } }
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
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) { controlPanel { onDelete() } }
                    onSee { assert(isEmpty = true) }
                }
            }
        }
    }

    @Test fun rollNoteUnbindOnDelete() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        TODO()
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) { controlPanel { onDelete() } }
                    onSee { assert(isEmpty = true) }
                }
            }
        }
    }
}