package sgtmelon.scriptum.data.repository.room

import android.content.Context
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
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.extension.move

/**
 * Repository of [RoomDb] which work with notes
 *
 * @param context for open [RoomDb]
 */
class NoteRepo(override val context: Context) : INoteRepo, IRoomWork {

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    override suspend fun getCount(bin: Boolean): Int {
        val count: Int

        openRoom().apply {
            val rankIdList = if (bin) rankDao.getIdList() else rankDao.getIdVisibleList()
            count = noteDao.getCount(bin, rankIdList)
        }.close()

        return count
    }

    /**
     * [optimal] - need for note lists where displays short information.
     */
    override suspend fun getList(@Sort sort: Int, bin: Boolean, optimal: Boolean,
                         filterVisible: Boolean): MutableList<NoteItem> {
        val itemList = ArrayList<NoteItem>()

        inRoom {
            var list = noteDao.getSortBy(sort, bin) ?: return@inRoom

            /**
             * If need get all items.
             *
             * For example:
             * Need get all items for cancel bind in status bar.
             * Notes must be showed in bin list even if rank not visible.
             */
            if (filterVisible) list = rankDao.filterVisible(list)

            list.forEach {
                val rollEntityList = rollDao.getPreview(it.id, optimal)
                val rollItemList = rollConverter.toItem(rollEntityList)

                itemList.add(noteConverter.toItem(it, rollItemList, alarmDao.get(it.id)))
            }
        }

        return itemList.correctRankSort(sort)
    }

    private suspend fun INoteDao.getSortBy(@Sort sort: Int, bin: Boolean): List<NoteEntity>? {
        return when (sort) {
            Sort.CHANGE -> getByChange(bin)
            Sort.CREATE -> getByCreate(bin)
            Sort.RANK -> getByRank(bin)
            Sort.COLOR -> getByColor(bin)
            else -> null
        }
    }

    /**
     * List must contains only item which isVisible.
     */
    private suspend fun IRankDao.filterVisible(list: List<NoteEntity>): List<NoteEntity> {
        val idVisibleList = getIdVisibleList()

        return list.filter { noteConverter.toItem(it).isVisible(idVisibleList)  }
    }

    private fun MutableList<NoteItem>.correctRankSort(@Sort sort: Int) = apply {
        if (sort != Sort.RANK) return@apply

        /**
         * List must contains item with and without rank.
         */
        if (any { it.haveRank() } && any { !it.haveRank() } ) {

            /**
             * Move items without rank to list end.
             */
            while (!first().haveRank()) move(from = 0)
        }
    }


    /**
     * Return null if note doesn't exist.
     *
     * [optimisation] - need for note lists where displays short information.
     */
    override suspend fun getItem(id: Long, optimisation: Boolean): NoteItem? {
        val item: NoteItem?

        openRoom().apply {
            item = noteDao.get(id)?.let {
                val rollList = rollConverter.toItem(rollDao.getPreview(it.id, optimisation))
                return@let noteConverter.toItem(it, rollList, alarmDao.get(id))
            }
        }.close()

        return item
    }

    private suspend fun IRollDao.getPreview(id: Long, optimisation: Boolean): MutableList<RollEntity> {
        return if (optimisation) getView(id) else get(id)
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override suspend fun getRollList(noteId: Long) = ArrayList<RollItem>().apply {
        inRoom { addAll(rollConverter.toItem(rollDao.get(noteId))) }
    }


    /**
     * Have hide notes in list or not.
     */
    override suspend fun isListHide(): Boolean {
        val isListHide: Boolean

        openRoom().apply {
            isListHide = noteDao.get(false).any {
                !noteConverter.toItem(it).isVisible(rankDao.getIdVisibleList())
            }
        }.close()

        return isListHide
    }

    override suspend fun clearBin() = inRoom {
        val noteList = noteDao.get(true).apply {
            forEach { rankDao.clearConnection(it.id, it.rankId) }
        }

        noteDao.delete(noteList)
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
        rankDao.clearConnection(noteItem.id, noteItem.rankId)
        noteDao.delete(noteConverter.toEntity(noteItem))
    }


    override suspend fun convertNote(noteItem: NoteItem.Text): NoteItem.Roll {
        val convertItem = noteItem.onConvert()

        inRoom {
            convertItem.rollList.forEach {
                it.id = rollDao.insert(rollConverter.toEntity(convertItem.id, it))
            }

            noteDao.update(noteConverter.toEntity(convertItem))
        }

        return convertItem
    }

    override suspend fun convertNote(noteItem: NoteItem.Roll, useCache: Boolean): NoteItem.Text {
        val convertItem: NoteItem.Text

        openRoom().apply {
            convertItem = if (useCache) {
                noteItem.onConvert()
            } else {
                noteItem.onConvert(rollConverter.toItem(rollDao.get(noteItem.id)))
            }

            noteDao.update(noteConverter.toEntity(convertItem))
            rollDao.delete(convertItem.id)
        }.close()

        return convertItem
    }

    override suspend fun getCopyText(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.name.isNotEmpty()) {
            append(noteItem.name).append("\n")
        }

        when (noteItem.type) {
            NoteType.TEXT -> append(noteItem.text)
            NoteType.ROLL -> inRoom {
                append(rollConverter.toItem(rollDao.get(noteItem.id)).getText())
            }
        }
    }.toString()

    override suspend fun saveNote(noteItem: NoteItem.Text, isCreate: Boolean) {
        if (noteItem.type != NoteType.TEXT) return

        inRoom {
            val entity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = noteDao.insert(entity)
            } else {
                noteDao.update(entity)
            }
        }
    }

    override suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean) {
        if (noteItem.type != NoteType.ROLL) return

        inRoom {
            val noteEntity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = noteDao.insert(noteEntity)
                noteItem.rollList.forEach {
                    it.id = rollDao.insert(rollConverter.toEntity(noteItem.id, it))
                }
            } else {
                noteDao.update(noteEntity)

                /**
                 * List of roll id's, which wasn't swiped.
                 */
                val idSaveList = ArrayList<Long>()

                noteItem.rollList.forEach { item ->
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
    }

    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) = inRoom {
        val item = noteItem.rollList[p]

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

        if (isVisible == value) return@inRoom

        if (value == null) {
            rollVisibleDao.insert(RollVisibleEntity(noteId = noteId, value = isVisible))
        } else {
            rollVisibleDao.update(noteId, isVisible)
        }
    }

    override suspend fun getRollVisible(noteId: Long): Boolean {
        val isVisible: Boolean

        openRoom().apply {
            isVisible = rollVisibleDao.get(noteId) ?: run {
                RollVisibleEntity(noteId = noteId).also { rollVisibleDao.insert(it) }.value
            }
        }.close()

        return isVisible
    }


    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    private suspend fun IRankDao.clearConnection(noteId: Long, rankId: Long) {
        val entity = get(rankId)?.apply { this.noteId.remove(noteId) } ?: return
        update(entity)
    }

}