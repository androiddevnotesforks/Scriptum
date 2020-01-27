package sgtmelon.scriptum.repository.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.model.NoteConverter
import sgtmelon.scriptum.room.converter.model.RollConverter
import sgtmelon.scriptum.room.dao.INoteDao
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.dao.IRollDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity

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
                val rollEntityList = rollDao.getOptimal(it.id, optimal)
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
                val rollList = rollConverter.toItem(rollDao.getOptimal(it.id, optimisation))
                return@let noteConverter.toItem(it, rollList, alarmDao.get(id))
            }
        }.close()

        return item
    }

    private suspend fun IRollDao.getOptimal(id: Long, optimisation: Boolean): MutableList<RollEntity> {
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
                noteConverter.toItem(it).isNotVisible(rankDao.getIdVisibleList())
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
        noteDao.update(noteConverter.toEntity(noteItem.delete()))
    }

    override suspend fun restoreNote(noteItem: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(noteItem.restore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(noteItem: NoteItem) = inRoom {
        rankDao.clearConnection(noteItem.id, noteItem.rankId)
        noteDao.delete(noteConverter.toEntity(noteItem))
    }


    override suspend fun convertToRoll(noteItem: NoteItem) {
        if (noteItem.type != NoteType.TEXT) return

        noteItem.onConvertText()

        inRoom {
            noteItem.rollList.forEach {
                it.id = rollDao.insert(rollConverter.toEntity(noteItem.id, it))
            }

            noteDao.update(noteConverter.toEntity(noteItem))
        }
    }

    override suspend fun convertToText(noteItem: NoteItem, useCache: Boolean) {
        if (noteItem.type != NoteType.ROLL) return

        inRoom {
            if (useCache) {
                noteItem.onConvertRoll()
            } else {
                noteItem.onConvertRoll(rollConverter.toItem(rollDao.get(noteItem.id)))
            }

            noteDao.update(noteConverter.toEntity(noteItem))
            rollDao.delete(noteItem.id)
        }
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

    override suspend fun saveTextNote(noteItem: NoteItem, isCreate: Boolean) {
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

    override suspend fun saveRollNote(noteItem: NoteItem, isCreate: Boolean) {
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

    override suspend fun updateRollCheck(noteItem: NoteItem, p: Int) = inRoom {
        val item = noteItem.rollList[p]

        val rollId = item.id ?: return@inRoom

        rollDao.update(rollId, item.isCheck)
        noteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean) = inRoom {
        rollDao.updateAllCheck(noteItem.id, check)
        noteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateNote(noteItem: NoteItem) = inRoom {
        noteDao.update(noteConverter.toEntity(noteItem))
    }


    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    private suspend fun IRankDao.clearConnection(noteId: Long, rankId: Long) {
        val entity = get(rankId)?.apply { this.noteId.remove(noteId) } ?: return
        update(entity)
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onConvertText() {
            if (type != NoteType.TEXT) return

            rollList.clear()

            var p = 0
            textToList().forEach {
                rollList.add(RollItem(position = p++, text = it))
            }

            convert().updateComplete(Complete.EMPTY)
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onConvertRoll() {
            if (type != NoteType.ROLL) return

            convert().text = rollList.getText()
            rollList.clear()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onConvertRoll(list: List<RollItem>) {
            if (type != NoteType.ROLL) return

            rollList.clear()
            convert().text = list.getText()
        }
    }

}