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

    override fun insert(p: Int, rankEntity: RankEntity): Long {
        val id: Long

        openRoom().apply {
            id = iRankDao.insert(rankEntity)
            if (p != 0) updateRankPosition(p, db = this)
        }.close()

        return id
    }

    override fun getRankModel() = RankModel(getCompleteRankList())

    override fun delete(name: String, p: Int) = inRoom {
        val rankEntity = iRankDao[name] ?: return@inRoom

        if (rankEntity.noteId.isNotEmpty()) {
            val noteList = iNoteDao[rankEntity.noteId]

            /**
             * Убирает из списков ненужную категорию по id
             */
            noteList.forEach {
                val index = it.rankId.indexOf(rankEntity.id)

                it.rankId.removeAt(index)
                it.rankPs.removeAt(index)
            }

            iNoteDao.update(noteList)
        }

        iRankDao.delete(rankEntity)

        updateRankPosition(p, db = this)
    }

    override fun update(rankEntity: RankEntity) = inRoom { iRankDao.update(rankEntity) }

    override fun update(list: MutableList<RankEntity>) = inRoom {
        val noteIdList: MutableSet<Long> = mutableSetOf()

        list.forEachIndexed { i, item ->
            if (item.position != i) {
                item.noteId.forEach { noteIdList.add(it) }
                item.position = i
            }
        }

        if (noteIdList.isNotEmpty()) {
            updateNoteRankPosition(noteIdList.toList(), list, db = this)
        }

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
     * @param fromPosition - Позиция удаления категории
     */
    private fun updateRankPosition(fromPosition: Int, db: RoomDb) = with(db) {
        val rankList = iRankDao.get()
        val noteIdList = ArrayList<Long>()

        for (i in fromPosition until rankList.size) {
            rankList[i].apply {
                noteId.forEach { if (!noteIdList.contains(it)) noteIdList.add(it) }
                position = i
            }
        }

        iRankDao.update(rankList)
        updateNoteRankPosition(noteIdList, rankList, db = this)
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