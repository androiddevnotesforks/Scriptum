package sgtmelon.scriptum.ui.cases

import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ui.dialogs.NoteDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.bin.startBinItemTest

/**
 * Parent class for tests of [NoteDialogUi] inside [MainPage.BIN].
 */
abstract class BinNoteDialogCase(private val type: NoteType) : ParentUiTest(),
    DialogCloseCase {

    abstract fun insert(): NoteItem

    abstract fun insert(entity: NoteEntity): NoteItem

    override fun close() = startBinItemTest(insert()) {
        openNoteDialog(it) { softClose() }
        assert(isEmpty = false)
    }

    open fun untitled() {
        val entity = when (type) {
            NoteType.TEXT -> db.textNote.copy(name = "")
            NoteType.ROLL -> db.rollNote.copy(name = "")
        }

        startBinItemTest(insert(entity)) {
            openNoteDialog(it)
        }
    }

    open fun restore() = insert().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openNoteDialog(it) { restore() }
                    assert(isEmpty = true)
                }
                openNotes()
            }
        }
    }

    open fun copy() {
        TODO()

        startBinItemTest(insert()) {
            openNoteDialog(it) { copy() }
        }
    }

    open fun clear() = insert().let {
        launch {
            mainScreen {
                openBin {
                    openNoteDialog(it) { clear() }
                    assert(isEmpty = true)
                }
                openNotes(isEmpty = true)
            }
        }
    }
}