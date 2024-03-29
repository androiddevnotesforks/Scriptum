package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity


/**
 * Test for bind notification inside [MainActivity].
 */
class BindMainTest : ParentNotificationTest() {

    // TODO fix all

    /**
     * Notify on start is implied
     */

    @Test fun notesTextBindUnbind() = startNotesBindUnbindTest(db.insertText())

    @Test fun notesRollBindUnbind() = startNotesBindUnbindTest(db.insertRoll())

    private fun startNotesBindUnbindTest(item: NoteItem) = launchSplash {
        TODO()

        mainScreen {
            openNotes {
                apply { onSee() }.openNoteDialog(item) { bind() }
                apply { onSee() }.openNoteDialog(item) { bind() }
            }
        }
    }


    @Test fun notesTextUpdateOnConvert() = startNotesUpdateOnConvertText(with(db) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUpdateOnConvert() = startNotesUpdateOnConvertText(with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUpdateOnConvertText(item: NoteItem) = launchSplash {
        TODO()

        mainScreen {
            openNotes {
                apply { onSee() }.openNoteDialog(item) { convert() }
                apply { onSee() }.openNoteDialog(item) { delete() }
            }
        }
    }


    @Test fun notesTextUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(db) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUnbindOnDeleteTest(item: NoteItem) = launchSplash {
        TODO()

        mainScreen {
            openNotes { openNoteDialog(item) { onSee { delete() } } }
            openBin { onSee { assertItem(item, 0) } }
        }
    }


    @Test fun rankHideShowOnClick() = insertRankWithStatusNote().let {
        launchSplash {
            mainScreen {
                openNotes()
                openRank { onSee { itemVisible() } }
                openNotes(isEmpty = true, isHide = true)
                openRank { onSee { itemVisible() } }
                openNotes { openNoteDialog(it) { onSee { bind() } } }
            }
        }
    }

    @Test fun rankCancel() = insertRankWithStatusNote().let {
        launchSplash {
            mainScreen {
                openRank { onSee { itemVisible() } }
                openNotes(isEmpty = true, isHide = true)
                openRank { onSee { itemCancel() } }
                openNotes { openNoteDialog(it) { onSee { bind() } } }
            }
        }
    }

    /**
     * Update binds on snackbar undo
     */
    @Test fun rankUndo() {
        TODO()
    }

    private fun insertRankWithStatusNote(): NoteItem {
        TODO()
        //
        //        val noteItem = with(db) {
        //            return@with if (halfChance()) {
        //                insertText(textNote.copy(isStatus = true))
        //            } else {
        //                insertRoll(rollNote.copy(isStatus = true))
        //            }
        //        }
        //
        //        val rankEntity = with(db) {
        //            insertRank(rankEntity.copy(noteId = arrayListOf(noteItem.id)))
        //        }
        //
        //        val converter = NoteConverter()
        //        db.inRoomTest {
        //            noteDao.update(converter.toEntity(noteItem.apply {
        //                rankId = rankEntity.id
        //                rankPs = rankEntity.position
        //            }))
        //        }
        //
        //        return noteItem
    }


    /**
     * Notify notes order in status bar after change sort
     */
    @Test fun preferenceChangeSort() {
        TODO()
    }

    /**
     * Notify after adding notes from backup file
     */
    @Test fun preferenceBackupResult() {
        TODO()
    }
}