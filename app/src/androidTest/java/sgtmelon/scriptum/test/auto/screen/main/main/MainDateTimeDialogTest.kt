package sgtmelon.scriptum.test.auto.screen.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IDateTimeDialogTest
import sgtmelon.scriptum.ui.screen.main.NotesScreen

/**
 * Test dateTime dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDateTimeDialogTest : ParentUiTest(), IDateTimeDialogTest {

    private fun runTest(func: NotesScreen.() -> Unit) = launch {
        mainScreen { notesScreen(func = func) }
    }

    @Test override fun dateReset() = data.insertNotification(data.insertNote()).let {
        runTest { openNoteDialog(it) { onNotification { runDateReset() } } }
    }

    @Test override fun toastToday() = data.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runToastToday() } } }
    }

    @Test override fun toastOther() = data.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runToastOther() } } }
    }

    @Test override fun timeApplyEnablePast() = data.insertNote().let {
        runTest { openNoteDialog(it) { onNotification { runTimeApplyEnablePast() } } }
    }

    @Test override fun timeApplyEnableList() = data.insertNote().let {
        runTest {
            val date = data.insertNotification().alarmDate
            openNoteDialog(it) { onNotification { runTimeApplyEnableList(date) } }
        }
    }
}