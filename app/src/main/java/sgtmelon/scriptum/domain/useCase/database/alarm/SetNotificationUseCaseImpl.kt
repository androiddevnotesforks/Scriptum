package sgtmelon.scriptum.domain.useCase.database.alarm

import java.util.Calendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

/**
 * Insert or update existing alarm.
 */
class SetNotificationUseCaseImpl(
    private val dataSource: AlarmDataSource,
    private val converter: AlarmConverter
) : SetNotificationUseCase {

    override suspend operator fun invoke(item: NoteItem, calendar: Calendar) {
        invoke(item, calendar.getText())
    }

    override suspend operator fun invoke(item: NoteItem, date: String) {
        item.alarmDate = date

        val entity = converter.toEntity(item)
        if (item.haveAlarm()) {
            dataSource.update(entity)
        } else {
            /** Catch of insert errors happen inside dataSource. */
            item.alarmId = dataSource.insert(entity) ?: return
        }
    }
}