package sgtmelon.scriptum.app.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.RankModel
import sgtmelon.scriptum.app.model.data.ColorData.size
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter
import sgtmelon.scriptum.app.room.dao.RankDao
import sgtmelon.scriptum.app.room.dao.RollDao
import sgtmelon.scriptum.app.screen.main.notes.NotesViewModel
import sgtmelon.scriptum.app.model.key.DbField
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class RoomRepo(private val context: Context) : IRoomRepo {

    private val preference = Preference(context)

    // TODO !! заменить db на openRoom (добавить extension для возврата val)

    private lateinit var db: RoomDb

    // TODO think about it
    private fun openRoom() = RoomDb.getInstance(context)

    private fun getNoteListQuery(fromBin: Boolean) = SimpleSQLiteQuery(
            "SELECT * FROM ${DbField.Note.TABLE}" +
                    " WHERE ${DbField.Note.BIN } = ${BoolConverter().toInt(fromBin)}" +
                    " ORDER BY ${preference.sortNoteOrder}")

    override fun getNoteModelList(bin: Boolean): MutableList<NoteModel> {
        val list = ArrayList<NoteModel>()

        openRoom().apply {
            val rankVisibleList = getRankDao().rankVisibleList

            getNoteDao()[getNoteListQuery(bin)].forEach {
                val statusItem = StatusItem(context, it, notify = false)

                if (it.rankId.isNotEmpty() && !rankVisibleList.contains(it.rankId[0])) {
                    statusItem.cancelNote()
                } else {
                    if (it.isStatus && NotesViewModel.updateStatus) {
                        statusItem.notifyNote()
                    }

                    list.add(NoteModel(it, getRollDao().getView(it.id), statusItem))
                }
            }
        }.close()

        return list
    }

    override fun clearBin() = openRoom().apply {
        val noteList = getNoteDao()[true].apply {
            forEach { clearRank(it.id, it.rankId, getRankDao()) }
        }

        getNoteDao().delete(noteList)
    }.close()

    override fun deleteNote(id: Long) = openRoom().apply {
        getNoteDao().apply {
            update(id, context.getTime(), bin = true)
            update(id, status = false)
        }
    }.close()

    override fun restoreNote(id: Long) =
            openRoom().apply { getNoteDao().update(id, context.getTime(), bin = false) }.close()

    /**
     * Удаление заметки с отчисткой категории
     */
    override fun clearNote(id: Long) = openRoom().apply {
        with(getNoteDao()[id]) {
            clearRank(id, rankId, getRankDao())
            getNoteDao().delete(item = this)
        }
    }.close()

    /**
     * [noteId] - Id заметки, которую надо убрать из категории
     * [rankIdList] - Массив из id категорий, принадлежащих заметке
     */
    private fun clearRank(noteId: Long, rankIdList: List<Long>, rankDao: RankDao) = with(rankDao) {
        if (rankIdList.isEmpty()) return@with

        with(get(rankIdList)) {
            forEach { it.noteId.remove(noteId) }
            update(rankList = this)
        }
    }

    override fun getRankVisibleList(): List<Long> {
        val list = ArrayList<Long>()

        openRoom().apply { list.addAll(getRankDao().rankVisibleList) }.close()

        return list
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        db = RoomDb.getInstance(context)
        val noteItem = db.getNoteDao()[id]
        val rollList = db.getRollDao()[id]

        val noteModel = NoteModel(noteItem, rollList, StatusItem(context, noteItem, notify = false))
        db.close()

        return noteModel
    }

    override fun getRankDialogName(): Array<String> {
        db = RoomDb.getInstance(context)
        val array = db.getRankDao().name
        db.close()

        return array
    }

    override fun getRankCheck(rankId: List<Long>): BooleanArray {
        db = RoomDb.getInstance(context)
        val array = getRankCheck(rankId, db)
        db.close()

        return array
    }

    private fun getRankCheck(rankId: List<Long>, db: RoomDb): BooleanArray = with(db) {
        val rankList = getRankDao().simple
        val check = BooleanArray(rankList.size)

        rankList.forEachIndexed { i, item -> check[i] = rankId.contains(item.id) }

        return check
    }

    override fun convertToRoll(noteModel: NoteModel): NoteModel {
        openRoom().apply {
            noteModel.apply {
                listRoll.clear()

                val textToRoll = noteItem.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()

                var p = 0
                for (toRoll in textToRoll) {
                    if (toRoll.isEmpty()) continue

                    listRoll.add(RollItem().apply {
                        noteId = noteItem.id
                        position = p++
                        text = toRoll
                        id = getRollDao().insert(rollItem = this)
                    })
                }

                noteItem.apply {
                    change = context.getTime()
                    type = NoteType.ROLL
                    setRollText(0, size)
                }
            }
        }.close()

        return noteModel
    }

    override fun convertToText(noteModel: NoteModel): NoteModel {
        openRoom().apply {
            noteModel.apply {
                noteItem.apply {
                    change = context.getTime()
                    type = NoteType.TEXT
                    text = getTextFromRollNote(getRollDao(), noteItem.id)
                }

                getNoteDao().update(noteItem)
                getRollDao().delete(noteItem.id)

                listRoll.clear()
            }
        }.close()

        return noteModel
    }

    /**
     * Получение текста для текстовой заметки на основе списка
     */
    override fun getCopyRoll(noteId: Long): String {
        val builder = StringBuilder()

        openRoom().apply { builder.append(getTextFromRollNote(getRollDao(), noteId)) }.close()

        return builder.toString()
    }

    private fun getTextFromRollNote(rollDao: RollDao, noteId: Long) = StringBuilder().apply {
        rollDao[noteId].forEachIndexed { i, item ->
            if (i != 0) append("\n")
            append(item.text)
        }
    }.toString()

    /**
     * Получение текста для уведомления на основе списка
     */
    override fun getTextForStatus(noteId: Long, check: String) = StringBuilder().apply {
        openRoom().apply {
            with(getRollDao()[noteId]) {
                append("$check |")

                forEachIndexed { i, item ->
                    if (item.isCheck) append(" \u2713 ") else append(" - ")

                    append(item.text)

                    if (i != size - 1) append(" |")
                }
            }
        }.close()
    }.toString()

    override fun getRankId(): Array<Long> {
        db = RoomDb.getInstance(context)
        val array: Array<Long> = db.getRankDao().id
        db.close()

        return array
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel {
        val noteItem = noteModel.noteItem

        openRoom().apply {
            if (isCreate) {
                noteItem.id = getNoteDao().insert(noteItem)
            } else {
                getNoteDao().update(noteItem)
            }
        }.close()

        updateRank(noteItem.id, noteItem.rankId)

        return noteModel
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel {
        val noteItem = noteModel.noteItem
        val listRoll = noteModel.listRoll

        openRoom().apply {
            if (isCreate) {
                noteItem.id = getNoteDao().insert(noteItem)

                /**
                 * Запись в пунктов в БД
                 */
                listRoll.forEachIndexed { index, rollItem ->
                    rollItem.apply {
                        noteId = noteItem.id
                        position = index
                        id = getRollDao().insert(rollItem)
                    }
                }
            } else {
                getNoteDao().update(noteItem)

                val idSaveList = ArrayList<Long>()

                listRoll.forEachIndexed { index, rollItem ->
                    rollItem.position = index

                    val id = rollItem.id
                    if (id == null) {
                        rollItem.id = getRollDao().insert(rollItem)
                    } else {
                        getRollDao().update(id, index, rollItem.text)
                    }

                    rollItem.id?.let { idSaveList.add(it) }
                }

                /**
                 * Удаление пунктов, которые swipe
                 */
                getRollDao().delete(noteItem.id, idSaveList)
            }
        }.close()

        updateRank(noteItem.id, noteItem.rankId)

        return noteModel
    }

    /**
     * Добавление или удаление id заметки к категорииё
     * [rankIdList] - Id категорий принадлежащих заметке
     */
    private fun updateRank(noteId: Long, rankIdList: List<Long>) = openRoom().apply {
        val list = getRankDao().simple
        val check = getRankCheck(rankIdList, db = this)

        list.forEachIndexed { i, item ->
            if (check[i] && !item.noteId.contains(noteId)) {
                item.noteId.add(noteId)
            } else if (!check[i]) {
                item.noteId.remove(noteId)
            }
        }

        getRankDao().update(list)
    }.close()

    override fun insertRank(p: Int, rankItem: RankItem): Long {
        var id: Long = 0

        openRoom().apply {
            id = getRankDao().insert(rankItem)
            if (p != 0) updateRankFromPosition(p, db = this)
        }.close()

        return id
    }

    /**
     * @param p - Позиция удаления категории
     */
    private fun updateRankFromPosition(p: Int, db: RoomDb) = with(db) {
        val rankList = getRankDao().simple
        val noteIdList = java.util.ArrayList<Long>()

        for (i in p until rankList.size) {
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

    override fun updateRollCheck(rollItem: RollItem, noteItem: NoteItem) { // TODO переделать
        rollItem.id?.let { id ->
            openRoom().apply {
                getRollDao().update(id, rollItem.isCheck)
                getNoteDao().update(noteItem)
            }.close()
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, isAll: Boolean) =
            openRoom().apply {
                getRollDao().updateAllCheck(noteItem.id, isAll)
                getNoteDao().update(noteItem)
            }.close()

    override fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean) =
            openRoom().apply {
                getRollDao().updateAllCheck(noteItem.id, check)
                getNoteDao().update(noteItem)
            }.close()

    override fun updateNoteItemBind(id: Long, status: Boolean) =
            openRoom().apply { getNoteDao().update(id, status) }.close()

    override fun updateNoteItem(noteItem: NoteItem) =
            openRoom().apply { getNoteDao().update(noteItem) }.close()

    /**
     * Обновление элементов списка в статус баре
     */
    override fun updateStatus() = openRoom().apply {
        val rankVisible = getRankDao().rankVisibleList

        getNoteDao()[getNoteListQuery(fromBin = false)].forEach {
            val statusItem = StatusItem(context, it, notify = false)

            if (it.rankId.isNotEmpty() && !rankVisible.contains(it.rankId[0])) {
                statusItem.cancelNote()
            } else if (it.isStatus) {
                statusItem.notifyNote()
            }
        }
    }.close()

    override fun deleteRank(name: String, p: Int) = openRoom().apply {
        with(getRankDao()[name]) {
            if (noteId.isNotEmpty()) {
                val noteList = getNoteDao()[noteId]

                /**
                 * Убирает из списков ненужную категорию по id
                 */
                noteList.forEach {
                    val index = it.rankId.indexOf(id)

                    it.rankId.removeAt(index)
                    it.rankPs.removeAt(index)
                }

                getNoteDao().update(noteList)
            }

            getRankDao().delete(rankItem = this)
        }

        updateRankFromPosition(p, this)
        updateStatus()
    }.close()

    override fun getRankModel(): RankModel {
        val list = getRankComplexList()

        return RankModel(list, ArrayList<String>().apply {
            list.forEach { add(it.name.toUpperCase()) }
        })
    }

    override fun updateRank(dragFrom: Int, dragTo: Int): List<RankItem> { // TODO разобрать
        val startFirst = dragFrom < dragTo

        val iStart = if (startFirst) dragFrom else dragTo
        val iEnd = if (startFirst) dragTo else dragFrom
        val iAdd = if (startFirst) -1 else 1

        val rankList = getRankComplexList()
        val noteIdList = ArrayList<Long>()

        for (i in iStart..iEnd) {
            val rankItem = rankList[i]

            for (id in rankItem.noteId) {
                if (!noteIdList.contains(id)) {
                    noteIdList.add(id)
                }
            }

            val start = i == dragFrom
            val end = i == dragTo

            val newPosition = if (start) dragTo else i + iAdd
            rankItem.position = newPosition

            if (startFirst) {
                if (end) {
                    rankList.removeAt(i)
                    rankList.add(newPosition, rankItem)
                } else {
                    rankList[i] = rankItem
                }
            } else {
                if (start) {
                    rankList.removeAt(i)
                    rankList.add(newPosition, rankItem)
                } else {
                    rankList[i] = rankItem
                }
            }
        }

        if (rankList[0].position != 0) rankList.reverse()

        openRoom().apply {
            getRankDao().update(rankList)
            updateNoteRankPosition(noteIdList, rankList, db = this)
        }.close()

        return rankList
    }


    private fun getRankComplexList(): MutableList<RankItem> {  // TODO !! добавить конвертирование типов
        val list = ArrayList<RankItem>()

        openRoom().apply {
            list.addAll(getRankDao().simple)
            list.forEach {
                it.textCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.TEXT))
                it.rollCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.ROLL))
            }
        }.close()

        return list
    }

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}