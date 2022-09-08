package sgtmelon.scriptum.domain.useCase.alarm

import java.util.Calendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo

/**
 * Insert or update existing alarm.
 */
class SetNotificationUseCaseImpl(
    private val noteRepo: NoteRepo,
    private val alarmRepo: AlarmRepo
) : SetNotificationUseCase {

    override suspend fun invoke(item: NotificationItem): NotificationItem? {
        val noteItem = noteRepo.getItem(item.note.id, isOptimal = true) ?: return null
        val newId = alarmRepo.insertOrUpdate(noteItem, item.alarm.date) ?: return null

        /** After insert need return item with new id. */
        return item.copy(alarm = item.alarm.copy(id = newId))
    }

    override suspend operator fun invoke(item: NoteItem, calendar: Calendar) {
        invoke(item, calendar.toText())
    }

    override suspend operator fun invoke(item: NoteItem, date: String) {
        alarmRepo.insertOrUpdate(item, date)
    }
}