package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.parent.situation.IDateTimeDialogTest
import sgtmelon.scriptum.cleanup.ui.screen.main.NotesScreen
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test dateTime dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDateTimeDialogTest : ParentUiTest(), IDateTimeDialogTest {

    private fun runTest(func: NotesScreen.() -> Unit) = launch {
        mainScreen { openNotes(func = func) }
    }

    @Test override fun dateReset() = db.insertNotification(db.insertNote()).let {
        runTest { openNoteDialog(it) { onNotification { runDateReset() } } }
    }

    @Test override fun toastToday() = db.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runToastToday() } } }
    }

    @Test override fun toastOther() = db.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runToastOther() } } }
    }

    @Test override fun timeApplyEnablePast() = db.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runTimeApplyEnablePast() } } }
    }

    @Test override fun timeApplyEnableList() = db.insertNote().let {
        runTest {
            val date = db.insertNotification().alarmDate
            openNoteDialog(it) { onNotification { runTimeApplyEnableList(date) } }
        }
    }
}