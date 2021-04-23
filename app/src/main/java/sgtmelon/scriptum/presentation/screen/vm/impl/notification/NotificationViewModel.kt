package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData.Snackbar
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [INotificationActivity].
 */
class NotificationViewModel(application: Application) :
        ParentViewModel<INotificationActivity>(application),
        INotificationViewModel {

    private lateinit var interactor: INotificationInteractor

    fun setInteractor(interactor: INotificationInteractor) {
        this.interactor = interactor
    }


    @RunPrivate val itemList: MutableList<NotificationItem> = mutableListOf()
    @RunPrivate val cancelList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupInsets()

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

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


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
        AppIdlingResource.getInstance().startWork(IdlingTag.Notification.LOAD_DATA)

        callback?.beforeLoad()

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
                    callback?.showProgress()
                }

                runBack { itemList.clearAdd(interactor.getList()) }
            }

            updateList()

            AppIdlingResource.getInstance().stopWork(IdlingTag.Notification.LOAD_DATA)
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

        // TODO if cancel list is too big (eg 10 items) need remove first

        viewModelScope.launchBack { interactor.cancelNotification(item) }

        callback?.apply {
            notifyInfoBind(itemList.size)
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
            notifyInfoBind(itemList.size)
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

        /**
         * After insert need update item in list (due to new item id).
         */
        viewModelScope.launch {
            itemList[position] = runBack {
                interactor.setNotification(item)
            } ?: return@launch

            callback?.setList(itemList)
        }
    }

    override fun onSnackbarDismiss() = cancelList.clear()

}