package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runMain
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.notifications.state.UndoState
import sgtmelon.scriptum.infrastructure.screen.parent.list.notify.ListViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull

class NotificationsViewModelImpl(
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getList: GetNotificationListUseCase
) : ListViewModelImpl<NotificationItem>(),
    NotificationsViewModel {

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

    init {
        viewModelScope.launchBack { fetchList() }
    }

    private suspend fun fetchList() {
        showList.postValue(ShowListState.Loading)
        _itemList.clearAdd(getList())
        itemList.postValue(_itemList)
        notifyShowList()
    }

    override fun removeItem(position: Int) = flowOnBack {
        val item = _itemList.removeAtOrNull(position) ?: return@flowOnBack

        /** Save item for snackbar undo action and display it. */
        undoList.add(Pair(position, item))
        showSnackbar.postValue(true)

        updateList = UpdateListState.Remove(position)
        itemList.postValue(_itemList)
        notifyShowList()

        deleteNotification(item)

        emit(value = item to _itemList.size)
    }

    override fun undoRemove(): Flow<UndoState> = flowOnBack {
        if (undoList.isEmpty()) return@flowOnBack

        val pair = undoList.removeAtOrNull(index = undoList.lastIndex) ?: return@flowOnBack
        val item = pair.second

        /**
         * Check item position correct, just in case.
         * List size after adding item, will be last index.
         */
        val isCorrect = pair.first in _itemList.indices
        val position = if (isCorrect) pair.first else _itemList.size
        _itemList.add(position, item)

        emit(UndoState.NotifyInfoCount(_itemList.size))

        updateList = UpdateListState.chooseInsert(_itemList.size, position)

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }
        notifyShowList()

        /** Show/hide snackbar for next item. */
        showSnackbar.postValue(undoList.isNotEmpty())

        /** After insert need update item in list (due to new item id). */
        val newItem = setNotification(item) ?: return@flowOnBack
        _itemList[position] = newItem
        updateList = UpdateListState.Set

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }

        val calendar = newItem.alarm.date.toCalendar()
        emit(UndoState.NotifyAlarm(newItem.note.id, calendar))
    }

    override fun clearUndoStack() {
        undoList.clear()
        showSnackbar.postValue(false)
    }
}