package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.screen.callback.notification.NotificationCallback
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

    private val iAlarmRepo = AlarmRepo.getInstance(context)

    private val notificationList: MutableList<NotificationItem> = ArrayList()

    fun onSetup() = callback.apply {
        setupToolbar()
        setupRecycler(iPreferenceRepo.theme)
    }

    fun onUpdateData() {
        notificationList.clearAndAdd(iAlarmRepo.getList())

        callback.notifyDataSetChanged(notificationList)
        callback.bind()
    }

    fun onClickNote(p: Int) = with(notificationList[p].note) {
        callback.startActivity(context.getNoteIntent(type, id))
    }

    fun onClickCancel(p: Int) {
        iAlarmRepo.delete(notificationList[p].alarm.id)
        callback.notifyItemRemoved(p, notificationList.apply { removeAt(p) })
    }

}