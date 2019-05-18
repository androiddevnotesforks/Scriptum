package sgtmelon.scriptum.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.DbField
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Репозиторий для обработки данных [RoomDb]
 *
 * @param context для открытия [RoomDb] и получения данных из [Preference]
 *
 * @author SerjantArbuz
 */
class RoomRepo(private val context: Context) : IRoomRepo {

    private val preference = Preference(context)

    private fun openRoom() = RoomDb.getInstance(context)

    private fun getNoteListQuery(bin: Boolean) = SimpleSQLiteQuery(
            "SELECT * FROM ${DbField.Note.TABLE}" +
                    " WHERE ${DbField.Note.BIN} = ${BoolConverter().toInt(bin)}" +
                    " ORDER BY ${preference.sortNoteOrder}")

    override fun getNoteModelList(bin: Boolean) = ArrayList<NoteModel>().apply {
        openRoom().apply {
            val rankIdVisibleList = getRankDao().rankIdVisibleList

            getNoteDao()[getNoteListQuery(bin)].forEach {
                val statusItem = StatusItem(context, it, notify = false)

                if (it.rankId.isNotEmpty() && !rankIdVisibleList.contains(it.rankId[0])) {
                    statusItem.cancelNote()
                } else {
                    if (it.isStatus && NotesViewModel.updateStatus) {
                        statusItem.notifyNote()
                    }

                    add(NoteModel(it, getRollDao().getView(it.id), statusItem))
                }
            }
        }.close()
    }

    override suspend fun clearBin() = openRoom().apply {
        val noteList = getNoteDao()[true].apply {
            forEach { clearRankConnection(getRankDao(), it) }
        }

        getNoteDao().delete(noteList)
    }.close()

    override suspend fun deleteNote(item: NoteItem) = openRoom().apply {
        getNoteDao().update(item.apply {
            change = context.getTime()
            isBin = true
            isStatus = false
        })
    }.close()

    override suspend fun restoreNote(item: NoteItem) = openRoom().apply {
        getNoteDao().update(item.apply {
            change = context.getTime()
            isBin = false
        })
    }.close()

    override suspend fun clearNote(item: NoteItem) = openRoom().apply {
        clearRankConnection(getRankDao(), item)
        getNoteDao().delete(item)
    }.close()

    override fun getRankIdVisibleList() = ArrayList<Long>().apply {
        openRoom().apply { addAll(getRankDao().rankIdVisibleList) }.close()
    }

    override fun getRankCount(): Boolean {
        val count: Int

        openRoom().apply { count = getRankDao().count }.close()

        return count == 0
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        val noteModel: NoteModel

        openRoom().apply {
            val noteItem = getNoteDao()[id]
            val statusItem = StatusItem(context, noteItem, notify = false)

            noteModel = NoteModel(noteItem, getRollDao()[id], statusItem)
        }.close()

        return noteModel
    }

    override fun getRankNameList() = ArrayList<String>().apply {
        openRoom().apply { addAll(getRankDao().name) }.close()
    }

    override fun getRankCheckArray(noteItem: NoteItem): BooleanArray {
        val array: BooleanArray
        openRoom().apply { array = calculateRankCheckArray(noteItem, db = this) }.close()
        return array
    }

    override fun convertToRoll(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteItem.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

        openRoom().apply {
            listRoll.clear()

            var p = 0
            noteItem.splitTextForRoll().forEach {
                if (it.isNotEmpty()) listRoll.add(RollItem().apply {
                    noteId = noteItem.id
                    position = p++
                    text = it
                    id = getRollDao().insert(rollItem = this)
                })
            }

            noteItem.apply {
                change = context.getTime()
                type = NoteType.ROLL
                setCompleteText(check = 0, size = listRoll.size)
            }

            getNoteDao().update(noteItem)
        }.close()
    }

