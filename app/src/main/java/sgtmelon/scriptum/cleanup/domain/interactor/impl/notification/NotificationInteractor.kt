package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interactor for [INotificationViewModel].
 */
class NotificationInteractor(
    private val noteRepo: NoteRepo,
    private val alarmRepo: AlarmRepo,
    private val bindRepo: BindRepo
) : ParentInteractor(),
    INotificationInteractor {

    override suspend fun getCount(): Int = bindRepo.getNotificationCount()

    override suspend fun getList(): List<NotificationItem> = alarmRepo.getList()


    override suspend fun setNotification(item: NotificationItem): NotificationItem? {
        val noteId = item.note.id
        val noteItem = noteRepo.getItem(noteId, isOptimal = true) ?: return null

        alarmRepo.insertOrUpdate(noteItem, item.alarm.date)

        /**
         * After insert need return item with new id.
         */
        return alarmRepo.getItem(noteId)
    }

    override suspend fun cancelNotification(item: NotificationItem) = alarmRepo.delete(item.note.id)

}