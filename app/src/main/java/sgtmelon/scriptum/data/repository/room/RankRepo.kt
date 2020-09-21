package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Repository of [RoomDb] for work with ranks.
 */
class RankRepo(
        override val roomProvider: RoomProvider,
        private val converter: RankConverter
) : IRankRepo,
        IRoomWork {

    override suspend fun getCount(): Int = takeFromRoom { rankDao.getCount() }

    override suspend fun getList(): MutableList<RankItem> = takeFromRoom {
        val list = converter.toItem(rankDao.get())

        for (item in list) {
            item.hasBind = noteDao.get(item.noteId).any { it.isStatus }
            item.hasNotification = alarmDao.get(item.noteId).isNotEmpty()
        }

        return@takeFromRoom list
    }

    override suspend fun getBind(noteId: List<Long>): Boolean = takeFromRoom {
        noteDao.get(noteId).any { it.isStatus }
    }

    /**
     * Return list of rank id's which is visible.
     */
    override suspend fun getIdVisibleList(): List<Long> = takeFromRoom {
        rankDao.getIdVisibleList()
    }


    override suspend fun insert(name: String): Long? = takeFromRoom {
        val id = rankDao.insert(RankEntity(name = name))

        return@takeFromRoom checkInsertIgnore(id)
    }

    override suspend fun insert(rankItem: RankItem) = inRoom {
        for (id in rankItem.noteId) {
            /**
             * Remove rank from note.
             */
            val noteEntity = noteDao.get(id)?.apply {
                rankId = rankItem.id
                rankPs = rankItem.position
            } ?: continue

            noteDao.update(noteEntity)
        }

        /**
         * Id after insert will be the same, like in [rankItem].
         */
        rankDao.insert(converter.toEntity(rankItem))
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
        updateRankPosition(noteDao, rankList, noteIdList)
        rankDao.update(converter.toEntity(rankList))
    }

    /**
     * Update [NoteEntity.rankPs] for notes from [noteIdList] which related with [rankList].
     */
    @RunPrivate
    suspend fun updateRankPosition(noteDao: INoteDao, rankList: List<RankItem>,
                                   noteIdList: List<Long>) {
        if (noteIdList.isEmpty()) return

        val noteList = noteDao.get(noteIdList)
        for (entity in noteList) {
            entity.rankPs = rankList.firstOrNull { it.id == entity.rankId }?.position ?: continue
        }

        noteDao.update(noteList)
    }


    /**
     * Add [NoteEntity.id] to [RankEntity.noteId] or remove after some changes.
     */
    override suspend fun updateConnection(noteItem: NoteItem) = inRoom {
        val list = rankDao.get()
        val checkArray = calculateCheckArray(list, noteItem.rankId)

        rankDao.update(updateNoteId(list, checkArray, noteItem.id))
    }

    @RunPrivate
    fun calculateCheckArray(rankList: List<RankEntity>, rankId: Long): BooleanArray {
        return BooleanArray(rankList.size).apply {
            rankList.indexOfFirst { it.id == rankId }.takeIf { it != -1 }?.let {
                set(it, true)
            }
        }
    }

    @RunPrivate
    fun updateNoteId(
        list: List<RankEntity>,
        checkArray: BooleanArray,
        noteId: Long
    ): List<RankEntity> {
        if (list.size != checkArray.size) return list

        for ((i, item) in list.withIndex()) {
            val checkValue = checkArray.getOrNull(i) ?: continue

            when {
                checkValue && !item.noteId.contains(noteId) -> item.noteId.add(noteId)
                !checkValue -> item.noteId.remove(noteId)
            }
        }

        return list
    }


    /**
     * Return array with all rank names.
     */
    override suspend fun getDialogItemArray(emptyName: String): Array<String> = takeFromRoom {
        ArrayList<String>().apply {
            add(emptyName)
            addAll(rankDao.getNameList())
        }.toTypedArray()
    }

    /**
     * Return rank id by [position].
     */
    override suspend fun getId(position: Int): Long {
        return if (position == DbData.Note.Default.RANK_PS) {
            DbData.Note.Default.RANK_ID
        } else {
            takeFromRoom { rankDao.getId(position) ?: DbData.Note.Default.RANK_ID }
        }
    }


    override suspend fun getRankBackup(): List<RankEntity> = takeFromRoom { rankDao.get() }

}