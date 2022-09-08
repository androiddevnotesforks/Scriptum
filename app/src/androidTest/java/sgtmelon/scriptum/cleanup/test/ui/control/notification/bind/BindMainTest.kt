package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import kotlin.random.Random
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest

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

    private fun startNotesBindUnbindTest(item: NoteItem) = launch {
        TODO()

        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(item) { onBind() }
                apply { onSee() }.openNoteDialog(item) { onBind() }
            }
        }
    }


    @Test fun notesTextUpdateOnConvert() = startNotesUpdateOnConvertText(with(db) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUpdateOnConvert() = startNotesUpdateOnConvertText(with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUpdateOnConvertText(item: NoteItem) = launch {
        TODO()

        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(item) { onConvert() }
                apply { onSee() }.openNoteDialog(item) { onDelete() }
            }
        }
    }


    @Test fun notesTextUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(db) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUnbindOnDeleteTest(item: NoteItem) = launch {
        TODO()

        mainScreen {
            notesScreen { openNoteDialog(item) { onSee { onDelete() } } }
            binScreen { onSee { onAssertItem(item, 0) } }
        }
    }


    @Test fun rankHideShowOnClick() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onSee { onClickVisible() } }
                notesScreen(isEmpty = true, isHide = true)
                rankScreen { onSee { onClickVisible() } }
                notesScreen { openNoteDialog(it) { onSee { onBind() } } }
            }
        }
    }

    @Test fun rankCancel() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                rankScreen { onSee { onClickVisible() } }
                notesScreen(isEmpty = true, isHide = true)
                rankScreen { onSee { onClickCancel() } }
                notesScreen { openNoteDialog(it) { onSee { onBind() } } }
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

        val noteItem = with(db) {
            return@with if (Random.nextBoolean()) {
                insertText(textNote.copy(isStatus = true))
            } else {
                insertRoll(rollNote.copy(isStatus = true))
            }
        }

        val rankEntity = with(db) {
            insertRank(rankEntity.copy(noteId = arrayListOf(noteItem.id)))
        }

        val converter = NoteConverter()
        db.inRoomTest {
            noteDao.update(converter.toEntity(noteItem.apply {
                rankId = rankEntity.id
                rankPs = rankEntity.position
            }))
        }

        return noteItem
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