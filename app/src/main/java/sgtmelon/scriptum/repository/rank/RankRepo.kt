package sgtmelon.scriptum.repository.rank

import android.content.Context
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Репозиторий обработки данных [RoomDb] для работы с категориями
 *
 * @param context для открытия [RoomDb]
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    // TODO убрать roomRepo
    private val iRoomRepo: IRoomRepo = RoomRepo(context)

    // TODO move to interactor
    override suspend fun notifyBind() = inRoom {
        val rankIdVisibleList = iRankDao.getIdVisibleList()

        iRoomRepo.getNoteModelList(bin = false).forEach {
            BindControl(context, it).updateBind(rankIdVisibleList)
        }
    }

    override fun insert(rankEntity: RankEntity): Long {
        val id: Long
        openRoom().apply { id = iRankDao.insert(rankEntity) }.close()
        return id
    }

    /**
     * TODO подумать, может можно убрать дополнительный запрос для получения text/rollCount
     */
    override fun get() = ArrayList<RankEntity>().apply {
        inRoom {
            addAll(iRankDao.get())
            forEach { it.noteCount = iNoteDao.getCount(it.noteId) }
        }
    }

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

    override fun update(list: MutableList<RankEntity>) = inRoom {
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
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
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