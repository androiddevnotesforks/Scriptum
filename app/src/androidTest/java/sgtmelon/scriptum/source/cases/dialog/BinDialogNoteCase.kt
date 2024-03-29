package sgtmelon.scriptum.source.cases.dialog

import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.ui.screen.dialogs.NoteDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchBinItem
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Parent class for tests of [NoteDialogUi] inside [MainPage.BIN].
 */
abstract class BinDialogNoteCase(private val type: NoteType) : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    abstract fun insert(): NoteItem

    abstract fun insert(entity: NoteEntity): NoteItem

    override fun close() = launchBinItem(insert()) {
        openNoteDialog(it) { softClose() }
        assert(isEmpty = false)
    }

    open fun untitled() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(name = "")
            NoteType.ROLL -> db.rollNote.copy(name = "")
        }

        launchBinItem(insert(entity)) {
            openNoteDialog(it)
        }
    }

    open fun restore() = insert().let {
        launchMain {
            openNotes(isEmpty = true)
            openBin {
                openNoteDialog(it) { restore() }
                assert(isEmpty = true)
            }
            openNotes()
        }
    }

    open fun todo_copy() {
        TODO("Finish")

        launchBinItem(insert()) {
            openNoteDialog(it) { copy() }
        }
    }

    open fun clear() = insert().let {
        launchMain {
            openBin {
                openNoteDialog(it) { clear() }
                assert(isEmpty = true)
            }
            openNotes(isEmpty = true)
        }
    }

    override fun rotateClose() = launchBinItem(insert()) {
        openNoteDialog(it) {
            rotate.switch()
            assert()
            softClose()
        }
        assert(isEmpty = false)
    }

    override fun rotateWork() = launchBinItem(insert()) {
        openNoteDialog(it) {
            rotate.switch()
            /** Just check click listeners work fine. Doesn't matter what action to do. */
            clear()
        }
        assert(isEmpty = true)
    }
}