package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
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


    @VisibleForTesting
    val itemList: MutableList<NotificationItem> = mutableListOf()

    @VisibleForTesting
    val cancelList: MutableList<Pair<Int, NotificationItem>> = mutableListOf()

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

        val pair = cancelList.removeAtOrNull(index = cancelList.indices.last) ?: return
        val item = pair.second

        val isCorrect = pair.first in itemList.indices
        if (isCorrect) {
            itemList.add(pair.first, item)
        } else {
            itemList.add(item)
        }

        viewModelScope.launch { interactor.setNotification(item) }

        callback?.apply {
            notifyInfoBind(itemList.size)

            val position = if (isCorrect) pair.first else itemList.indices.last
            notifyItemInsertedScroll(itemList, position)

            if (itemList.size == 1) {
                onBindingList()
            }

            if (cancelList.isNotEmpty()) {
                showSnackbar(interactor.theme)
            }
        }
    }

    override fun onSnackbarDismiss() = cancelList.clear()

}