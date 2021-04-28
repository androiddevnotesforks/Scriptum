package sgtmelon.scriptum.test.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.ParentNotificationTest
import kotlin.random.Random

/**
 * Test for bind notification inside [MainActivity].
 */
class BindMainTest : ParentNotificationTest() {

    /**
     * Notify on start is implied
     */

    @Test fun notesTextBindUnbind() = startNotesBindUnbindTest(data.insertText())

    @Test fun notesRollBindUnbind() = startNotesBindUnbindTest(data.insertRoll())

    private fun startNotesBindUnbindTest(item: NoteItem) = launch {
        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(item) { onBind() }
                apply { onSee() }.openNoteDialog(item) { onBind() }
            }
        }
    }


    @Test fun notesTextUpdateOnConvert() = startNotesUpdateOnConvertText(with(data) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUpdateOnConvert() = startNotesUpdateOnConvertText(with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUpdateOnConvertText(item: NoteItem) = launch {
        mainScreen {
            notesScreen {
                apply { onSee() }.openNoteDialog(item) { onConvert() }
                apply { onSee() }.openNoteDialog(item) { onDelete() }
            }
        }
    }


    @Test fun notesTextUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(data) {
        insertText(textNote.copy(isStatus = true))
    })

    @Test fun notesRollUnbindOnDelete() = startNotesUnbindOnDeleteTest(with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    })

    private fun startNotesUnbindOnDeleteTest(item: NoteItem) = launch {
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

    @Test fun rankHideShowOnLongClick() = insertRankWithStatusNote().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onSee { onClickVisible() } }
                notesScreen(isEmpty = true, isHide = true)
                rankScreen { onSee { onLongClickVisible() } }
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
        val noteItem = with(data) {
            return@with if (Random.nextBoolean()) {
                insertText(textNote.copy(isStatus = true))
            } else {
                insertRoll(rollNote.copy(isStatus = true))
            }
        }

        val rankEntity = with(data) {
            insertRank(rankEntity.copy(noteId = arrayListOf(noteItem.id)))
        }

        val converter = NoteConverter()
        data.inRoomTest {
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