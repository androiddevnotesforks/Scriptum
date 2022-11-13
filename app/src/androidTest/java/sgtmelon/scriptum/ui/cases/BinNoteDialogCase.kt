package sgtmelon.scriptum.ui.cases

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

    override fun close() = startBinItemTest(insertToBin()) {
        openNoteDialog(it) { softClose() }
        assert(isEmpty = false)
    }

    open fun untitled() = startBinItemTest(
        when (type) {
            NoteType.TEXT -> db.insertTextToBin(db.textNote.copy(name = ""))
            NoteType.ROLL -> db.insertRollToBin(db.rollNote.copy(name = ""))
        }
    ) {
        openNoteDialog(it)
    }

    open fun restore() = insertToBin().let {
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
    }

    open fun clear() = insertToBin().let {
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

    private fun insertToBin(): NoteItem {
        return when (type) {
            NoteType.TEXT -> db.insertTextToBin()
            NoteType.ROLL -> db.insertRollToBin()
        }
    }
}