    override fun convertToText(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            noteItem.apply {
                change = context.getTime()
                type = NoteType.TEXT
                text = getRollDao()[id].joinToString(separator = "\n") { it.text }
            }

            getNoteDao().update(noteItem)
            getRollDao().delete(noteItem.id)

            listRoll.clear()
        }.close()
    }

    override fun getRollListString(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            append(getRollDao()[noteItem.id].joinToString(separator = "\n") { it.text })
        }.close()
    }.toString()

    override fun getRollStatusString(noteItem: NoteItem) = StringBuilder().apply {
        if (noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            append(getRollDao()[noteItem.id]
                    .joinToString(prefix = "${noteItem.text}\n", separator = "\n") {
                        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
                    }
            )
        }.close()
    }.toString()

    override fun getRankIdList() = ArrayList<Long>().apply {
        openRoom().apply { addAll(getRankDao().id) }.close()
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteItem.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

        openRoom().apply {
            with(getNoteDao()) {
                if (isCreate) noteItem.id = insert(noteItem) else update(noteItem)
            }
        }.close()

        updateRank(noteItem)
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        //TODO !! Оптимизировать
        val listRollTemp = listRoll.filterNot { it.text.isEmpty() }
        listRoll.clear()
        listRoll.addAll(listRollTemp)

        openRoom().apply {
            if (isCreate) {
                noteItem.id = getNoteDao().insert(noteItem)

                /**
                 * Запись в пунктов в БД
                 */
                listRoll.forEachIndexed { i, item ->
                    item.apply {
                        noteId = noteItem.id
                        position = i
                    }.id = getRollDao().insert(item)
                }
            } else {
                getNoteDao().update(noteItem)

                val idSaveList = ArrayList<Long>()

                listRoll.forEachIndexed { i, item ->
                    item.position = i

                    val id = item.id
                    if (id == null) {
                        item.id = getRollDao().insert(item)
                    } else {
                        getRollDao().update(id, i, item.text)
                    }

                    item.id?.let { idSaveList.add(it) }
                }

                /**
                 * Удаление пунктов, которые swipe
                 */
                getRollDao().delete(noteItem.id, idSaveList)
            }
        }.close()

        updateRank(noteItem)
    }

    override fun insertRank(p: Int, rankItem: RankItem): Long {
        val id: Long

        openRoom().apply {
            id = getRankDao().insert(rankItem)
            if (p != 0) updateRankPosition(p, db = this)
        }.close()

        return id
    }

    override fun updateRollCheck(noteItem: NoteItem, rollItem: RollItem) {
        rollItem.id?.let {
            openRoom().apply {
                getRollDao().update(it, rollItem.isCheck)
                getNoteDao().update(noteItem)
            }.close()
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, check: Boolean) =
            openRoom().apply {
                getRollDao().updateAllCheck(noteItem.id, check)
                getNoteDao().update(noteItem)
            }.close()

    override fun updateNote(noteItem: NoteItem) =
            openRoom().apply { getNoteDao().update(noteItem) }.close()

    override suspend fun notifyStatusBar() = openRoom().apply {
        val rankIdVisibleList = getRankDao().rankIdVisibleList

        getNoteDao()[getNoteListQuery(bin = false)].forEach {
            val statusItem = StatusItem(context, it, notify = false)

            if (it.rankId.isNotEmpty() && !rankIdVisibleList.contains(it.rankId[0])) {
                statusItem.cancelNote()
            } else if (it.isStatus) {
                statusItem.notifyNote()
            }
        }
    }.close()

    override fun deleteRank(name: String, p: Int) = openRoom().apply {
        val rankItem = getRankDao()[name]

        if (rankItem.noteId.isNotEmpty()) {
            val noteList = getNoteDao()[rankItem.noteId]

            /**
             * Убирает из списков ненужную категорию по id
             */
            noteList.forEach {
                val index = it.rankId.indexOf(rankItem.id)

                it.rankId.removeAt(index)
                it.rankPs.removeAt(index)
            }

            getNoteDao().update(noteList)
        }

        getRankDao().delete(rankItem)

        updateRankPosition(p, db = this)
    }.close()

    override fun getRankModel() = RankModel(getCompleteRankList())

    override fun updateRank(dragFrom: Int, dragTo: Int): MutableList<RankItem> { // TODO оптимизировать
        val startFirst = dragFrom < dragTo

        val iStart = if (startFirst) dragFrom else dragTo
        val iEnd = if (startFirst) dragTo else dragFrom
        val iAdd = if (startFirst) -1 else 1

        val rankList = getCompleteRankList()
        val noteIdList = ArrayList<Long>()

        for (i in iStart..iEnd) {
            val rankItem = rankList[i]
            rankItem.noteId.forEach { if (noteIdList.contains(it)) noteIdList.add(it) }

            val start = i == dragFrom
            val end = i == dragTo

            val newPosition = if (start) dragTo else i + iAdd
            rankItem.position = newPosition

            if (if (startFirst) end else start) {
                rankList.removeAt(i)
                rankList.add(newPosition, rankItem)
            } else {
                rankList[i] = rankItem
            }
        }

        rankList.sortBy { it.position }

        openRoom().apply {
            getRankDao().update(rankList)
            updateNoteRankPosition(noteIdList, rankList, db = this)
        }.close()

        return rankList
    }

    override fun updateRank(rankItem: RankItem) =
            openRoom().apply { getRankDao().update(rankItem) }.close()

    override fun updateRank(rankList: List<RankItem>) =
            openRoom().apply { getRankDao().update(rankList) }.close()

    // TODO прибрать private

    private fun getCompleteRankList() = ArrayList<RankItem>().apply {
        openRoom().apply {
            addAll(getRankDao().simple)
            forEach {
                it.textCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.TEXT))
                it.rollCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.ROLL))
            }
        }.close()
    }

    /**
     * @param fromPosition - Позиция удаления категории
     */
    private fun updateRankPosition(fromPosition: Int, db: RoomDb) = with(db) {
        val rankList = getRankDao().simple
        val noteIdList = ArrayList<Long>()

        for (i in fromPosition until rankList.size) {
            rankList[i].apply {
                noteId.forEach { if (!noteIdList.contains(it)) noteIdList.add(it) }
                position = i
            }
        }

        getRankDao().update(rankList)
        updateNoteRankPosition(noteIdList, rankList, db = this)
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    private fun updateNoteRankPosition(noteIdList: List<Long>, rankList: List<RankItem>, db: RoomDb) =
            with(db.getNoteDao()) {
                val noteList = get(noteIdList)

                noteList.forEach { item ->
                    val newIdList = ArrayList<Long>()
                    val newPsList = ArrayList<Long>()

                    rankList.forEach {
                        if (item.rankId.contains(it.id)) {
                            newIdList.add(it.id)
                            newPsList.add(it.position.toLong())
                        }
                    }

                    item.rankId = newIdList
                    item.rankPs = newPsList
                }

                update(noteList)
            }

    /**
     * Добавление или удаление id заметки к категорииё
     */
    private fun updateRank(noteItem: NoteItem) = openRoom().apply {
        val list = getRankDao().simple
        val check = calculateRankCheckArray(noteItem, db = this)

        val id = noteItem.id
        list.forEachIndexed { i, item ->
            if (check[i] && !item.noteId.contains(id)) {
                item.noteId.add(id)
            } else if (!check[i]) {
                item.noteId.remove(id)
            }
        }

        getRankDao().update(list)
    }.close()

    /**
     * Удаление связи между Rank и Note
     *
     * @param rankDao передаётся таким образом, чтобы не закрыть db
     * @param noteItem заметка, которая будет удалена
     */
    private fun clearRankConnection(rankDao: RankDao, noteItem: NoteItem) {
        if (noteItem.rankId.isEmpty()) return

        rankDao[noteItem.rankId].apply {
            forEach { it.noteId.remove(noteItem.id) }
            rankDao.update(list = this)
        }
    }

    private fun calculateRankCheckArray(noteItem: NoteItem, db: RoomDb): BooleanArray {
        val rankList = db.getRankDao().simple
        val check = BooleanArray(rankList.size)

        rankList.forEachIndexed { i, item -> check[i] = noteItem.rankId.contains(item.id) }

        return check
    }

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}