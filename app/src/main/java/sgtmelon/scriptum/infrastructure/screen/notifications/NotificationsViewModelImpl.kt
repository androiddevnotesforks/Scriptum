package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.utils.clearAdd
import sgtmelon.scriptum.infrastructure.utils.removeAtOrNull
import sgtmelon.test.idling.getIdling

class NotificationsViewModelImpl(
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getList: GetNotificationListUseCase
) : ViewModel(),
    NotificationsViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    private fun notifyShowList() {
        val state = showList.value ?: return
        val newState = if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List

        /** Skip same state. */
        if (state != newState) {
            showList.postValue(newState)
        }
    }

    override val itemList: MutableLiveData<List<NotificationItem>> = MutableLiveData()

    /** This list needed because don't want put mutable list inside liveData. */
    private val _itemList: MutableList<NotificationItem> = mutableListOf()

    /** Variable for specific list updates (update not all items). */
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

    init {
        viewModelScope.launchBack { fetchList() }
    }

    private suspend fun fetchList() {
        getIdling().start(IdlingTag.Notification.LOAD_DATA)

        showList.postValue(ShowListState.Loading)
        _itemList.clearAdd(getList())
        itemList.postValue(_itemList)
        notifyShowList()

        getIdling().stop(IdlingTag.Notification.LOAD_DATA)
    }

    override fun removeNotification(p: Int) = flowOnBack {
        val item = _itemList.removeAtOrNull(p) ?: return@flowOnBack

        /** Save item for snackbar undo action. */
        undoList.add(Pair(p, item))
        showSnackbar.postValue(true)

        updateList = UpdateListState.Remove(p)
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

        /**
         * If list size equals 1 -> need just show list without animation, because of
         * animation glitch.
         */
        updateList = if (_itemList.size == 1) {
            UpdateListState.NotifyHard
        } else {
            UpdateListState.Insert(position)
        }

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