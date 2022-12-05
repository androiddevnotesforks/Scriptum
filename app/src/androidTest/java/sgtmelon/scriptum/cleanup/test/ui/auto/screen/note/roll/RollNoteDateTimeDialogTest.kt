package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.test.parent.situation.DateTimeDialogCase
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

@RunWith(AndroidJUnit4::class)
class RollNoteDateTimeDialogTest : ParentUiTest(), DateTimeDialogCase {

    private fun runTest(item: NoteItem.Roll, func: RollNoteScreen.() -> Unit) = launchSplash {
        mainScreen { openNotes { openRoll(item, func = func) } }
    }

    @Test override fun dateReset() = db.insertNotification(db.insertRoll()).let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        runTest(it) { controlPanel { onNotification(isUpdateDate = true) { runDateReset() } } }
    }

    @Test override fun toastToday() = db.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runToastToday() } } }
    }

    @Test override fun toastOther() = db.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runToastOther() } } }
    }

    @Test override fun timeApplyEnablePast() = db.insertRoll().let {
        runTest(it) { controlPanel { onNotification { runTimeApplyEnablePast() } } }
    }

    @Test override fun timeApplyEnableList() = db.insertRoll().let {
        runTest(it) {
            val date = db.insertNotification().alarmDate
            controlPanel { onNotification { runTimeApplyEnableList(date) } }
        }
    }
}