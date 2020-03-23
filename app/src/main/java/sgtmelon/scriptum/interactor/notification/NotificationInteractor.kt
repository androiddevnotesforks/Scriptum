package sgtmelon.scriptum.interactor.notification

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationBridge
import sgtmelon.scriptum.presentation.screen.vm.notification.NotificationViewModel

/**
 * Interactor for [NotificationViewModel].
 */
class NotificationInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val bindRepo: IBindRepo,
        private var callback: INotificationBridge?
) : ParentInteractor(),
        INotificationInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    override suspend fun getCount() = bindRepo.getNotificationCount()

    override suspend fun getList() = alarmRepo.getList()

    override suspend fun cancelNotification(item: NotificationItem) {
        val id = item.note.id

        alarmRepo.delete(id)
        callback?.cancelAlarm(id)
    }

}