package sgtmelon.scriptum.infrastructure.screen.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus

class NotesViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    override val list: ListStorageImpl<NoteItem>,
    private val getList: GetNotesListUseCase,
    private val sortList: SortNoteListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationDateList: GetNotificationsDateListUseCase
) : ViewModel(),
    NotesViewModel {

    override val isListHide: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateData() {
        viewModelScope.launchBack {
            list.change {
                val (itemList, isHide) = getList()
                isListHide.postValue(isHide)
                it.clearAdd(itemList)
            }
        }
    }

    override fun getNoteNotification(p: Int): Flow<Pair<Calendar, Boolean>> = flowOnBack {
        val item = list.localData.getOrNull(p) ?: return@flowOnBack
        emit(value = item.alarm.date.toCalendar() to item.haveAlarm)
    }

    override fun getOccupiedDateList(): Flow<List<String>> = flowOnBack {
        emit(getNotificationDateList())
    }

    override fun deleteNoteNotification(p: Int): Flow<NoteItem> = flowOnBack {
        val item = list.change { it.getOrNull(p)?.clearAlarm() ?: return@flowOnBack }

        deleteNotification(item)
        emit(item)
    }

    override fun setNoteNotification(
        calendar: Calendar,
        p: Int
    ): Flow<Pair<NoteItem, Calendar>> = flowOnBack {
        if (calendar.isBeforeNow()) return@flowOnBack

        val item = list.change {
            val item = it.getOrNull(p) ?: return@flowOnBack

            /** Inside will be updated data about alarm. */
            setNotification(item, calendar)

            return@change item
        }

        emit(value = item to calendar)
    }

    override fun updateNoteBind(p: Int): Flow<NoteItem> = flowOnBack {
        val item = list.change { it.getOrNull(p)?.switchStatus() ?: return@flowOnBack }

        updateNote(item)
        emit(item)
    }

    override fun convertNote(p: Int): Flow<NoteItem> = flowOnBack {
        val newItem = list.change {
            val item = it.getOrNull(p) ?: return@flowOnBack
            val newItem = convertNote(item)

            /** Sort list without new call to dataBase. */
            it[p] = newItem
            it.clearAdd(sortList(it, preferencesRepo.sort))

            return@change newItem
        }

        emit(newItem)
    }

    override fun getNoteText(p: Int): Flow<String> = flowOnBack {
        val item = list.localData.getOrNull(p) ?: return@flowOnBack
        emit(getCopyText(item))
    }

    override fun deleteNote(p: Int): Flow<NoteItem> = flowOnBack {
        val item = list.change { it.removeAtOrNull(p) ?: return@flowOnBack }

        deleteNote(item)
        emit(item)
    }

    override fun onReceiveUnbindNote(noteId: Long) = list.change {
        val item = it.firstOrNull { item -> item.id == noteId } ?: return@change
        item.isStatus = false
    }
}