package sgtmelon.scriptum.repository.rank

import android.content.Context
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.RankConverter
import sgtmelon.scriptum.room.dao.INoteDao

/**
 * Repository of [RoomDb] for work with ranks
 *
 * @param context for open [RoomDb]
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    private val converter = RankConverter()

    override fun insert(name: String): RankItem {
        val id: Long
        openRoom().apply { id = iRankDao.insert(name) }.close()

        return RankItem(id, name = name)
    }

    override fun getList() = ArrayList<RankItem>().apply { inRoom { addAll(iRankDao.get()) } }

    override fun delete(rankItem: RankItem) = inRoom {
        for (id in rankItem.noteId) {
            val noteEntity = iNoteDao[id]?.apply {
                rankId = DbData.Note.Default.RANK_ID
                rankPs = DbData.Note.Default.RANK_PS
            } ?: continue

            iNoteDao.update(noteEntity)
        }

        iRankDao.delete(rankItem.name)
    }

    override fun update(item: RankItem) = inRoom { iRankDao.update(converter.toEntity(item)) }

    override fun updatePosition(list: List<RankItem>) = inRoom {
        val noteIdSet = mutableSetOf<Long>()

        list.forEachIndexed { i, item ->
            if (item.position != i) {
                item.position = i
                item.noteId.forEach { noteIdSet.add(it) }
            }
        }

        iNoteDao.updateRankInformation(noteIdSet.toList(), list)
        iRankDao.update(converter.toEntity(list))
    }

    /**
     * [idList] - id of notes, which need update
     * [rankList] - new rank list, with need rank positions
     */
    private fun INoteDao.updateRankInformation(idList: List<Long>, rankList: List<RankItem>) {
        if (idList.isEmpty()) return

        val noteList = get(idList)

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