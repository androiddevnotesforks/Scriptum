package sgtmelon.scriptum.source.cases.dialog

import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.provider.DateProvider
import sgtmelon.scriptum.source.ui.screen.dialogs.NoteDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchNotesItem

/**
 * Parent class for tests of [NoteDialogUi] inside [MainPage.NOTES].
 */
abstract class NotesDialogNoteCase(private val type: NoteType) : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    abstract fun insert(): NoteItem

    abstract fun insert(entity: NoteEntity): NoteItem

    override fun close() = launchNotesItem(insert()) {
        openNoteDialog(it) { softClose() }
        assert(isEmpty = false)
    }

    open fun untitled() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(name = "")
            NoteType.ROLL -> db.rollNote.copy(name = "")
        }

        launchNotesItem(insert(entity)) {
            openNoteDialog(it)
        }
    }

    open fun notification() = launchNotesItem(insert()) {
        openNoteDialog(it) { notification { applyDate { set(addMin = 2).applyTime() } } }
        assertItem(it)
    }

    open fun bind() = launchNotesItem(insert()) {
        openNoteDialog(it) { bind() }
        assertItem(it)
    }

    open fun unbind() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(isStatus = true)
            NoteType.ROLL -> db.rollNote.copy(isStatus = true)
        }

        launchNotesItem(insert(entity)) {
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

        launchMain {
            openNotes { openNoteDialog(item) { delete() } }
            openBin { openNoteDialog(item) { restore() } }
            openNotes { assertItem(item) }
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
        launchNotesItem(item) {
            var convertItem: NoteItem? = null
            openNoteDialog(it, p = 1) { convertItem = convert() }
            assertItem(convertItem!!, p = 0)
        }
    }

    open fun todo_copy() {
        TODO()

        launchNotesItem(insert()) {
            openNoteDialog(it) { copy() }
        }
    }

    open fun delete() = insert().let {
        launchMain {
            openNotes {
                openNoteDialog(it) { delete() }
                assert(isEmpty = true)
            }
            openBin()
        }
    }

    override fun rotateClose() = launchNotesItem(insert()) {
        openNoteDialog(it) {
            rotate.switch()
            assert()
            softClose()
        }
        assert(isEmpty = false)
    }

    override fun rotateWork() = launchNotesItem(insert()) {
        openNoteDialog(it) {
            rotate.switch()
            /** Just check click listeners work fine. Doesn't matter what action to do. */
            delete()
        }
        assert(isEmpty = true)
    }
}