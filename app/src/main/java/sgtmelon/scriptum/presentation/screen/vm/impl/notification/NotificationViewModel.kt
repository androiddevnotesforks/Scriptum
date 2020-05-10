package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAddAll
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
    val itemList: MutableList<NotificationItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        val theme = interactor.theme ?: return

        callback?.setupToolbar()
        callback?.setupRecycler(theme)
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
            val count = interactor.getCount() ?: return@launch
            
            if (count == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.showProgress()
                }

                interactor.getList()?.let { itemList.clearAddAll(it) } ?: return@launch
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