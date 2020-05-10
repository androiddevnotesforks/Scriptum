package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Repository of [RoomDb] which work with all tables data.
 */
class DevelopRepo(override val roomProvider: RoomProvider) : IDevelopRepo, IRoomWork {

    override suspend fun getNoteList(): List<NoteEntity>? = takeFromRoom {
        ArrayList<NoteEntity>().apply {
            addAll(noteDao.getByChange(bin = true))
            addAll(noteDao.getByChange(bin = false))
        }
    }

    override suspend fun getRollList(): List<RollEntity>? = takeFromRoom {
        ArrayList<RollEntity>().apply {
            noteDao.getByChange(bin = false)
                    .filter { it.type == NoteType.ROLL }
                    .map { it.id }
                    .forEach { noteId -> rollDao.get(noteId).forEach { add(it) } }

            noteDao.getByChange(bin = true)
                    .filter { it.type == NoteType.ROLL }
                    .map { it.id }
                    .forEach { noteId -> rollDao.get(noteId).forEach { add(it) } }
        }
    }

    override suspend fun getRankList(): List<RankEntity>? = takeFromRoom { rankDao.get() }

}