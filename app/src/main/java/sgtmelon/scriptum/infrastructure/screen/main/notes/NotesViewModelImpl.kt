package sgtmelon.scriptum.infrastructure.screen.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
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
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull
import java.util.Calendar

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
    private val getNotificationsDateList: GetNotificationsDateListUseCase
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

    override fun getNoteNotification(p: Int): Flow<Pair<Calendar, Boolean>> = flowBack {
        val item = list.localData.getOrNull(p) ?: return@flowBack
        emit(value = item.alarm.date.toCalendar() to item.haveAlarm)
    }

    override val notificationsDateList: Flow<List<String>>
        get() = flowBack { emit(getNotificationsDateList()) }

    override fun deleteNoteNotification(p: Int): Flow<NoteItem> = flowBack {
        val item = list.change { it.getOrNull(p)?.clearAlarm() ?: return@flowBack }

        deleteNotification(item)
        emit(item)
    }

    override fun setNoteNotification(
        calendar: Calendar,
        p: Int
    ): Flow<Pair<NoteItem, Calendar>> = flowBack {
        if (calendar.isBeforeNow) return@flowBack

        val item = list.change {
            val item = it.getOrNull(p) ?: return@flowBack

            /** Inside will be updated data about alarm. */
            setNotification(item, calendar)

            return@change item
        }

        emit(value = item to calendar)
    }

    override fun updateNoteBind(p: Int): Flow<NoteItem> = flowBack {
        val item = list.change { it.getOrNull(p)?.switchStatus() ?: return@flowBack }

        updateNote(item)
        emit(item)
    }

    override fun convertNote(p: Int): Flow<NoteItem> = flowBack {
        val newItem = list.change {
            val item = it.getOrNull(p) ?: return@flowBack
            val newItem = convertNote(item)

            /** Sort list without new call to dataBase. */
            it[p] = newItem
            it.clearAdd(sortList(it, preferencesRepo.sort))

            return@change newItem
        }

        emit(newItem)
    }

    override fun getNoteText(p: Int): Flow<String> = flowBack {
        val item = list.localData.getOrNull(p) ?: return@flowBack
        emit(getCopyText(item))
    }

    override fun deleteNote(p: Int): Flow<NoteItem> = flowBack {
        val item = list.change { it.removeAtOrNull(p) ?: return@flowBack }

        deleteNote(item)
        emit(item)
    }

    override fun onReceiveUnbindNote(noteId: Long) = list.change {
        val item = it.firstOrNull { item -> item.id == noteId } ?: return@change
        item.isStatus = false
    }

    override fun onReceiveInfoChange(state: ShowListState) = list.notifyShow(state)
}