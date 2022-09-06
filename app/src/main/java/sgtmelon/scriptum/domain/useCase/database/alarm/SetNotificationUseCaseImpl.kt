package sgtmelon.scriptum.domain.useCase.database.alarm

import java.util.Calendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Insert or update existing alarm.
 */
class SetNotificationUseCaseImpl(
    private val repository: AlarmRepo
) : SetNotificationUseCase {

    override suspend operator fun invoke(item: NoteItem, calendar: Calendar) {
        invoke(item, calendar.getText())
    }

    override suspend operator fun invoke(item: NoteItem, date: String) {
        repository.insertOrUpdate(item, date)
    }
}