package sgtmelon.scriptum.test.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IDateTimeDialogTest
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

@RunWith(AndroidJUnit4::class)
class TextNoteDateTimeDialogTest : ParentUiTest(), IDateTimeDialogTest {

    private fun runTest(item: NoteItem.Text, func: TextNoteScreen.() -> Unit) = launch {
        mainScreen { notesScreen { openTextNote(item, func = func) } }
    }

    @Test override fun dateReset() = data.insertNotification(data.insertText()).let {
        if (it !is NoteItem.Text) throw NoteCastException()

        runTest(it) { controlPanel { onNotification(isUpdateDate = true) { runDateReset() } } }
    }

    @Test override fun toastToday() = data.insertText().let {
        runTest(it) { controlPanel { onNotification { runToastToday() } } }
    }

    @Test override fun toastOther() = data.insertText().let {
        runTest(it) { controlPanel { onNotification { runToastOther() } } }
    }

    @Test override fun timeApplyEnablePast() = data.insertText().let {
        runTest(it) { controlPanel { onNotification { runTimeApplyEnablePast() } } }
    }

    @Test override fun timeApplyEnableList() = data.insertText().let {
        runTest(it) {
            val date = data.insertNotification().alarmDate
            controlPanel { onNotification { runTimeApplyEnableList(date) } }
        }
    }
}