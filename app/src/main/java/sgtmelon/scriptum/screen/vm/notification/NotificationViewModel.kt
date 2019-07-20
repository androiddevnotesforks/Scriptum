package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.screen.view.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel for [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) :
        ParentViewModel<INotificationActivity>(application),
        INotificationViewModel {

    private val iAlarmRepo = AlarmRepo.getInstance(context)

    private val notificationList: MutableList<NotificationItem> = ArrayList()

    override fun onSetup() {
        callback?.apply {
            setupToolbar()
            setupRecycler(iPreferenceRepo.theme)
        }
    }

    override fun onUpdateData() {
        notificationList.clearAndAdd(iAlarmRepo.getList())

        callback?.apply {
            notifyDataSetChanged(notificationList)
            bind()
        }
    }

    override fun onClickNote(p: Int) {
        with(notificationList[p].note) {
            callback?.startActivity(context.getNoteIntent(type, id))
        }
    }

    override fun onClickCancel(p: Int) {
        iAlarmRepo.delete(notificationList[p].alarm.id)
        callback?.notifyItemRemoved(p, notificationList.apply { removeAt(p) })
    }

}