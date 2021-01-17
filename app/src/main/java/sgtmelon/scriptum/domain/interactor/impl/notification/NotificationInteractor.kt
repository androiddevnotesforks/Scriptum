package sgtmelon.scriptum.domain.interactor.impl.notification

import sgtmelon.extension.getCalendarOrNull
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interactor for [INotificationViewModel].
 */
class NotificationInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val noteRepo: INoteRepo,
    private val alarmRepo: IAlarmRepo,
    private val bindRepo: IBindRepo,
    @RunPrivate var callback: INotificationBridge?
) : ParentInteractor(),
    INotificationInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    override suspend fun getCount(): Int = bindRepo.getNotificationCount()

    override suspend fun getList(): MutableList<NotificationItem> = alarmRepo.getList()


    override suspend fun setNotification(item: NotificationItem): NotificationItem? {
        val id = item.note.id
        val date = item.alarm.date

        val noteItem = noteRepo.getItem(id, isOptimal = true) ?: return null
        val calendar = date.getCalendarOrNull() ?: return null

        alarmRepo.insertOrUpdate(noteItem, date)

        runMain { callback?.setAlarm(calendar, id) }

        /**
         * After insert need return item with new id.
         */
        return alarmRepo.getItem(id)
    }

    override suspend fun cancelNotification(item: NotificationItem) {
        val id = item.note.id

        alarmRepo.delete(id)
        runMain { callback?.cancelAlarm(id) }
    }
}