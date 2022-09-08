package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Snackbar
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.validRemoveAt
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [INotificationActivity].
 */
class NotificationViewModel(
    callback: INotificationActivity,
    private val interactor: INotificationInteractor,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getList: GetNotificationListUseCase
) : ParentViewModel<INotificationActivity>(callback),
        INotificationViewModel {

    @RunPrivate val itemList: MutableList<NotificationItem> = mutableListOf()
    @RunPrivate val cancelList: MutableList<Pair<Int, NotificationItem>> =
        mutableListOf()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupInsets()

        callback?.prepareForLoad()

        if (bundle != null) {
            restoreSnackbar(bundle)
        }
    }

    /**
     * Restore saved snackbar data inside [onSaveData].
     */
    @RunPrivate fun restoreSnackbar(bundle: Bundle) {
        val positionArray = bundle.getIntArray(Snackbar.Intent.POSITIONS) ?: return
        val itemArray = bundle.getStringArray(Snackbar.Intent.ITEMS) ?: return

        /**
         * itemArray.isNotEmpty is implied.
         */
        if (positionArray.isNotEmpty() && positionArray.size == itemArray.size) {
            for (i in positionArray.indices) {
                val p = positionArray.getOrNull(i) ?: continue
                val json = itemArray.getOrNull(i) ?: continue
                val item = NotificationItem[json] ?: continue

                cancelList.add(Pair(p, item))
            }

            callback?.showSnackbar()
        }
    }


    /**
     * Save snackbar data and restore it inside [restoreSnackbar].
     */
    override fun onSaveData(bundle: Bundle) {
        val positionArray = cancelList.map { it.first }.toIntArray()
        val itemArray = cancelList.map { it.second.toJson() }.toTypedArray()

        bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray)
        bundle.putStringArray(Snackbar.Intent.ITEMS, itemArray)

        cancelList.clear()
    }

    /**
     * Get count before load all data because it's faster.
     */
    override fun onUpdateData() {
        getIdling().start(IdlingTag.Notification.LOAD_DATA)

        fun updateList() = callback?.apply {
            notifyList(itemList)
            onBindingList()
        }

        /**
         * If was rotation need show list. After that fetch updates.
         */
        if (itemList.isNotEmpty()) updateList()

        viewModelScope.launch {
            val count = runBack { interactor.getCount() }

            if (count == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.hideEmptyInfo()
                    callback?.showProgress()
                }

                runBack { itemList.clearAdd(getList()) }
            }

            updateList()

            getIdling().stop(IdlingTag.Notification.LOAD_DATA)
        }
    }

    override fun onClickNote(p: Int) {
        callback?.openNoteScreen(item = itemList.getOrNull(p) ?: return)
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        /**
         * Save item for snackbar undo action.
         */
        cancelList.add(Pair(p, item))

        viewModelScope.launch {
            runBack { deleteNotification(item) }

            callback?.sendCancelAlarmBroadcast(item)
            callback?.sendNotifyInfoBroadcast(itemList.size)
        }

        callback?.apply {
            notifyItemRemoved(itemList, p)
            showSnackbar()
        }
    }


    override fun onSnackbarAction() {
        if (cancelList.isEmpty()) return

        val pair = cancelList.validRemoveAt(index = cancelList.lastIndex) ?: return
        val item = pair.second

        /**
         * Check item position correct, just in case.
         * List size after adding item, will be last index.
         */
        val isCorrect = pair.first in itemList.indices
        val position = if (isCorrect) pair.first else itemList.size
        itemList.add(position, item)

        callback?.apply {
            sendNotifyInfoBroadcast(itemList.size)
            notifyItemInsertedScroll(itemList, position)

            /**
             * If list was empty need hide information and show list.
             */
            if (itemList.size == 1) {
                onBindingList()
            }

            /**
             * Show snackbar for next item undo remove.
             */
            if (cancelList.isNotEmpty()) {
                showSnackbar()
            }
        }

        viewModelScope.launch { snackbarActionBackground(item, position) }
    }

    /**
     * After insert need update item in list (due to new item id).
     */
    @RunPrivate suspend fun snackbarActionBackground(
        item: NotificationItem,
        position: Int
    ) {
        val newItem = runBack { setNotification(item) } ?: return

        itemList[position] = newItem
        callback?.setList(itemList)

        val calendar = newItem.alarm.date.toCalendar()
        callback?.sendSetAlarmBroadcast(newItem.note.id, calendar, showToast = false)
    }

    override fun onSnackbarDismiss() = cancelList.clear()

}