package sgtmelon.scriptum.repository.rank

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.INoteDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Repository of [RoomDb] for work with ranks.
 *
 * @param context for open [RoomDb]
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    override fun isEmpty(): Boolean {
        val count: Int

        openRoom().apply { count = iRankDao.getCount() }.close()

        return count == 0
    }

    override fun getList() = ArrayList<RankEntity>().apply {
        inRoom { addAll(iRankDao.get()) }
    }

    /**
     * Return list of rank id's which is visible.
     */
    override fun getIdVisibleList() = ArrayList<Long>().apply {
        inRoom { addAll(iRankDao.getIdVisibleList()) }
    }


    override fun insert(name: String): Long {
        val id: Long

        openRoom().apply { id = iRankDao.insert(RankEntity(name = name)) }.close()

        return id
    }

    override fun delete(rankEntity: RankEntity) = inRoom {
        for (id in rankEntity.noteId) {
            /**
             * Remove rank from note.
             */
            val noteEntity = iNoteDao[id]?.apply {
                rankId = DbData.Note.Default.RANK_ID
                rankPs = DbData.Note.Default.RANK_PS
            } ?: continue

            iNoteDao.update(noteEntity)
        }

        iRankDao.delete(rankEntity.name)
    }

    override fun update(rankEntity: RankEntity) = inRoom { iRankDao.update(rankEntity) }

    override fun updatePosition(rankList: List<RankEntity>, noteIdList: List<Long>) = inRoom {
        iNoteDao.updateRankInformation(rankList, noteIdList)
        iRankDao.update(rankList)
    }

    /**
     * Update [NoteEntity.rankPs] for notes from [noteIdList] which related with [rankList].
     */
    private fun INoteDao.updateRankInformation(rankList: List<RankEntity>,
                                               noteIdList: List<Long>) {
        if (noteIdList.isEmpty()) return

        val noteList = get(noteIdList)
        noteList.forEach { noteItem ->
            rankList.forEach {
                if (noteItem.rankId == it.id) noteItem.rankPs = it.position
            }
        }

        update(noteList)
    }

    /**
     * Add [NoteEntity.id] to [RankEntity.noteId] or remove after some changes.
     */
    override fun updateConnection(noteModel: NoteModel) = inRoom {
        val list = iRankDao.get()
        val checkArray = calculateCheckArray(list, noteModel.noteEntity.rankId)

        val id = noteModel.noteEntity.id
        list.forEachIndexed { i, item ->
            when {
                checkArray[i] && !item.noteId.contains(id) -> item.noteId.add(id)
                !checkArray[i] -> item.noteId.remove(id)
            }
        }

        iRankDao.update(list)
    }

    private fun calculateCheckArray(rankList: List<RankEntity>, rankId: Long): BooleanArray {
        val array = BooleanArray(rankList.size)

        val index = rankList.indexOfFirst { it.id == rankId }
        if (index != -1) array[index] = true

        return array
    }

    /**
     * Return array with all rank names
     */
    override fun getDialogItemArray() = ArrayList<String>().apply {
        add(context.getString(R.string.dialog_item_rank))
        inRoom { addAll(iRankDao.getNameList()) }
    }.toTypedArray()

    /**
     * Return rank id by check (position)
     */
    override fun getId(check: Int): Long {
        val id: Long

        if (check != DbData.Note.Default.RANK_PS) {
            openRoom().apply {
                id = iRankDao.getIdList().getOrNull(check) ?: DbData.Note.Default.RANK_ID
            }.close()
        } else {
            id = DbData.Note.Default.RANK_ID
        }

        return id
    }

}