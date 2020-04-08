package sgtmelon.scriptum.data.repository.room

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Repository of [RoomDb] for work with ranks.
 *
 * @param context for open [RoomDb]
 */
class RankRepo(override val context: Context) : IRankRepo, IRoomWork {

    private val converter = RankConverter()


    override suspend fun getCount(): Int {
        val count: Int

        openRoom().apply { count = rankDao.getCount() }.close()

        return count
    }

    override suspend fun getList() = ArrayList<RankItem>().apply {
        inRoom {
            addAll(converter.toItem(rankDao.get()))
            forEach { item ->
                item.hasBind = noteDao.get(item.noteId).any { it.isStatus }
                item.hasNotification = alarmDao.get(item.noteId).isNotEmpty()
            }
        }
    }

    override suspend fun getBind(noteId: List<Long>): Boolean {
        val hasBind: Boolean

        openRoom().apply {
            hasBind = noteDao.get(noteId).any { it.isStatus }
        }.close()

        return hasBind
    }

    /**
     * Return list of rank id's which is visible.
     */
    override suspend fun getIdVisibleList() = ArrayList<Long>().apply {
        inRoom { addAll(rankDao.getIdVisibleList()) }
    }


    override suspend fun insert(name: String): Long {
        val id: Long

        openRoom().apply { id = rankDao.insert(RankEntity(name = name)) }.close()

        return id
    }

    override suspend fun delete(rankItem: RankItem) = inRoom {
        for (id in rankItem.noteId) {
            /**
             * Remove rank from note.
             */
            val noteEntity = noteDao.get(id)?.apply {
                rankId = DbData.Note.Default.RANK_ID
                rankPs = DbData.Note.Default.RANK_PS
            } ?: continue

            noteDao.update(noteEntity)
        }

        rankDao.delete(rankItem.name)
    }

    override suspend fun update(rankItem: RankItem) = inRoom {
        rankDao.update(converter.toEntity(rankItem))
    }

    override suspend fun update(rankList: List<RankItem>) = inRoom {
        rankDao.update(converter.toEntity(rankList))
    }

    override suspend fun updatePosition(rankList: List<RankItem>,
                                        noteIdList: List<Long>) = inRoom {
        noteDao.updateRankPosition(rankList, noteIdList)
        rankDao.update(converter.toEntity(rankList))
    }

    /**
     * Update [NoteEntity.rankPs] for notes from [noteIdList] which related with [rankList].
     */
    private suspend fun INoteDao.updateRankPosition(rankList: List<RankItem>,
                                                    noteIdList: List<Long>) {
        if (noteIdList.isEmpty()) return

        val noteList = get(noteIdList)
        for (entity in noteList) {
            entity.rankPs = rankList.firstOrNull { entity.rankId == it.id }?.position ?: continue
        }

        update(noteList)
    }


    /**
     * Add [NoteEntity.id] to [RankEntity.noteId] or remove after some changes.
     */
    override suspend fun updateConnection(noteItem: NoteItem) = inRoom {
        val list = rankDao.get()
        val checkArray = calculateCheckArray(list, noteItem.rankId)

        rankDao.update(list.updateNoteId(noteItem.id, checkArray))
    }

    private fun calculateCheckArray(rankList: List<RankEntity>, rankId: Long): BooleanArray {
        val array = BooleanArray(rankList.size)

        val index = rankList.indexOfFirst { it.id == rankId }
        if (index != -1) array[index] = true

        return array
    }

    private fun List<RankEntity>.updateNoteId(noteId: Long, checkArray: BooleanArray) = apply {
        forEachIndexed { i, item ->
            when {
                checkArray[i] && !item.noteId.contains(noteId) -> item.noteId.add(noteId)
                !checkArray[i] -> item.noteId.remove(noteId)
            }
        }
    }


    /**
     * Return array with all rank names.
     */
    override suspend fun getDialogItemArray() = ArrayList<String>().apply {
        add(context.getString(R.string.dialog_item_rank))
        inRoom { addAll(rankDao.getNameList()) }
    }.toTypedArray()

    /**
     * Return rank id by [position].
     */
    override suspend fun getId(position: Int): Long {
        val id: Long

        if (position == DbData.Note.Default.RANK_PS) {
            id = DbData.Note.Default.RANK_ID
        } else {
            openRoom().apply {
                id = rankDao.getId(position) ?: DbData.Note.Default.RANK_ID
            }.close()
        }

        return id
    }

}