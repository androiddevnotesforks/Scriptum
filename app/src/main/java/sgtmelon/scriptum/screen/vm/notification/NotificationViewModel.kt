package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.interactor.notification.INotificationInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel

/**
 * ViewModel for [NotificationActivity]
 */
class NotificationViewModel(application: Application) :
        ParentViewModel<INotificationActivity>(application),
        INotificationViewModel {

    private val iInteractor: INotificationInteractor = NotificationInteractor(context)
    private val itemList: MutableList<NotificationItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
        }
    }


    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            bind()
        }
    }

    override fun onClickNote(p: Int) {
        val item = itemList[p].note
        callback?.startActivity(NoteActivity.getInstance(context, item.type, item.id))
    }

    override fun onClickCancel(p: Int) {
        itemList[p].let { viewModelScope.launch { iInteractor.cancelNotification(it, callback) } }

        callback?.notifyItemRemoved(p, itemList.apply { removeAt(p) })
    }

}