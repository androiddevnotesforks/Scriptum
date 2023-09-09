package sgtmelon.scriptum.infrastructure.screen.main.notes

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListViewModel
import java.util.Calendar

interface NotesViewModel : ListViewModel<NoteItem>,
    UnbindNoteReceiver.Callback,
    InfoChangeReceiver.Callback {

    val isListHide: LiveData<Boolean>

    fun updateData()

    fun getNoteNotification(p: Int): Flow<Pair<Calendar, Boolean>>

    val notificationsDateList: Flow<List<String>>

    fun deleteNoteNotification(p: Int): Flow<NoteItem>

    fun setNoteNotification(calendar: Calendar, p: Int): Flow<Pair<NoteItem, Calendar>>

    fun updateNoteBind(p: Int): Flow<NoteItem>

    fun convertNote(p: Int): Flow<NoteItem>

    fun getNoteText(p: Int): Flow<String>

    fun deleteNote(p: Int): Flow<NoteItem>

}