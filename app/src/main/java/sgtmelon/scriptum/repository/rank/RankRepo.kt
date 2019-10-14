package sgtmelon.scriptum.repository.rank

import android.content.Context
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Repository of [RoomDb] for work with ranks
 *
 * @param context for open [RoomDb]
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    override fun insert(rankEntity: RankEntity): Long {
        val id: Long
        openRoom().apply { id = iRankDao.insert(rankEntity) }.close()
        return id
    }

    override fun getList() = ArrayList<RankEntity>().apply { inRoom { addAll(iRankDao.get()) } }

    override fun delete(rankEntity: RankEntity) = inRoom {
        rankEntity.noteId.forEach {
            val noteEntity = iNoteDao[it]?.apply {
                rankId = NoteEntity.ND_RANK_ID
                rankPs = NoteEntity.ND_RANK_PS
            } ?: return@forEach

            iNoteDao.update(noteEntity)
        }

        iRankDao.delete(rankEntity.name)
    }

    override fun update(rankEntity: RankEntity) = inRoom { iRankDao.update(rankEntity) }

    override fun update(list: List<RankEntity>) = inRoom {
        val noteIdSet: MutableSet<Long> = mutableSetOf()

        list.forEachIndexed { i, item ->
            if (item.position != i) {
                item.noteId.forEach { noteIdSet.add(it) }
                item.position = i
            }
        }

        if (noteIdSet.isNotEmpty()) updateNoteRankPosition(noteIdSet.toList(), list, db = this)

        iRankDao.update(list)
    }

    /**
     * [noteIdList] - id of notes, which need update
     * [rankList] - new rank list, with need rank positions
     */
    private fun updateNoteRankPosition(noteIdList: List<Long>, rankList: List<RankEntity>, db: RoomDb) =
            with(db.iNoteDao) {
                val noteList = get(noteIdList)

                noteList.forEach { item ->
                    rankList.forEach {
                        if (item.rankId == it.id) {
                            item.rankPs = it.position
                        }
                    }
                }

                update(noteList)
            }

}