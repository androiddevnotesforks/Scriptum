package sgtmelon.scriptum.data.repository.database

import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource

/**
 * Repository for work with status bar bind data.
 */
class BindRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val alarmDataSource: AlarmDataSource
) : BindRepo {

    override suspend fun unbindNote(noteId: Long) {
        val entity = noteDataSource.get(noteId) ?: return

        if (!entity.isStatus) return

        entity.isStatus = false
        noteDataSource.update(entity)
    }

    override suspend fun getNotificationsCount(): Int = alarmDataSource.getCount()

}