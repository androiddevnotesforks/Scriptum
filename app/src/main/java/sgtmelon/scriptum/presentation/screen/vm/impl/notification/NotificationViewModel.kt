package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [NotificationActivity].
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
        callback?.setupRecycler(interactor.theme)
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    /**
     * Get count before load all data because it's faster.
     */
    override fun onUpdateData() {
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
            if (interactor.getCount() == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.showProgress()
                }

                itemList.clearAdd(interactor.getList())
            }

            updateList()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(item = itemList.getOrNull(p) ?: return)
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return

        /**
         * Save item for snackbar undo action.
         */
        cancelList.add(Pair(p, item))

        viewModelScope.launch { interactor.cancelNotification(item) }

        callback?.apply {
            notifyInfoBind(itemList.size)
            notifyItemRemoved(itemList, p)
            showSnackbar(interactor.theme)
        }
    }


    override fun onSnackbarAction() {
        if (cancelList.isEmpty()) return

        val pair = cancelList.removeAtOrNull(index = cancelList.lastIndex) ?: return
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
             * Show snackbar for next item undo.
             */
            if (cancelList.isNotEmpty()) {
                showSnackbar(interactor.theme)
            }
        }

        /**
         * After insert need update item in list (due to new item id).
         */
        viewModelScope.launch {
            itemList[position] = interactor.setNotification(item) ?: return@launch
            callback?.setList(itemList)
        }
    }

    override fun onSnackbarDismiss() = cancelList.clear()

}