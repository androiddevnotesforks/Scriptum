package sgtmelon.scriptum.tests.ui.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.cases.dialog.DateTimeDialogCase
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.time.TimeDialogUi
import sgtmelon.scriptum.source.ui.screen.main.NotesScreen
import sgtmelon.scriptum.source.ui.tests.launchNotesItem

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
        assertClose(it) { softClose() }
        assertClose(it) { cancel() }
        assertClose(it) { applyDate { softClose() } }
        assertClose(it) { applyDate { cancel() } }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun NotesScreen.assertClose(item: NoteItem, closeDialog: DateDialogUi.() -> Unit) {
        openNoteDialog(item) { notification { closeDialog() } }
        assert(isEmpty = false)
    }

    @Test override fun dateReset() = super.dateReset()

    @Test override fun toastToday() = super.toastToday()

    @Test override fun toastOther() = super.toastOther()

    @Test override fun timeApplyEnablePast() = super.timeApplyEnablePast()

    @Test override fun timeApplyEnableList() = super.timeApplyEnableList()

    @Test override fun rotateClose() = launchNotesItem(db.insertNote()) {
        fun assertDateRotationClose(onClose: DateDialogUi.() -> Unit) {
            assertRotationClose(it) {
                rotate.switch()
                assert()
                onClose()
            }
        }

        assertDateRotationClose { softClose() }
        assertDateRotationClose { cancel() }

        fun assertTimeRotationClose(onClose: TimeDialogUi.() -> Unit) {
            assertRotationClose(it) {
                applyDate {
                    rotate.switch()
                    assert()
                    onClose()
                }
            }
        }

        assertTimeRotationClose { softClose() }
        assertTimeRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private inline fun NotesScreen.assertRotationClose(
        item: NoteItem,
        crossinline closeDialog: DateDialogUi.() -> Unit
    ) {
        openNoteDialog(item) { notification { closeDialog() } }
        assert(isEmpty = false)
    }

    @Test override fun rotateWork() = super.rotateWork()

    @Test override fun rotateReset() = super.rotateReset()

}