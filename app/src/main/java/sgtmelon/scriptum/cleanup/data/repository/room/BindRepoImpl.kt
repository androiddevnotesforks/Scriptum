package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Repository of [Database] which work with notes bind in status bar.
 */
class BindRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val alarmDataSource: AlarmDataSource
) : BindRepo {

    override suspend fun unbindNote(id: Long) {
        val noteEntity = noteDataSource.get(id) ?: return

        if (!noteEntity.isStatus) return

        noteEntity.isStatus = false
        noteDataSource.update(noteEntity)
    }

    override suspend fun getNotificationCount(): Int = alarmDataSource.getCount()

}