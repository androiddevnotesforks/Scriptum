package sgtmelon.scriptum.cleanup.data.repository.room

import java.util.Calendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

/**
 * Repository for work with alarm.
 */
class AlarmRepoImpl(
    private val dataSource: AlarmDataSource,
    private val converter: AlarmConverter
) : AlarmRepo {

    override suspend fun insertOrUpdate(item: NoteItem, calendar: Calendar) {
        insertOrUpdate(item, calendar.getText())
    }

    override suspend fun insertOrUpdate(item: NoteItem, date: String) {
        item.alarmDate = date

        val entity = converter.toEntity(item)
        if (item.haveAlarm()) {
            dataSource.update(entity)
        } else {
            /** Catch of insert errors happen inside dataSource. */
            item.alarmId = dataSource.insert(entity) ?: return
        }
    }

    override suspend fun delete(noteId: Long) = dataSource.delete(noteId)


    override suspend fun getItem(noteId: Long): NotificationItem? = dataSource.getItem(noteId)

    override suspend fun getList(): List<NotificationItem> = dataSource.getItemList()

    override suspend fun getBackupList(noteIdList: List<Long>): List<AlarmEntity> {
        return dataSource.getList(noteIdList)
    }
}