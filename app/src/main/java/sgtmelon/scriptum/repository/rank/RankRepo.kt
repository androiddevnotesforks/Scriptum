package sgtmelon.scriptum.repository.rank

import android.content.Context
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.room.RoomRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Репозиторий обработки данных [RoomDb] для работы с категориями
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    // TODO убрать roomRepo
    private val iRoomRepo = RoomRepo.getInstance(context)

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

    override fun getRankModel() = RankModel(getCompleteRankList())

    override fun delete(name: String) = inRoom { iRankDao.delete(name) }

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
     * TODO подумать, может можно убрать дополнительный запрос для получения text/rollCount
     */
    private fun getCompleteRankList() = ArrayList<RankEntity>().apply {
        inRoom {
            addAll(iRankDao.get())
            forEach {
                it.textCount = iNoteDao.getCount(it.noteId, NoteTypeConverter().toInt(NoteType.TEXT))
                it.rollCount = iNoteDao.getCount(it.noteId, NoteTypeConverter().toInt(NoteType.ROLL))
            }
        }
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    private fun updateNoteRankPosition(noteIdList: List<Long>, rankList: List<RankEntity>, db: RoomDb) =
            with(db.iNoteDao) {
                val noteList = get(noteIdList)

                noteList.forEach { item ->
                    val newIdList = ArrayList<Long>()
                    val newPsList = ArrayList<Long>()

                    rankList.forEach {
                        if (item.rankId.contains(it.id)) {
                            newIdList.add(it.id)
                            newPsList.add(it.position.toLong())
                        }
                    }

                    item.rankId = newIdList
                    item.rankPs = newPsList
                }

                update(noteList)
            }

    companion object {
        fun getInstance(context: Context): IRankRepo = RankRepo(context)
    }

}