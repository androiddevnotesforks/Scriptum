package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.extension.fromRoom
import sgtmelon.scriptum.data.room.extension.inRoom
import sgtmelon.scriptum.data.room.extension.safeInsert
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData.Note
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

    override suspend fun getCount(): Int = fromRoom { rankDao.getCount() }

    override suspend fun getList(): MutableList<RankItem> = fromRoom {
        val list = converter.toItem(rankDao.get())

        for (item in list) {
            item.bindCount = noteDao.getBindCount(item.noteId)
            item.notificationCount = alarmDao.getCount(item.noteId)
        }

        return@fromRoom list
    }

    /**
     * Return list of rank id's which is visible.
     */
    override suspend fun getIdVisibleList(): List<Long> = fromRoom { rankDao.getIdVisibleList() }


    override suspend fun insert(name: String): Long? = fromRoom {
        rankDao.safeInsert(RankEntity[name])
    }

    override suspend fun insert(item: RankItem) = inRoom {
        for (id in item.noteId) {
            /**
             * Remove rank from note.
             */
            val noteEntity = noteDao.get(id)?.apply {
                rankId = item.id
                rankPs = item.position
            } ?: continue

            noteDao.update(noteEntity)
        }

        /**
         * Id after insert will be the same, like in [item].
         */
        rankDao.safeInsert(converter.toEntity(item))
    }

    override suspend fun delete(item: RankItem) = inRoom {
        for (id in item.noteId) {
            /**
             * Remove rank from note.
             */
            val noteEntity = noteDao.get(id)?.apply {
                rankId = Note.Default.RANK_ID
                rankPs = Note.Default.RANK_PS
            } ?: continue

            noteDao.update(noteEntity)
        }

        rankDao.delete(item.name)
    }

    override suspend fun update(item: RankItem) = inRoom {
        rankDao.update(converter.toEntity(item))
    }

    override suspend fun update(list: List<RankItem>) = inRoom {
        rankDao.update(converter.toEntity(list))
    }

    override suspend fun updatePosition(list: List<RankItem>, idList: List<Long>) = inRoom {
        updateRankPosition(list, idList, noteDao)
        rankDao.update(converter.toEntity(list))
    }

    /**
     * Update [NoteEntity.rankPs] for notes from [idList] which related with [list].
     */
    @RunPrivate
    suspend fun updateRankPosition(
        list: List<RankItem>,
        idList: List<Long>,
        noteDao: INoteDao
    ) {
        if (idList.isEmpty()) return

        val noteList = noteDao.get(idList)
        for (entity in noteList) {
            entity.rankPs = list.firstOrNull { it.id == entity.rankId }?.position ?: continue
        }

        noteDao.update(noteList)
    }


    /**
     * Add [NoteEntity.id] to [RankEntity.noteId] or remove after some changes.
     */
    override suspend fun updateConnection(item: NoteItem) = inRoom {
        val list = rankDao.get()
        val checkArray = calculateCheckArray(list, item.rankId)

        rankDao.update(updateNoteId(list, checkArray, item.id))
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
    override suspend fun getDialogItemArray(emptyName: String): Array<String> = fromRoom {
        ArrayList<String>().apply {
            add(emptyName)
            addAll(rankDao.getNameList())
        }.toTypedArray()
    }

    /**
     * Return rank id by [position].
     */
    override suspend fun getId(position: Int): Long {
        return if (position == Note.Default.RANK_PS) {
            Note.Default.RANK_ID
        } else {
            fromRoom { rankDao.getId(position) ?: Note.Default.RANK_ID }
        }
    }


    override suspend fun getRankBackup(): List<RankEntity> = fromRoom { rankDao.get() }

}