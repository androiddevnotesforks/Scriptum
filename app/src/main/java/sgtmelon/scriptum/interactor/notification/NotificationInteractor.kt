package sgtmelon.scriptum.interactor.notification

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.AlarmRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.BindRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationBridge
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Interactor for [NotificationViewModel].
 */
class NotificationInteractor(
        private val iPreferenceRepo: IPreferenceRepo,
        private val iAlarmRepo: IAlarmRepo,
        private val iBindRepo: IBindRepo,
        private var callback: INotificationBridge?
) : ParentInteractor(),
        INotificationInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override suspend fun getCount() = iBindRepo.getNotificationCount()

    override suspend fun getList() = iAlarmRepo.getList()

    override suspend fun cancelNotification(item: NotificationItem) {
        val id = item.note.id

        iAlarmRepo.delete(id)
        callback?.cancelAlarm(id)
    }

}