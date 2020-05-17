package sgtmelon.scriptum.data.repository.room

import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.data.room.dao.IRankDao
import sgtmelon.scriptum.data.room.dao.IRollDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.extension.move

/**
 * Repository of [RoomDb] which work with notes.
 */
class NoteRepo(override val roomProvider: RoomProvider) : INoteRepo, IRoomWork {

    // TODO test for nullable values

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    /**
     * Important:
     *
     * - For notes page need take only visible items count
     * - For bin page need take all items count
     */
    override suspend fun getCount(bin: Boolean): Int? = takeFromRoom {
        val rankIdList = if (bin) rankDao.getIdList() else rankDao.getIdVisibleList()

        return@takeFromRoom noteDao.getCount(bin, rankIdList)
    }

    /**
     * [isOptimal] - need for note lists where displays short information.
     *
     * [filterVisible] - need use if you need get only rank visible notes. Otherwise you will
     *                   get all items even if rank not visible.
     */
    override suspend fun getList(@Sort sort: Int, bin: Boolean, isOptimal: Boolean,
                                 filterVisible: Boolean): MutableList<NoteItem>? = takeFromRoom {
        var entityList = getSortBy(noteDao, sort, bin) ?: return@takeFromRoom null

        if (filterVisible) entityList = filterVisible(rankDao, entityList)

        val itemList = entityList.map {
            val rollList = rollConverter.toItem(getPreview(rollDao, it.id, isOptimal))
            return@map noteConverter.toItem(it, rollList, alarmDao.get(it.id))
        }.toMutableList()

        return@takeFromRoom correctRankSort(itemList, sort)
    }

    @VisibleForTesting
    suspend fun getSortBy(iNoteDao: INoteDao, @Sort sort: Int, bin: Boolean) = when (sort) {
        Sort.CHANGE -> iNoteDao.getByChange(bin)
        Sort.CREATE -> iNoteDao.getByCreate(bin)
        Sort.RANK -> iNoteDao.getByRank(bin)
        Sort.COLOR -> iNoteDao.getByColor(bin)
        else -> null
    }

    /**
     * List must contains only item which isVisible.
     */
    @VisibleForTesting
    suspend fun filterVisible(iRankDao: IRankDao, list: List<NoteEntity>): List<NoteEntity> {
        val idVisibleList = iRankDao.getIdVisibleList()

        return list.filter { noteConverter.toItem(it).isVisible(idVisibleList) }
    }

    @VisibleForTesting
    fun correctRankSort(list: MutableList<NoteItem>, @Sort sort: Int) = list.apply {
        if (sort != Sort.RANK) return@apply

        /**
         * List must contains item with and without rank.
         */
        if (any { it.haveRank() } && any { !it.haveRank() }) {

            /**
             * Move items without rank to list end.
             */
            while (!first().haveRank()) {
                move(from = 0)
            }
        }
    }


    /**
     * Return null if note doesn't exist.
     *
     * [id] - note item id.
     * [isOptimal] - need for note lists where displays short information.
     */
    override suspend fun getItem(id: Long, isOptimal: Boolean): NoteItem? = takeFromRoom {
        val entity = noteDao.get(id) ?: return@takeFromRoom null
        val rollList = rollConverter.toItem(getPreview(rollDao, id, isOptimal))

        return@takeFromRoom noteConverter.toItem(entity, rollList, alarmDao.get(id))
    }

