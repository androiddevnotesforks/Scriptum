package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.room.Database
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.data.room.extension.inRoom
import sgtmelon.scriptum.cleanup.data.room.extension.safeDelete
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.getText
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Repository of [Database] which work with notes.
 */
class NoteRepo(
    override val roomProvider: RoomProvider,
    private val noteConverter: NoteConverter,
    private val rollConverter: RollConverter
) : INoteRepo, IRoomWork {

    /**
     * Important:
     *
     * - For notes page need take only visible items count
     * - For bin page need take all items count
     */
    override suspend fun getCount(isBin: Boolean): Int = fromRoom {
        val noCategoryCount = noteDao.getNoCategoryCount(isBin)

        val rankIdList = if (isBin) rankDao.getIdList() else rankDao.getIdVisibleList()
        val rankVisibleCount = noteDao.getRankVisibleCount(isBin, rankIdList)

        return@fromRoom noCategoryCount + rankVisibleCount
    }

    /**
     * [isOptimal] - need for note lists where displays short information.
     *
     * [filterVisible] - need use if you need get only rank visible notes. Otherwise you will
     *                   get all items even if rank not visible.
     */
    override suspend fun getList(
        sort: Sort,
        isBin: Boolean,
        isOptimal: Boolean,
        filterVisible: Boolean
    ): MutableList<NoteItem> = fromRoom {
        var entityList = getSortBy(isBin, sort, noteDao)

        if (filterVisible) entityList = filterVisible(entityList, rankDao)

        val itemList = entityList.map {
            transformNoteEntity(it, isOptimal, db = this)
        }.toMutableList()

        return@fromRoom correctRankSort(itemList, sort)
    }

    @RunPrivate
    suspend fun getSortBy(isBin: Boolean, sort: Sort, noteDao: NoteDao) = when (sort) {
        Sort.CHANGE -> noteDao.getListByChange(isBin)
        Sort.CREATE -> noteDao.getListByCreate(isBin)
        Sort.RANK -> noteDao.getListByRank(isBin)
        Sort.COLOR -> noteDao.getListByColor(isBin)
    }

    /**
     * List must contains only item which isVisible.
     */
    @RunPrivate
    suspend fun filterVisible(list: List<NoteEntity>, rankDao: RankDao): List<NoteEntity> {
        val idVisibleList = rankDao.getIdVisibleList()

        return list.filter { noteConverter.toItem(it).isRankVisible(idVisibleList) }
    }

    /**
     * Correcting rank sort, because notes without rank stay first in list.
     */
    @RunPrivate
    fun correctRankSort(list: MutableList<NoteItem>, sort: Sort) = list.apply {
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
    override suspend fun getItem(id: Long, isOptimal: Boolean): NoteItem? = fromRoom {
        val entity = noteDao.get(id) ?: return@fromRoom null

        return@fromRoom transformNoteEntity(entity, isOptimal, db = this)
    }

    @RunPrivate
    suspend fun transformNoteEntity(
        entity: NoteEntity,
        isOptimal: Boolean,
        db: Database
    ): NoteItem = with(db) {
        val isVisible = rollVisibleDao.getVisible(entity.id)
        val rollList = rollConverter.toItem(getPreview(entity.id, isVisible, isOptimal, db))
        val alarmEntity = alarmDao.get(entity.id)

        return noteConverter.toItem(entity, isVisible, rollList, alarmEntity)
    }

    @RunPrivate
    suspend fun getPreview(
        id: Long,
        isVisible: Boolean?,
        isOptimal: Boolean,
        db: Database
    ): MutableList<RollEntity> = with(db) {
        if (isOptimal) {
            /**
             * If:
             *  1. list items not hide (true or null) -> get simple view
             *  2. is hide and not all done -> get only not checked items view
             *  3. is hide and all done -> get simple view
             */
            if (isVisible != false) {
                rollDao.getPreviewList(id)
            } else {
                rollDao.getPreviewHideList(id).takeIf { it.isNotEmpty() } ?: rollDao.getPreviewList(
                    id
                )
            }
        } else {
            rollDao.getList(id)
        }
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override suspend fun getRollList(noteId: Long): MutableList<RollItem> = fromRoom {
        rollConverter.toItem(rollDao.getList(noteId))
    }

    // Repo other functions

    /**
     * Have hide notes in list or not.
     */
    override suspend fun isListHide(): Boolean = fromRoom {
        val rankIdVisibleList = rankDao.getIdVisibleList()

        return@fromRoom noteDao.getList(false).any {
            !noteConverter.toItem(it).isRankVisible(rankIdVisibleList)
        }
    }

    // Repo work with delete functions

    override suspend fun clearBin() = inRoom {
        val itemList = noteDao.getList(true)

        for (it in itemList) {
            clearConnection(it.id, it.rankId, rankDao)
        }

        noteDao.delete(itemList)
    }

    override suspend fun deleteNote(item: NoteItem) = inRoom {
        alarmDao.delete(item.id)
        noteDao.update(noteConverter.toEntity(item.onDelete()))
    }

    override suspend fun restoreNote(item: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(item.onRestore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(item: NoteItem) = inRoom {
        clearConnection(item.id, item.rankId, rankDao)
        noteDao.delete(noteConverter.toEntity(item))
    }

    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    @RunPrivate
    suspend fun clearConnection(noteId: Long, rankId: Long, rankDao: RankDao) {
        val entity = rankDao.get(rankId) ?: return
        entity.noteId.remove(noteId)
        rankDao.update(entity)
    }

    // Repo save and update functions

    override suspend fun convertNote(item: NoteItem.Text): NoteItem.Roll = fromRoom {
        val newItem = item.onConvert()

        for (it in newItem.list) {
            it.id = rollDao.insert(rollConverter.toEntity(newItem.id, it))
        }

        noteDao.update(noteConverter.toEntity(newItem))

        return@fromRoom newItem
    }

    override suspend fun convertNote(
        item: NoteItem.Roll,
        useCache: Boolean
    ): NoteItem.Text = fromRoom {
        val newItem = if (useCache) {
            item.onConvert()
        } else {
            item.onConvert(rollConverter.toItem(rollDao.getList(item.id)))
        }

        noteDao.update(noteConverter.toEntity(newItem))
        rollDao.delete(newItem.id)

        return@fromRoom newItem
    }

    override suspend fun getCopyText(item: NoteItem) = StringBuilder().apply {
        if (item.name.isNotEmpty()) {
            append(item.name).append("\n")
        }

        when (item) {
            is NoteItem.Text -> append(item.text)
            is NoteItem.Roll -> append(getRollList(item.id).getText())
        }
    }.toString()

    override suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean) = inRoom {
        val entity = noteConverter.toEntity(item)

        if (isCreate) {
            item.id = noteDao.insert(entity)
        } else {
            noteDao.update(entity)
        }
    }

    override suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean) = inRoom {
        val noteEntity = noteConverter.toEntity(item)

        if (isCreate) {
            item.id = noteDao.insert(noteEntity)

            for (rollItem in item.list) {
                rollItem.id = rollDao.insert(rollConverter.toEntity(item.id, rollItem))
            }
        } else {
            noteDao.update(noteEntity)

            /**
             * List of roll id's, which wasn't swiped.
             */
            val excludeIdList = ArrayList<Long>()

            for (rollItem in item.list) {
                val id = rollItem.id

                if (id == null) {
                    rollItem.id = rollDao.insert(rollConverter.toEntity(item.id, rollItem))
                } else {
                    rollDao.update(id, rollItem.position, rollItem.text)
                }

                rollItem.id?.let { excludeIdList.add(it) }
            }

            /**
             * Remove swiped rolls.
             */
            rollDao.safeDelete(item.id, excludeIdList)
        }
    }

    override suspend fun updateRollCheck(item: NoteItem.Roll, p: Int) = inRoom {
        val rollItem = item.list.getOrNull(p) ?: return@inRoom
        val rollId = rollItem.id ?: return@inRoom

        rollDao.updateCheck(rollId, rollItem.isCheck)
        noteDao.update(noteConverter.toEntity(item))
    }

    override suspend fun updateRollCheck(item: NoteItem.Roll, isCheck: Boolean) = inRoom {
        rollDao.updateAllCheck(item.id, isCheck)
        noteDao.update(noteConverter.toEntity(item))
    }

    override suspend fun updateNote(item: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(item))
    }

    override suspend fun setRollVisible(item: NoteItem.Roll) = inRoom {
        val value = rollVisibleDao.getVisible(item.id)

        if (value == null) {
            val entity = RollVisibleEntity(noteId = item.id, value = item.isVisible)
            rollVisibleDao.insert(entity)
        } else if (item.isVisible != value) {
            rollVisibleDao.update(item.id, item.isVisible)
        }
    }

    // Repo backup functions

    override suspend fun getNoteBackup(): List<NoteEntity> {
        return fromRoom { noteDao.getList(isBin = false) }
    }

    override suspend fun getRollBackup(noteIdList: List<Long>): List<RollEntity> {
        return fromRoom { rollDao.getList(noteIdList) }
    }

    override suspend fun getRollVisibleBackup(noteIdList: List<Long>): List<RollVisibleEntity> {
        return fromRoom { rollVisibleDao.getList(noteIdList) }
    }

}