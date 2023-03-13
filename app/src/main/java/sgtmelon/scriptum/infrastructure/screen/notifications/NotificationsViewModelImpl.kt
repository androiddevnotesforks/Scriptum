package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.GetNoteUseCase
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.notifications.state.UndoState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull

class NotificationsViewModelImpl(
    override val list: ListStorageImpl<NotificationItem>,
    private val getList: GetNotificationListUseCase,
    private val getNote: GetNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase
) : ViewModel(),
    NotificationsViewModel {

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

    init {
        viewModelScope.launchBack {
            list.change { it.clearAdd(getList()) }
        }
    }

    override fun removeItem(position: Int) = flowOnBack {
        val item = list.change(UpdateListState.Remove(position)) {
            it.removeAtOrNull(position) ?: return@flowOnBack
        }

        /** Save item for snackbar undo action and display it. */
        undoList.add(Pair(position, item))
        showSnackbar.postValue(true)

        deleteNotification(item)

        emit(value = item to list.localData.size)
    }

    override fun undoRemove(): Flow<UndoState> = flowOnBack {
        if (undoList.isEmpty()) return@flowOnBack

        val pair = undoList.removeAtOrNull(index = undoList.lastIndex) ?: return@flowOnBack
        val item = pair.second

        /** Need set list value on mainThread for prevent postValue overriding. */
        val position = list.changeNext {
            /** Check item position correct, just in case. */
            val isCorrect = pair.first in it.indices
            /** List size after adding item, will be last index. */
            val position = if (isCorrect) pair.first else it.size
            it.add(position, item)

            list.update = UpdateListState.chooseInsert(it.size, position)

            return@changeNext position
        }

        emit(UndoState.NotifyInfoCount(list.localData.size))

        /** Show/hide snackbar for next item. */
        showSnackbar.postValue(undoList.isNotEmpty())

        /** Need set list value on mainThread for prevent postValue overriding. */
        val newItem = list.changeNext(UpdateListState.Set) {
            /** After insert need update item in list (due to new item id). */
            val newItem = setNotification(item) ?: return@changeNext null
            it[position] = newItem

            return@changeNext newItem
        } ?: return@flowOnBack

        val calendar = newItem.alarm.date.toCalendar()
        emit(UndoState.NotifyAlarm(newItem.note.id, calendar))
    }

    override fun clearUndoStack() {
        undoList.clear()
        showSnackbar.postValue(false)
    }

    override fun getNote(item: NotificationItem): Flow<NoteItem> = flowOnBack {
        val noteItem = getNote(item.note.id) ?: return@flowOnBack
        emit(noteItem)
    }
}