package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.test.prod.RunPrivate

/**
 * Repository for work with notes.
 */
class NoteRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val rollDataSource: RollDataSource,
    private val rollVisibleDataSource: RollVisibleDataSource,
    private val rankDataSource: RankDataSource,
    private val alarmDataSource: AlarmDataSource,
    private val noteConverter: NoteConverter,
    private val rollConverter: RollConverter
) : NoteRepo {

    /**
     * Important:
     *
     * - For notes page need take only visible items count
     * - For bin page need take all items count
     */
    override suspend fun getCount(isBin: Boolean): Int {
        val rankIdList = if (isBin) {
            rankDataSource.getIdList()
        } else {
            rankDataSource.getIdVisibleList()
        }

        return noteDataSource.getRankVisibleCount(isBin, rankIdList)
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
    ): MutableList<NoteItem> {
        var entityList = noteDataSource.getList(sort, isBin)

        if (filterVisible) entityList = filterVisible(entityList)

        val itemList = entityList.map { transformNoteEntity(it, isOptimal) }.toMutableList()

        return correctRankSort(itemList, sort)
    }

    /**
     * List must contains only item which isVisible.
     */
    @RunPrivate
    suspend fun filterVisible(list: List<NoteEntity>): List<NoteEntity> {
        val idVisibleList = rankDataSource.getIdVisibleList()

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
     * [noteId] - note item id.
     * [isOptimal] - need for note lists where displays short information.
     */
    override suspend fun getItem(noteId: Long, isOptimal: Boolean): NoteItem? {
        val entity = noteDataSource.get(noteId) ?: return null

        return transformNoteEntity(entity, isOptimal)
    }

    @RunPrivate
    suspend fun transformNoteEntity(entity: NoteEntity, isOptimal: Boolean): NoteItem {
        val isVisible = rollVisibleDataSource.getVisible(entity.id)
        val rollList = rollConverter.toItem(getPreview(entity.id, isVisible, isOptimal))
        val alarmEntity = alarmDataSource.get(entity.id)

        return noteConverter.toItem(entity, isVisible, rollList, alarmEntity)
    }

    @RunPrivate
    suspend fun getPreview(
        id: Long,
        isVisible: Boolean?,
        isOptimal: Boolean
    ): MutableList<RollEntity> {
        return if (isOptimal) {
            /**
             * If:
             *  1. list items not hide (true or null) -> get simple view
             *  2. is hide and not all done -> get only not checked items view
             *  3. is hide and all done -> get simple view
             */
            if (isVisible != false) {
                rollDataSource.getPreviewList(id)
            } else {
                rollDataSource.getPreviewHideList(id).takeIf { it.isNotEmpty() }
                    ?: rollDataSource.getPreviewList(id)
            }
        } else {
            rollDataSource.getList(id)
        }
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override suspend fun getRollList(noteId: Long): MutableList<RollItem> {
        return rollConverter.toItem(rollDataSource.getList(noteId))
    }

    // Repo other functions

    /**
     * Have hide notes in list or not.
     */
    override suspend fun isListHide(): Boolean {
        val rankIdVisibleList = rankDataSource.getIdVisibleList()

        return noteDataSource.getList(isBin = false).any {
            !noteConverter.toItem(it).isRankVisible(rankIdVisibleList)
        }
    }

    // Repo work with delete functions

    override suspend fun clearBin() {
        val itemList = noteDataSource.getList(isBin = true)

        for (it in itemList) {
            clearConnection(it.id, it.rankId)
        }

        noteDataSource.delete(itemList)
    }

    override suspend fun deleteNote(item: NoteItem) {
        alarmDataSource.delete(item.id)
        noteDataSource.update(noteConverter.toEntity(item.onDelete()))
    }

    override suspend fun restoreNote(item: NoteItem) {
        noteDataSource.update(noteConverter.toEntity(item.onRestore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(item: NoteItem) {
        clearConnection(item.id, item.rankId)
        noteDataSource.delete(noteConverter.toEntity(item))
    }

    /**
     * Remove connection between [RankEntity] and [NoteItem] (in case if last one will be delete).
     */
    @RunPrivate
    suspend fun clearConnection(noteId: Long, rankId: Long) {
        val entity = rankDataSource.get(rankId) ?: return
        entity.noteId.remove(noteId)
        rankDataSource.update(entity)
    }

    // Repo save and update functions

    override suspend fun convertNote(item: NoteItem.Text): NoteItem.Roll {
        val newItem = item.onConvert()

        for (it in newItem.list) {
            it.id = rollDataSource.insert(rollConverter.toEntity(newItem.id, it))
        }

        noteDataSource.update(noteConverter.toEntity(newItem))

        return newItem
    }

    override suspend fun convertNote(item: NoteItem.Roll, useCache: Boolean): NoteItem.Text {
        val newItem = if (useCache) {
            item.onConvert()
        } else {
            item.onConvert(rollConverter.toItem(rollDataSource.getList(item.id)))
        }

        noteDataSource.update(noteConverter.toEntity(newItem))
        rollDataSource.delete(newItem.id)

        return newItem
    }

    override suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean) {
        val entity = noteConverter.toEntity(item)

        if (isCreate) {
            /** Catch of insert errors happen inside dataSource. */
            item.id = noteDataSource.insert(entity) ?: return
        } else {
            noteDataSource.update(entity)
        }
    }

    override suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean) {
        val entity = noteConverter.toEntity(item)

        if (isCreate) {
            /** Catch of insert errors happen inside dataSource. */
            item.id = noteDataSource.insert(entity) ?: return

            for (rollItem in item.list) {
                rollItem.id = rollDataSource.insert(rollConverter.toEntity(item.id, rollItem))
            }
        } else {
            noteDataSource.update(entity)

            /** List of roll id's, which wasn't swiped. */
            val excludeIdList = ArrayList<Long>()

            for (rollItem in item.list) {
                val id = rollItem.id

                if (id == null) {
                    rollItem.id = rollDataSource.insert(rollConverter.toEntity(item.id, rollItem))
                } else {
                    rollDataSource.update(id, rollItem.position, rollItem.text)
                }

                rollItem.id?.let { excludeIdList.add(it) }
            }

            /**
             * Remove swiped rolls.
             */
            rollDataSource.delete(item.id, excludeIdList)
        }
    }

    override suspend fun updateRollCheck(item: NoteItem.Roll, p: Int) {
        val rollItem = item.list.getOrNull(p) ?: return
        val rollId = rollItem.id ?: return

        rollDataSource.updateCheck(rollId, rollItem.isCheck)
        noteDataSource.update(noteConverter.toEntity(item))
    }

    override suspend fun updateNote(item: NoteItem) {
        noteDataSource.update(noteConverter.toEntity(item))
    }

    override suspend fun setRollVisible(item: NoteItem.Roll) {
        val value = rollVisibleDataSource.getVisible(item.id)

        if (value == null) {
            val entity = RollVisibleEntity(noteId = item.id, value = item.isVisible)
            rollVisibleDataSource.insert(entity)
        } else if (item.isVisible != value) {
            rollVisibleDataSource.update(item.id, item.isVisible)
        }
    }
}