package sgtmelon.scriptum.domain.interactor.impl.notification

import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationBridge
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interactor for [NotificationViewModel].
 */
class NotificationInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val bindRepo: IBindRepo,
        @VisibleForTesting var callback: INotificationBridge?
) : ParentInteractor(),
        INotificationInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    override suspend fun getCount(): Int = bindRepo.getNotificationCount()

    override suspend fun getList(): MutableList<NotificationItem> = alarmRepo.getList()

    override suspend fun cancelNotification(item: NotificationItem) {
        val id = item.note.id

        alarmRepo.delete(id)
        callback?.cancelAlarm(id)
    }

}