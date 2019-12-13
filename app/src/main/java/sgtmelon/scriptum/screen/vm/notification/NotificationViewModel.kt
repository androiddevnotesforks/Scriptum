package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel

/**
 * ViewModel for [NotificationActivity]
 */
class NotificationViewModel(application: Application) :
        ParentViewModel<INotificationActivity>(application),
        INotificationViewModel {

    private val iInteractor: INotificationInteractor by lazy { NotificationInteractor(context, callback) }
    private val itemList: MutableList<NotificationItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    /**
     * Get count before load all data because it's faster.
     */
    override fun onUpdateData() {
        viewModelScope.launch {
            val count = iInteractor.getCount()

            if (count == 0) {
                itemList.clear()
            } else {
                callback?.showProgress()
                itemList.clearAndAdd(iInteractor.getList())
            }

            callback?.apply {
                notifyList(itemList)
                onBindingList()
            }
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(itemList[p])
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAt(p)

        viewModelScope.launch { iInteractor.cancelNotification(item) }

        callback?.apply {
            notifyInfoBind(itemList.size)
            notifyList(itemList)
        }
    }

}