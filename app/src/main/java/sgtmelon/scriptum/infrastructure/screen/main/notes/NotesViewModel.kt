package sgtmelon.scriptum.infrastructure.screen.main.notes

import androidx.lifecycle.LiveData
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

interface NotesViewModel : UnbindNoteReceiver.Callback {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<NoteItem>>

    fun updateData()

    fun getNotificationData(p: Int): Flow<Pair<Calendar, Boolean>>

    fun getExistDateList(): Flow<List<String>>

    fun deleteNotification(p: Int): Flow<NoteItem>

    fun setNotification(calendar: Calendar, p: Int): Flow<Pair<NoteItem, Calendar>>

    // TODO menu bind

    // TODO menu convert

    fun getCopyText(p: Int): Flow<String>

    // TODO remove
    //    fun onUpdateData()
    //
    //    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    //    fun onShowOptionsDialog(item: NoteItem, p: Int)
    //
    //
    //    fun onResultOptionsDialog(p: Int, @Options.Notes which: Int)
    //
    //    fun onResultDateDialog(calendar: Calendar, p: Int)
    //
    //    fun onResultDateDialogClear(p: Int)
    //
    //    fun onResultTimeDialog(calendar: Calendar, p: Int)

}