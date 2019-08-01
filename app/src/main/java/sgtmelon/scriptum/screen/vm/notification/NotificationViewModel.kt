package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.room.RoomRepo
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
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

    private val iAlarmRepo = AlarmRepo.getInstance(context)

    private val notificationList: MutableList<NotificationItem> = ArrayList()

    // TODO #RELEASE
    private val repo = RoomRepo.getInstance(context)
    private val list: MutableList<NoteModel> = ArrayList()

    override fun onSetup() {
        callback?.apply {
            setupToolbar()
            setupRecycler(iPreferenceRepo.theme)
        }

        // TODO #RELEASE
        list.addAll(repo.getNoteModelList(bin = false))
        if (list.isNotEmpty()) {
            list.random().noteEntity.let {
                callback?.startActivity(AlarmActivity.getInstance(context, it.id, it.color))
            }
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