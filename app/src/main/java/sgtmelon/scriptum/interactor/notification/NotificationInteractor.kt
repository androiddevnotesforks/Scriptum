package sgtmelon.scriptum.interactor.notification

import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.IAlarmBridge
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Interactor for [NotificationViewModel]
 */
class NotificationInteractor(context: Context) : ParentInteractor(context),
        INotificationInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iAlarmRepo.getList()

    override suspend fun cancelNotification(item: NotificationItem,
                                            callback: IAlarmBridge.Cancel?) {
        iAlarmRepo.delete(item.note.id)
        callback?.cancelAlarm(AlarmReceiver[item])
    }

}