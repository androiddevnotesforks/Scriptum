package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.test.IDateTimeDialogTest
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen

@RunWith(AndroidJUnit4::class)
class RollNoteDateTimeDialogTest : ParentUiTest(), IDateTimeDialogTest {

    private fun runTest(item: NoteItem.Roll, func: RollNoteScreen.() -> Unit) = launch {
        mainScreen { notesScreen { openRollNote(item, func = func) } }
    }

    @Test override fun dateReset() = data.insertNotification(data.insertRoll()).let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        runTest(it) { controlPanel { onNotification(isUpdateDate = true) { runDateReset() } } }
    }

    @Test override fun toastToday() = data.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runToastToday() } } }
    }

    @Test override fun toastOther() = data.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runToastOther() } } }
    }

    @Test override fun timeApplyEnablePast() = data.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runTimeApplyEnablePast() } } }
    }

    @Test override fun timeApplyEnableList() = data.insertRoll().let {
        runTest(it) {
            val date = data.insertNotification().alarmDate
            controlPanel { onNotification { runTimeApplyEnableList(date) } }
        }
    }
}