package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource

/**
 * Repository for work with status bar bind data.
 */
class BindRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val alarmDataSource: AlarmDataSource
) : BindRepo {

    override suspend fun unbindNote(id: Long) {
        val entity = noteDataSource.get(id) ?: return

        if (!entity.isStatus) return

        entity.isStatus = false
        noteDataSource.update(entity)
    }

    override suspend fun getNotificationsCount(): Int = alarmDataSource.getCount()

}