    @VisibleForTesting
    suspend fun getPreview(iRollDao: IRollDao, id: Long, isOptimal: Boolean) = with(iRollDao) {
        if (isOptimal) getView(id) else get(id)
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override suspend fun getRollList(noteId: Long): MutableList<RollItem>? = takeFromRoom {
        rollConverter.toItem(rollDao.get(noteId))
    }


    /**
     * Have hide notes in list or not.
     */
    override suspend fun isListHide(): Boolean? = takeFromRoom {
        noteDao.get(false).any {
            !noteConverter.toItem(it).isVisible(rankDao.getIdVisibleList())
        }
    }

    override suspend fun clearBin() = inRoom {
        val itemList = noteDao.get(true).apply {
            forEach { clearConnection(rankDao, it.id, it.rankId) }
        }

        noteDao.delete(itemList)
    }


    override suspend fun deleteNote(noteItem: NoteItem) = inRoom {
        alarmDao.delete(noteItem.id)
        noteDao.update(noteConverter.toEntity(noteItem.onDelete()))
    }

    override suspend fun restoreNote(noteItem: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(noteItem.onRestore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(noteItem: NoteItem) = inRoom {
        clearConnection(rankDao, noteItem.id, noteItem.rankId)
        noteDao.delete(noteConverter.toEntity(noteItem))
    }


    override suspend fun convertNote(noteItem: NoteItem.Text): NoteItem.Roll? = takeFromRoom {
        val item = noteItem.onConvert()

        item.list.forEach {
            it.id = rollDao.insert(rollConverter.toEntity(item.id, it))
        }

        noteDao.update(noteConverter.toEntity(item))

        return@takeFromRoom item
    }

    override suspend fun convertNote(noteItem: NoteItem.Roll,
                                     useCache: Boolean): NoteItem.Text? = takeFromRoom {
        val item = if (useCache) {
            noteItem.onConvert()
        } else {
            noteItem.onConvert(rollConverter.toItem(rollDao.get(noteItem.id)))
        }

        noteDao.update(noteConverter.toEntity(item))
        rollDao.delete(item.id)

        return@takeFromRoom item
    }

    override suspend fun getCopyText(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.name.isNotEmpty()) {
            append(noteItem.name).append("\n")
        }

        when (noteItem) {
            is NoteItem.Text -> append(noteItem.text)
            is NoteItem.Roll -> inRoom {
                append(rollConverter.toItem(rollDao.get(noteItem.id)).getText())
            }
        }
    }.toString()

    override suspend fun saveNote(noteItem: NoteItem.Text, isCreate: Boolean) = inRoom {
        val entity = noteConverter.toEntity(noteItem)

        if (isCreate) {
            noteItem.id = noteDao.insert(entity)
        } else {
            noteDao.update(entity)
        }
    }

    override suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean) = inRoom {
        val noteEntity = noteConverter.toEntity(noteItem)

        if (isCreate) {
            noteItem.id = noteDao.insert(noteEntity)
            noteItem.list.forEach {
                it.id = rollDao.insert(rollConverter.toEntity(noteItem.id, it))
            }
        } else {
            noteDao.update(noteEntity)

            /**
             * List of roll id's, which wasn't swiped.
             */
            val idSaveList = ArrayList<Long>()

            noteItem.list.forEach { item ->
                val id = item.id

                if (id == null) {
                    item.id = rollDao.insert(rollConverter.toEntity(noteItem.id, item))
                } else {
                    rollDao.update(id, item.position, item.text)
                }

                item.id?.let { idSaveList.add(it) }
            }

            /**
             * Remove swiped rolls.
             */
            rollDao.delete(noteItem.id, idSaveList)
        }
    }

    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) = inRoom {
        val item = noteItem.list.getOrNull(p) ?: return@inRoom
        val rollId = item.id ?: return@inRoom

        rollDao.update(rollId, item.isCheck)
        noteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, check: Boolean) = inRoom {
        rollDao.updateAllCheck(noteItem.id, check)
        noteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateNote(noteItem: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(noteItem))
    }


    override suspend fun setRollVisible(noteId: Long, isVisible: Boolean) = inRoom {
        val value = rollVisibleDao.get(noteId)

        if (value == null) {
            rollVisibleDao.insert(RollVisibleEntity(noteId = noteId, value = isVisible))
        } else if (isVisible != value) {
            rollVisibleDao.update(noteId, isVisible)
        }
    }

    override suspend fun getRollVisible(noteId: Long): Boolean? = takeFromRoom {
        rollVisibleDao.get(noteId) ?: run {
            RollVisibleEntity(noteId = noteId).also { rollVisibleDao.insert(it) }.value
        }
    }


    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    @VisibleForTesting
    suspend fun clearConnection(iRankDao: IRankDao, noteId: Long, rankId: Long) {
        val entity = iRankDao.get(rankId)?.also { it.noteId.remove(noteId) } ?: return
        iRankDao.update(entity)
    }

}