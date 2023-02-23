package sgtmelon.scriptum.data.repository.database

import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm

/**
 * Repository for work with alarm.
 */
class AlarmRepoImpl(
    private val dataSource: AlarmDataSource,
    private val converter: AlarmConverter
) : AlarmRepo {

    override suspend fun insertOrUpdate(item: NoteItem, date: String): Long? {
        item.alarm.date = date

        val entity = converter.toEntity(item)
        if (item.haveAlarm) {
            dataSource.update(entity)
        } else {
            /** Catch of insert errors happen inside dataSource. */
            item.alarm.id = dataSource.insert(entity) ?: return null
        }

        return item.alarm.id
    }

    override suspend fun delete(noteId: Long) = dataSource.delete(noteId)

    override suspend fun getList(): List<NotificationItem> = dataSource.getItemList()

    override suspend fun getDateList(): List<String> = dataSource.getDateList()
}