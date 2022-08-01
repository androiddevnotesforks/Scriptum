package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interactor for [INotificationViewModel].
 */
class NotificationInteractor(
    private val noteRepo: INoteRepo,
    private val alarmRepo: IAlarmRepo,
    private val bindRepo: IBindRepo
) : ParentInteractor(),
    INotificationInteractor {

    override suspend fun getCount(): Int = bindRepo.getNotificationCount()

    override suspend fun getList(): List<NotificationItem> = alarmRepo.getList()


    override suspend fun setNotification(item: NotificationItem): NotificationItem? {
        val id = item.note.id
        val noteItem = noteRepo.getItem(id, isOptimal = true) ?: return null

        alarmRepo.insertOrUpdate(noteItem, item.alarm.date)

        /**
         * After insert need return item with new id.
         */
        return alarmRepo.getItem(id)
    }

    override suspend fun cancelNotification(item: NotificationItem) = alarmRepo.delete(item.note.id)

}