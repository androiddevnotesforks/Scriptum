package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel

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
    val itemList: MutableList<NotificationItem> = ArrayList()

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

                itemList.clearAddAll(interactor.getList())
            }

            updateList()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(item = itemList.getOrNull(p) ?: return)
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return

        viewModelScope.launch { interactor.cancelNotification(item) }

        callback?.apply {
            notifyInfoBind(itemList.size)
            notifyItemRemoved(itemList, p)
        }
    }

}