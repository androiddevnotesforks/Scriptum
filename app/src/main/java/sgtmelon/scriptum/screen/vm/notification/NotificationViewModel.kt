package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel

/**
 * ViewModel for [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) :
        ParentViewModel<INotificationActivity>(application),
        INotificationViewModel {

    private val notificationList: MutableList<NotificationItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
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
        val item = notificationList[p]
        iAlarmRepo.delete(item.note.id)

        callback?.notifyItemRemoved(p, notificationList.apply { removeAt(p) })
        callback?.cancelAlarm(AlarmReceiver.getInstance(context, item.note.id, item.note.color))
    }

}