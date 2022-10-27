package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.onBack
import sgtmelon.extensions.runMain
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.validRemoveAt
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.test.idling.getIdling

class NotificationViewModelImpl(
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getList: GetNotificationListUseCase
) : ViewModel(),
    NotificationViewModel {

    init {
        viewModelScope.launchBack { updateList() }
    }

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

    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

    private suspend fun updateList() {
        getIdling().start(IdlingTag.Notification.LOAD_DATA)

        showList.postValue(ShowListState.Loading)
        _itemList.clearAdd(getList())
        itemList.postValue(_itemList)
        notifyShowList()

        getIdling().stop(IdlingTag.Notification.LOAD_DATA)
    }

    override fun removeNotification(p: Int) = flow {
        val item = _itemList.validRemoveAt(p) ?: return@flow

        /** Save item for snackbar undo action. */
        undoList.add(Pair(p, item))
        showSnackbar.postValue(true)

        updateList = UpdateListState.Removed(p)
        itemList.postValue(_itemList)
        notifyShowList()

        deleteNotification(item)

        emit(value = item to _itemList.size)
    }.onBack()

    override fun undoRemove(): Flow<UndoState> = flow {
        if (undoList.isEmpty()) return@flow

        val pair = undoList.validRemoveAt(index = undoList.lastIndex) ?: return@flow
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
            UpdateListState.SkipInsert
        } else {
            UpdateListState.Insert(position)
        }

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }
        notifyShowList()

        /** Show/hide snackbar for next item. */
        showSnackbar.postValue(undoList.isNotEmpty())

        /** After insert need update item in list (due to new item id). */
        val newItem = setNotification(item) ?: return@flow
        _itemList[position] = newItem
        updateList = UpdateListState.Set

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }

        val calendar = newItem.alarm.date.toCalendar()
        emit(UndoState.NotifyAlarm(newItem.note.id, calendar))
    }.onBack()

    override fun clearUndoStack() {
        undoList.clear()
        showSnackbar.postValue(false)
    }
}