package sgtmelon.scriptum.ui.cases

import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.provider.DateProvider
import sgtmelon.scriptum.parent.ui.dialogs.NoteDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.notes.startNotesItemTest

/**
 * Parent class for tests of [NoteDialogUi] inside [MainPage.NOTES].
 */
abstract class NotesNoteDialogCase(private val type: NoteType) : ParentUiTest(),
    DialogCloseCase {

    abstract fun insert(): NoteItem

    abstract fun insert(entity: NoteEntity): NoteItem

    override fun close() = startNotesItemTest(insert()) {
        openNoteDialog(it) { softClose() }
        assert(isEmpty = false)
    }

    open fun untitled() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(name = "")
            NoteType.ROLL -> db.rollNote.copy(name = "")
        }

        startNotesItemTest(insert(entity)) {
            openNoteDialog(it)
        }
    }

    open fun notification() = startNotesItemTest(insert()) {
        openNoteDialog(it) { notification { applyDate { set(addMin = 2).applyTime() } } }
        assertItem(it)
    }

    open fun bind() = startNotesItemTest(insert()) {
        openNoteDialog(it) { bind() }
        assertItem(it)
    }

    open fun unbind() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(isStatus = true)
            NoteType.ROLL -> db.rollNote.copy(isStatus = true)
        }

        startNotesItemTest(insert(entity)) {
            openNoteDialog(it) { bind() }
            assertItem(it)
        }
    }

    @Test fun unbindOnDelete() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(isStatus = true)
            NoteType.ROLL -> db.rollNote.copy(isStatus = true)
        }
        val item = insert(entity)

        launch {
            mainScreen {
                openNotes { openNoteDialog(item) { delete() } }
                openBin { openNoteDialog(item) { restore() } }
                openNotes { assertItem(item) }
            }
        }
    }

    @Test fun convert() {
        val firstEntity = when (type) {
            NoteType.TEXT -> db.textNote.copy(change = DateProvider.DATE_2)
            NoteType.ROLL -> db.rollNote.copy(change = DateProvider.DATE_2)
        }

        insert(firstEntity)

        val secondEntity = when (type) {
            NoteType.TEXT -> db.textNote.copy(change = DateProvider.DATE_1)
            NoteType.ROLL -> db.rollNote.copy(change = DateProvider.DATE_1)
        }

        val item = insert(secondEntity)
        startNotesItemTest(item) {
            var convertItem: NoteItem? = null
            openNoteDialog(it, p = 1) { convertItem = convert() }
            assertItem(convertItem!!, p = 0)
        }
    }

    open fun copy() {
        TODO()

        startNotesItemTest(insert()) {
            openNoteDialog(it) { copy() }
        }
    }

    open fun delete() = insert().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { delete() }
                    assert(isEmpty = true)
                }
                openBin()
            }
        }
    }
}