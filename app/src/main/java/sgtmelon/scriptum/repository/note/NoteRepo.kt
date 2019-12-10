package sgtmelon.scriptum.repository.note

import android.content.Context
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
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

    /**
     * [optimal] - need for note lists where displays short information.
     */
    override fun getList(@Sort sort: Int, bin: Boolean, optimal: Boolean,
                         filterVisible: Boolean): MutableList<NoteItem> {
        val itemList = ArrayList<NoteItem>()

        inRoom {
            var list = iNoteDao.getBySort(sort, bin) ?: return@inRoom

            /**
             * If need get all items.
             *
             * For example:
             * Need get all items for cancel bind in status bar.
             * Notes must be showed in bin list even if rank not visible.
             */
            if (filterVisible) list = iRankDao.filterVisible(list)

            list.forEach {
                val rollEntityList = iRollDao.getOptimal(it.id, optimal)
                val rollItemList = rollConverter.toItem(rollEntityList)

                itemList.add(noteConverter.toItem(it, rollItemList, iAlarmDao[it.id]))
            }
        }

        return itemList.correctRankSort(sort)
    }

    private fun INoteDao.getBySort(@Sort sort: Int, bin: Boolean): List<NoteEntity>? {
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
    private fun IRankDao.filterVisible(list: List<NoteEntity>): List<NoteEntity> {
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
    override fun getItem(id: Long, optimisation: Boolean): NoteItem? {
        val item: NoteItem?

        openRoom().apply {
            item = iNoteDao[id]?.let {
                val rollList = rollConverter.toItem(iRollDao.getOptimal(it.id, optimisation))
                return@let noteConverter.toItem(it, rollList, iAlarmDao[id])
            }
        }.close()

        return item
    }

    private fun IRollDao.getOptimal(id: Long, optimisation: Boolean): MutableList<RollEntity> {
        return if (optimisation) getView(id) else get(id)
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
     */
    override suspend fun getRollList(noteId: Long) = ArrayList<RollItem>().apply {
        inRoom { addAll(rollConverter.toItem(iRollDao[noteId])) }
    }


    /**
     * Have hide notes in list or not.
     */
    override fun isListHide(): Boolean {
        val isListHide: Boolean

        openRoom().apply {
            isListHide = iNoteDao[false].any {
                noteConverter.toItem(it).isNotVisible(iRankDao.getIdVisibleList())
            }
        }.close()

        return isListHide
    }

    override suspend fun clearBin() = inRoom2 {
        val noteList = iNoteDao[true].apply {
            forEach { iRankDao.clearConnection(it.id, it.rankId) }
        }

        iNoteDao.delete(noteList)
    }


    override suspend fun deleteNote(noteItem: NoteItem) = inRoom2 {
        iAlarmDao.delete(noteItem.id)
        iNoteDao.update(noteConverter.toEntity(noteItem.delete()))
    }

    override suspend fun restoreNote(noteItem: NoteItem) = inRoom2 {
        iNoteDao.update(noteConverter.toEntity(noteItem.restore()))
    }

    /**
     * Delete note forever and clear related categories
     */
    override suspend fun clearNote(noteItem: NoteItem) = inRoom2 {
        iRankDao.clearConnection(noteItem.id, noteItem.rankId)
        iNoteDao.delete(noteConverter.toEntity(noteItem))
    }


    override suspend fun convertToRoll(noteItem: NoteItem) {
        if (noteItem.type != NoteType.TEXT) return

        inRoom2 {
            noteItem.rollList.clear()

            var p = 0
            noteItem.textToList().forEach {
                noteItem.rollList.add(rollConverter.toItem(RollEntity().apply {
                    noteId = noteItem.id
                    position = p++
                    text = it
                    id = iRollDao.insert(rollEntity = this)
                }))
            }

            noteItem.convert().updateComplete(Complete.EMPTY)

            iNoteDao.update(noteConverter.toEntity(noteItem))
        }
    }

    override suspend fun convertToText(noteItem: NoteItem) {
        if (noteItem.type != NoteType.ROLL) return

        inRoom2 {
            noteItem.rollList.clear()
            noteItem.convert().text = rollConverter.toItem(iRollDao[noteItem.id]).getText()

            iNoteDao.update(noteConverter.toEntity(noteItem))
            iRollDao.delete(noteItem.id)
        }
    }

    override suspend fun getCopyText(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.name.isNotEmpty()) {
            append(noteItem.name).append("\n")
        }

        when (noteItem.type) {
            NoteType.TEXT -> append(noteItem.text)
            NoteType.ROLL -> inRoom2 {
                append(rollConverter.toItem(iRollDao[noteItem.id]).getText())
            }
        }
    }.toString()

    override suspend fun saveTextNote(noteItem: NoteItem, isCreate: Boolean) {
        if (noteItem.type != NoteType.TEXT) return

        inRoom2 {
            val entity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = iNoteDao.insert(entity)
            } else {
                iNoteDao.update(entity)
            }
        }
    }

    override suspend fun saveRollNote(noteItem: NoteItem, isCreate: Boolean) {
        if (noteItem.type != NoteType.ROLL) return

        inRoom2 {
            val noteEntity = noteConverter.toEntity(noteItem)

            if (isCreate) {
                noteItem.id = iNoteDao.insert(noteEntity)
                noteItem.rollList.forEach {
                    it.id = iRollDao.insert(rollConverter.toEntity(noteItem.id, it))
                }
            } else {
                iNoteDao.update(noteEntity)

                /**
                 * List of roll id's, which wasn't swiped.
                 */
                val idSaveList = ArrayList<Long>()

                noteItem.rollList.forEach {
                    val id = it.id

                    if (id == null) {
                        it.id = iRollDao.insert(rollConverter.toEntity(noteItem.id, it))
                    } else {
                        iRollDao.update(id, it.position, it.text)
                    }

                    it.id?.let { idSaveList.add(it) }
                }

                /**
                 * Remove swiped rolls.
                 */
                iRollDao.delete(noteItem.id, idSaveList)
            }
        }
    }

    override suspend fun updateRollCheck(noteItem: NoteItem, p: Int) = inRoom2 {
        val item = noteItem.rollList[p]

        val rollId = item.id ?: return@inRoom2

        iRollDao.update(rollId, item.isCheck)
        iNoteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean) = inRoom2 {
        iRollDao.updateAllCheck(noteItem.id, check)
        iNoteDao.update(noteConverter.toEntity(noteItem))
    }

    override suspend fun updateNote(noteItem: NoteItem) = inRoom2 {
        iNoteDao.update(noteConverter.toEntity(noteItem))
    }


    /**
     * Remove relation between [RankEntity] and [NoteItem] which will be delete
     */
    private suspend fun IRankDao.clearConnection(noteId: Long, rankId: Long) {
        val entity = get(rankId)?.apply { this.noteId.remove(noteId) } ?: return
        update(entity)
    }

}