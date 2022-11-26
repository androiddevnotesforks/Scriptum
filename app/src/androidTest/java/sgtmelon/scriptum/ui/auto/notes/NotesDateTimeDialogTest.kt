package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.tests.launchNotesItem
import sgtmelon.scriptum.ui.cases.dialog.DateTimeDialogCase

/**
 * Test dateTime dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotesDateTimeDialogTest : DateTimeDialogCase<NoteItem>() {

    override fun insert(): NoteItem = db.insertNote()

    override fun insertNotification(): NoteItem = db.insertNotification()

    override fun launchDateDialog(item: NoteItem, func: DateDialogUi.() -> Unit) {
        launchNotesItem(item) { openNoteDialog(it) { notification(func) } }
    }

    @Test override fun close() = launchNotesItem(db.insertNote()) {
        openNoteDialog(it) { notification { softClose() } }
        assert(isEmpty = false)
        openNoteDialog(it) { notification { cancel() } }
        assert(isEmpty = false)
        openNoteDialog(it) { notification { applyDate { softClose() } } }
        assert(isEmpty = false)
        openNoteDialog(it) { notification { applyDate { cancel() } } }
        assert(isEmpty = false)
    }

    @Test override fun dateReset() = super.dateReset()

    @Test override fun toastToday() = super.toastToday()

    @Test override fun toastOther() = super.toastOther()

    @Test override fun timeApplyEnablePast() = super.timeApplyEnablePast()

    @Test override fun timeApplyEnableList() = super.timeApplyEnableList()

}