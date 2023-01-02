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
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.switchStatus

class NotesViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val getList: GetNotesListUseCase,
    private val sortList: SortNoteListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationDateList: GetNotificationDateListUseCase
) : ViewModel(),
    NotesViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    /**
     * There is no reason check current [showList] state for skip identical values (like it done
     * for [NotificationsViewModelImpl.showList]). Because here we only can remove items from
     * list, without ability to undo this action.
     */
    private fun notifyShowList() {
        showList.postValue(if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List)
    }

    override val isListHide: MutableLiveData<Boolean> = MutableLiveData()

    override val itemList: MutableLiveData<List<NoteItem>> = MutableLiveData()

    /** This list needed because don't want put mutable list inside liveData. */
    private val _itemList: MutableList<NoteItem> = mutableListOf()

    override fun updateData() {
        viewModelScope.launchBack {
            val (list, isHide) = getList()
            isListHide.postValue(isHide)
            _itemList.clearAdd(list)
            itemList.postValue(list)
            notifyShowList()
        }
    }

    override fun getNoteNotification(p: Int): Flow<Pair<Calendar, Boolean>> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack
        emit(value = item.alarm.date.toCalendar() to item.haveAlarm)
    }

    override fun getOccupiedDateList(): Flow<List<String>> = flowOnBack {
        emit(getNotificationDateList())
    }

    override fun deleteNoteNotification(p: Int): Flow<NoteItem> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack

        item.clearAlarm()
        itemList.postValue(_itemList)
        deleteNotification(item)
        emit(item)
    }

    override fun setNoteNotification(
        calendar: Calendar,
        p: Int
    ): Flow<Pair<NoteItem, Calendar>> = flowOnBack {
        if (calendar.isBeforeNow()) return@flowOnBack

        val item = _itemList.getOrNull(p) ?: return@flowOnBack

        setNotification(item, calendar)
        itemList.postValue(_itemList)

        emit(value = item to calendar)
    }

    override fun updateNoteBind(p: Int): Flow<NoteItem> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack

        item.switchStatus()
        itemList.postValue(_itemList)
        updateNote(item)
        emit(item)
    }

    override fun convertNote(p: Int): Flow<NoteItem> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack
        val newItem = convertNote(item)

        /** Sort list without new call to dataBase. */
        _itemList[p] = newItem
        _itemList.clearAdd(sortList(_itemList, preferencesRepo.sort))
        itemList.postValue(_itemList)
        emit(newItem)
    }

    override fun getNoteText(p: Int): Flow<String> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack
        emit(getCopyText(item))
    }

    override fun deleteNote(p: Int): Flow<NoteItem> = flowOnBack {
        val item = _itemList.removeAtOrNull(p) ?: return@flowOnBack

        itemList.postValue(_itemList)
        notifyShowList()

        deleteNote(item)
        emit(item)
    }

    override fun onReceiveUnbindNote(noteId: Long) {
        val p = _itemList.indexOfFirst { it.id == noteId }
        val item = _itemList.getOrNull(p) ?: return

        item.isStatus = false
        itemList.postValue(_itemList)
    }
}