package sgtmelon.scriptum.interactor.notification

import android.content.Context

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationBridge
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Interactor for [NotificationViewModel]
 */
class NotificationInteractor(context: Context, private var callback: INotificationBridge?) :
        ParentInteractor(context),
        INotificationInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iAlarmRepo.getList()

    override suspend fun cancelNotification(item: NotificationItem) {
        val id = item.note.id

        iAlarmRepo.delete(id)
        callback?.cancelAlarm(id)
    }

}